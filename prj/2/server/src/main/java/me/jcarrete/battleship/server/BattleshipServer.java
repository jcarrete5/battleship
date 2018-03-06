package me.jcarrete.battleship.server;

import me.jcarrete.battleship.common.logging.ConsoleFormatter;
import me.jcarrete.battleship.common.logging.LogFileFormatter;
import me.jcarrete.battleship.common.net.Message;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.*;

public class BattleshipServer {

	public static final int PORT = 10000;
	public static final Logger LOGGER = Logger.getLogger(BattleshipServer.class.getPackage().getName());

	public static void main(String[] args) {
		setupLogger();
		Thread.setDefaultUncaughtExceptionHandler((thread, ex) ->
				LOGGER.log(Level.SEVERE, "Uncaught exception on " + thread.getName() + " thread", ex));

		try {
			final ServerSocket serverSocket = new ServerSocket(PORT);
			Runtime.getRuntime().addShutdownHook(new Thread(() -> {
				try {
					if (!serverSocket.isClosed()) {
						serverSocket.close();
					}
				} catch (IOException ex) {
					LOGGER.log(Level.WARNING, "Failed to close ServerSocket during shutdown", ex);
				}
			}));
			serverSocket.setPerformancePreferences(1, 0, 0);
			serverSocket.setSoTimeout(0);
			LOGGER.fine("serverSocket connection details: " + serverSocket);

			final BlockingQueue<Socket> connections = new LinkedBlockingQueue<>();
			Runtime.getRuntime().addShutdownHook(new Thread(() -> {
				for (Socket s : connections) {
					try {
						if (!s.isClosed()) {
							s.close();
						}
					} catch (IOException ex) {
						LOGGER.log(Level.WARNING, "Failed to close a socket (" + s + ") during shutdown", ex);
					}
				}
			}));
			Thread connectionThread = new Thread(awaitConnections(serverSocket, connections), "Await Connections " +
					"Thread");
			Thread matchmakingThread = new Thread(matchmake(connections), "Matchmaking Thread");
			connectionThread.start();
			matchmakingThread.start();
		} catch (IOException ex) {
			LOGGER.log(Level.SEVERE, "Failed to open ServerSocket on port " + PORT, ex);
			System.exit(1);
		}
	}

	private static Runnable awaitConnections(final ServerSocket serverSocket, final BlockingQueue<Socket> connections) {
		return () -> {
			LOGGER.info("Waiting for incoming connections");

			while (!Thread.interrupted()) {
				try {
					Socket socket = serverSocket.accept();
					LOGGER.info("Received connection from " + socket.getInetAddress() + ":" + socket.getPort());
					LOGGER.fine("New Socket: " + socket);
					connections.add(socket);
				} catch (SocketException ex) {
					LOGGER.log(Level.FINE, "serverSocket closed during serverSocket.accept()", ex);
				} catch (IOException ex) {
					LOGGER.log(Level.WARNING, "Failed to accept a connection", ex);
				}
			}
		};
	}

	private static Runnable matchmake(final BlockingQueue<Socket> connections) {
		return () -> {
			while (!Thread.interrupted()) {
				Socket s1, s2;

				// Wait for two connections
				try {
					s1 = connections.take();
					LOGGER.fine(socketAsString(s1) + " was taken from the connections queue");
				} catch (InterruptedException ex) {
					LOGGER.log(Level.WARNING, "Interrupted while waiting for first player", ex);
					Thread.currentThread().interrupt();
					throw new RuntimeException(ex);
				}

				try {
					s2 = connections.take();
					LOGGER.fine(socketAsString(s2) + " was taken from the connections queue");
				} catch (InterruptedException ex) {
					LOGGER.log(Level.WARNING, "Interrupted while waiting for a second player", ex);
					Thread.currentThread().interrupt();
					throw new RuntimeException(ex);
				}

				// Pick one to be the host
				Socket host, other;
				if (Math.random() < 0.5) {
					host = s1;
					other = s2;
				} else {
					host = s2;
					other = s1;
				}

				// Send data to each player to determine who to connect to
				try (DataOutputStream hostOut = new DataOutputStream(host.getOutputStream());
				     DataInputStream hostIn = new DataInputStream(host.getInputStream());
				     DataOutputStream otherOut = new DataOutputStream(other.getOutputStream())) {

					hostOut.writeBoolean(true);
					otherOut.writeBoolean(false);

					String hostAsString = socketAsString(host);
					LOGGER.fine(hostAsString + " is a host");
					LOGGER.fine("Waiting for " + hostAsString + " to setup p2p server socket");

					//FEATURE move this wait to another thread so the matchmaking thread can keep running
					// Wait for host to be ready to receive connections
					while (Message.valueOf(hostIn.readUTF()) != Message.HOST_READY) {
						if (Thread.interrupted()) {
							LOGGER.warning("Interrupted while waiting for " + Message.HOST_READY.name());
							Thread.currentThread().interrupt();
							throw new RuntimeException("Thread interrupted in the middle of IO");
						}
					}

					LOGGER.fine(hostAsString + " is ready to receive connections");

					// Tell other player where to connect to
					byte[] rawIp = host.getInetAddress().getAddress();
					otherOut.writeInt(rawIp.length);
					otherOut.write(rawIp);
					otherOut.writeInt(host.getPort());
				} catch (IOException ex) {
					LOGGER.log(Level.WARNING, "Failed to match " + socketAsString(s1) + " and " + socketAsString(s2),
							ex);
					continue;
				}

				LOGGER.info("Connection made between " + socketAsString(s1) + " and " + socketAsString(s2));
			}
		};
	}

	private static String socketAsString(Socket s) {
		return s.getInetAddress() + ":" + s.getPort();
	}

	private static void setupLogger() {
		final int MB = 1024 * 1024;
		LOGGER.setUseParentHandlers(false);
		LOGGER.setLevel(Level.ALL);

		// Setup ConsoleHandler
		ConsoleHandler consoleHandler = new ConsoleHandler();
		consoleHandler.setFormatter(new ConsoleFormatter());
		consoleHandler.setLevel(Level.ALL);
		consoleHandler.setErrorManager(new ErrorManager());
		LOGGER.addHandler(consoleHandler);

		// Setup FileHandler
		try {
			new File("logs/").mkdirs();
			FileHandler fileHandler = new FileHandler("logs/server-%g.log", 50 * MB, 1);
			fileHandler.setFormatter(new LogFileFormatter());
			fileHandler.setLevel(Level.ALL);
			fileHandler.setErrorManager(new ErrorManager());
			LOGGER.addHandler(fileHandler);
		} catch (IOException e) {
			LOGGER.log(Level.CONFIG, "Unable to setup FileHandler for 'logs/server-%g.log'", e);
		}
	}
}
