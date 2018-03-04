package me.jcarrete.battleship.client.net;

import me.jcarrete.battleship.client.BattleshipClient;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;

/**
 * Represents a connection to a game server.
 */
public class ServerConnection extends Socket {

	public static ServerConnection connectToGameServer(String serverHostname, int serverPort)
			throws IOException {
		return connectToGameServer(InetAddress.getByName(serverHostname), serverPort);
	}

	public static ServerConnection connectToGameServer(InetAddress serverAddress, int serverPort)
			throws IOException {
		return new ServerConnection(serverAddress, serverPort);
	}

	private final DataInputStream in;
	private final ExecutorService executor;

	private ServerConnection(InetAddress serverAddress, int serverPort) throws IOException {
		super(serverAddress, serverPort);
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			try {
				if (!this.isClosed()) {
					this.close();
				}
			} catch (IOException ex) {
				BattleshipClient.LOGGER.log(Level.WARNING, "Failed to close a ServerConnection for " + this, ex);
			}
		}));
		setKeepAlive(true);
		setPerformancePreferences(1, 0, 0);
		setSoTimeout(0);
		in = new DataInputStream(getInputStream());

		executor = Executors.newSingleThreadExecutor();
	}

	/**
	 * Receives a message from the server with the socket used to communicate
	 * with the matched partner.
	 * @return A {@link CompletableFuture} with a {@link PartnerConnection} if
	 * we successfully found a partner.
	 */
	public CompletableFuture<PartnerConnection> findPartner() {
		final CompletableFuture<PartnerConnection> future = new CompletableFuture<>();

		new Thread(() -> {
			try {
				byte[] rawIp = new byte[in.readInt()];
				int totalBytesRead = 0;
				do {
					int bytesRead = in.read(rawIp, totalBytesRead, rawIp.length);
					if (bytesRead == -1) throw new IOException("End of stream reached before entire ip was read");
					totalBytesRead += bytesRead;
				} while (totalBytesRead < rawIp.length);

				InetAddress targetAddress = InetAddress.getByAddress(rawIp);
				int targetPort = in.readInt();
				future.complete(new PartnerConnection(targetAddress, targetPort));
			} catch (IOException ex) {
				future.completeExceptionally(ex);
			}
		}, "PartnerConnection Search Thread").start();

		return future;
	}

	/**
	 * Receives a message from the server with information regarding which player will go first.
	 * @return A {@link CompletableFuture} with the result of the server deciding which
	 * player will start the game. (and also determines who is the host)
	 */
	public CompletableFuture<Boolean> getHasTurn() {
		final CompletableFuture<Boolean> future = new CompletableFuture<>();

		new Thread(() -> {
			try {
				future.complete(in.readBoolean());
			} catch (IOException ex) {
				future.completeExceptionally(ex);
			}
		}, "Get hasTurn Thread").start();

		return future;
	}

	@Override
	public synchronized void close() throws IOException {
		super.close();
		executor.shutdown();
	}
}
