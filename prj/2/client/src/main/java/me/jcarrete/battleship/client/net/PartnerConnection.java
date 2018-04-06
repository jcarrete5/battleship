package me.jcarrete.battleship.client.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.logging.Level;

import static me.jcarrete.battleship.client.BattleshipClient.LOGGER;

/**
 * Acts as a way to send data to another player over a network.
 */
public class PartnerConnection implements AutoCloseable {

	private Socket socket;
	private DataInputStream in;
	private DataOutputStream out;
	private Thread messageDispatchThread;
	private HashMap<Integer, LinkedList<Consumer<NetMessage>>> hooks;

	PartnerConnection(Socket s) throws IOException {
		socket = s;
		in = new DataInputStream(s.getInputStream());
		out = new DataOutputStream(s.getOutputStream());
		hooks = new HashMap<>();

		messageDispatchThread = new Thread(() -> {
			while (!Thread.interrupted()) {
				try {
					final int msgType = in.readInt();
					final int bodyLength = in.readInt();
					final byte[] body = new byte[bodyLength];

					int bytesRead = 0;
					while (bytesRead < bodyLength) {
						bytesRead += in.read(body, bytesRead, bodyLength - bytesRead);
					}

					NetMessage msg = new NetMessage(msgType, bodyLength, body);

					// Give the NetMessage to every hook listening to that message
					hooks.forEach((type, callbacks) -> {
						if (type != msgType) return;

					});
				} catch (SocketException e) {
					// Called when socket is closed in the middle of a read
					LOGGER.log(Level.INFO, "Socket is closed while reading data", e);
					Thread.currentThread().interrupt();
				} catch (IOException e) {
					LOGGER.log(Level.WARNING, "Failed to receive a net message", e);
				}
			}
		}, "Message Dispatch Thread");
	}

	public String remoteAddressAndPortAsString() {
		return socket.getInetAddress() + ":" + socket.getPort();
	}

	/**
	 * Attaches a listener to wait for a message from the partner.
	 * @param type the type of message to listen for.
	 * @return a {@link CompletableFuture} with the {@link NetMessage} response
	 * from the partner.
	 */
	public CompletableFuture<NetMessage> receive(int type) {
		final CompletableFuture<NetMessage> future = new CompletableFuture<>();
		LinkedList<Consumer<NetMessage>> callbacks = hooks.getOrDefault(type, new LinkedList<>());
		callbacks.add(future::complete);
		hooks.putIfAbsent(type, callbacks);
		return future;
	}

	/**
	 * Tell foe that we are ready to play.
	 */
	public void ready() throws IOException {
		LOGGER.fine("Sending ready message");
		out.writeInt(NetMessage.MSG_READY);
		out.writeInt(0);
	}

	@Override
	public void close() throws IOException {
		socket.close();
		messageDispatchThread.interrupt();
	}
}
