package me.jcarrete.battleship.server;

import me.jcarrete.battleship.common.logging.ConsoleFormatter;
import me.jcarrete.battleship.common.logging.LogFileFormatter;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
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
			awaitConnections(serverSocket);
		} catch (IOException ex) {
			LOGGER.log(Level.SEVERE, "Failed to open ServerSocket on port " + PORT, ex);
			System.exit(1);
		}
	}

	private static void awaitConnections(final ServerSocket serverSocket) {
		LOGGER.info("Waiting for incoming connections");
		while (true) {
			try {
				Socket socket = serverSocket.accept();
				LOGGER.info("Received connection from " + socket.getInetAddress() + ":" + socket.getPort());
				LOGGER.fine("New Socket: " + socket);
				socket.close();
			} catch (SocketException ex) {
				LOGGER.log(Level.FINE, "serverSocket closed during serverSocket.accept()", ex);
			} catch (IOException ex) {
				LOGGER.log(Level.WARNING, "Failed to accept a connection", ex);
			}
		}
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
