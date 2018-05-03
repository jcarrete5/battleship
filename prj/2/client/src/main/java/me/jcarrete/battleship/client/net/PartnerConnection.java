package me.jcarrete.battleship.client.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.*;
import java.util.logging.Level;

import static me.jcarrete.battleship.client.BattleshipClient.LOGGER;

/**
 * Acts as a way to send data to another player over a network.
 */
public class PartnerConnection implements AutoCloseable {

	private Socket socket;
	private DataInputStream in;
	private DataOutputStream out;
	private ExecutorService executorService;
	private LinkedBlockingQueue<NetMessage> messageQueue;
	private Thread receiveMessageThread;

	PartnerConnection(Socket s) throws IOException {
		socket = s;
		in = new DataInputStream(s.getInputStream());
		out = new DataOutputStream(s.getOutputStream());
		executorService = Executors.newCachedThreadPool();
		messageQueue = new LinkedBlockingQueue<>();
		receiveMessageThread = new Thread(() -> {
			while (!Thread.interrupted()) {
				try {
					NetMessage msg = receive();
					messageQueue.put(msg);
				} catch (SocketException e) {
					LOGGER.info("Input stream to partner closed");
					Thread.currentThread().interrupt();
				} catch (EOFException e) {
					LOGGER.info("Input stream has ended");
					Thread.currentThread().interrupt();
				} catch (IOException e) {
					LOGGER.log(Level.WARNING, "Dropped a message", e);
				} catch (InterruptedException e) {
					LOGGER.info("receiveMessageThread interrupted");
					Thread.currentThread().interrupt();
				}
			}
		}, "receive-message-thread");
		receiveMessageThread.start();
	}

	public String remoteAddressAndPortAsString() {
		return socket.getInetAddress() + ":" + socket.getPort();
	}

	/**
	 * Blocking function that waits for a message from the partner. Be careful using this
	 * as it will prevent the message from propagating to any thread that could be waiting
	 * for a {@link NetMessage}.
	 * @return The {@link NetMessage} received from the partner.
	 * @throws SocketException when the input stream is closed during a read
	 * @throws IOException the input stream has been closed and the contained input
	 * stream does not support reading after close, or another I/O error occurs.
	 * @throws EOFException the input stream has reached the end (usually due to
	 * socket being closed on other end.
	 */
	private NetMessage receive() throws IOException {
		LOGGER.fine("Waiting for a message from the partner...");

		// Read a message from partner
		final int msgType = in.readInt();
		final int bodyLength = in.readInt();
		final byte[] body = new byte[bodyLength];

		int bytesRead = 0;
		while (bytesRead < bodyLength) {
			bytesRead += in.read(body, bytesRead, bodyLength - bytesRead);
		}

		LOGGER.fine("Received a message from the partner");
		return new NetMessage(msgType, bodyLength, body);
	}

	/**
	 * Returns a {@link CompletableFuture} with a {@link NetMessage} result. Be careful using this
	 * as it will prevent the message from propagating to any thread that could be waiting
	 * for a {@link NetMessage}.
	 * @return A {@link CompletableFuture} with the {@link NetMessage}
	 * received from the partner.
	 */
	public CompletableFuture<NetMessage> getFutureMessage(final int msgType) {
		return CompletableFuture.supplyAsync(() -> {
			try {
				NetMessage msg = messageQueue.take();
				while (msg.getMsgType() != msgType) {
					messageQueue.put(msg);
					msg = messageQueue.take();
					Thread.yield();
				}
				return msg;
			} catch (InterruptedException e) {
				throw new CompletionException(e);
			}
		}, executorService);
	}

	/**
	 * Tell partner that we are ready to play.
	 * @throws IOException if an I/O error occurs when trying to send
	 * the message.
	 */
	public void ready() throws IOException {
		LOGGER.fine("Sending ready message to partner...");
		out.writeInt(NetMessage.MSG_READY);
		out.writeInt(0);
		LOGGER.fine("Sent ready message to partner");
	}

	/** Tell partner that I have quit the game.
	 * @throws IOException if an I/O error occurs when trying to send
	 * the message.
	 */
	public void quit() throws IOException {
		LOGGER.fine("Sending quit message to partner...");
		out.writeInt(NetMessage.MSG_QUIT);
		out.writeInt(0);
		LOGGER.fine("Sent quit message to partner");
	}

	public void fireAt(int index) throws IOException {
		LOGGER.fine("Sending fire message to partner...");
		out.writeInt(NetMessage.MSG_FIRE);
		out.writeInt(4);
		out.writeInt(index);
		LOGGER.fine("Sent fire message to partner");
	}

	public void respondToFire(int targetIndex, int hitStatus) throws IOException {
		LOGGER.fine("Sending fire response to partner...");
		out.writeInt(NetMessage.MSG_FIRE_RESULT);
		out.writeInt(8);
		out.writeInt(targetIndex);
		out.writeInt(hitStatus);
		LOGGER.fine("Sent fire response to partner");
	}

	@Override
	public void close() throws IOException {
		LOGGER.info("Closing partner...");
		receiveMessageThread.interrupt();
		executorService.shutdownNow();
		socket.close();
		LOGGER.info("Partner closed");
	}

//	@Override
//	protected void finalize() throws Throwable {
//		LOGGER.finer("PartnerConnection has been gc'd");
//		if (!socket.isClosed()) {
//			LOGGER.warning("Socket was never closed. Closing now");
//			socket.close();
//		}
//		super.finalize();
//	}
}
