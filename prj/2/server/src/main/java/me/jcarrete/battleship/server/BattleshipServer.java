package me.jcarrete.battleship.server;

import me.jcarrete.battleship.common.logging.ConsoleFormatter;
import me.jcarrete.battleship.common.logging.LogFileFormatter;

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

				try {
					s1 = connections.take();
				} catch (InterruptedException ex) {
					LOGGER.log(Level.WARNING, "Interrupted while waiting for first player", ex);
					Thread.currentThread().interrupt();
					throw new RuntimeException(ex);
				}

				try {
					s2 = connections.take();
				} catch (InterruptedException ex) {
					connections.add(s1); // Add first player back into queue
					LOGGER.log(Level.WARNING, "Interrupted while waiting for a second player", ex);
					Thread.currentThread().interrupt();
					throw new RuntimeException(ex);
				}

				boolean hasTurn = Math.random() < 0.5;

				try (DataOutputStream s1out = new DataOutputStream(s1.getOutputStream())) {
					byte[] rawIp = s2.getInetAddress().getAddress();
					s1out.writeInt(rawIp.length);
					s1out.write(rawIp);
					s1out.writeInt(s2.getPort());
					s1out.writeBoolean(hasTurn);
				} catch (IOException ex) {
					connections.add(s2); // Add second player back into queue
					LOGGER.log(Level.WARNING, "Failed to send all necessary data to first player", ex);
					continue;
				}

				try (DataOutputStream s2out = new DataOutputStream(s2.getOutputStream())) {
					byte[] rawIp = s1.getInetAddress().getAddress();
					s2out.writeInt(rawIp.length);
					s2out.write(rawIp);
					s2out.writeInt(s1.getPort());
					s2out.writeBoolean(!hasTurn);
				} catch (IOException ex) {
					connections.add(s1); // Add first player back into queue
					LOGGER.log(Level.WARNING, "Failed to send all necessary data to second player", ex);
					continue;
				}

				LOGGER.info("Connection made between "
						+ s1.getInetAddress() + ":" + s1.getPort() + " and "
						+ s2.getInetAddress() + ":" + s2.getPort());
			}
		};
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
