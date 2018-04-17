package me.jcarrete.battleship.client.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static me.jcarrete.battleship.client.BattleshipClient.LOGGER;

/**
 * Acts as a way to send data to another player over a network.
 */
public class PartnerConnection implements AutoCloseable {

	private Socket socket;
	private DataInputStream in;
	private DataOutputStream out;
	private ExecutorService executor;

	PartnerConnection(Socket s) throws IOException {
		socket = s;
		in = new DataInputStream(s.getInputStream());
		out = new DataOutputStream(s.getOutputStream());
		executor = Executors.newSingleThreadExecutor();
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
	private CompletableFuture<NetMessage> getFutureMessage() {
//		CompletableFuture<NetMessage> future = new CompletableFuture<>();
//		executor.execute(() -> {
//			try {
//				future.complete(receive());
//			} catch (IOException e) {
//				future.completeExceptionally(e);
//			}
//		});
//		return future;
		return CompletableFuture.supplyAsync(() -> {
			try {
				return receive();
			} catch (IOException e) {
				throw new CompletionException(e);
			}
		}, executor);
	}

	/**
	 * Tell foe that we are ready to play.
	 * @return A {@link CompletableFuture<NetMessage>} with the response from the
	 * partner after sending a ready message.
	 */
	public CompletableFuture<NetMessage> ready() throws IOException {
		LOGGER.fine("Sending ready message to partner...");
		out.writeInt(NetMessage.MSG_READY);
		out.writeInt(0);
		LOGGER.fine("Sent ready message to partner");

		return getFutureMessage();
	}

	@Override
	public void close() throws IOException {
		LOGGER.info("Closing partner...");
		executor.shutdownNow();
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
