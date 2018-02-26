package me.jcarrete.battleship.server;

import me.jcarrete.battleship.common.logging.ConsoleFormatter;
import me.jcarrete.battleship.common.logging.LogFileFormatter;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.*;

public class BattleshipServer {

	public static final int PORT = 10000;
	public static final Logger logger = Logger.getLogger(BattleshipServer.class.getPackage().getName());

	public static void main(String[] args) {
		setupLogger();

		try {
			final ServerSocket serverSocket = new ServerSocket(PORT);
			Runtime.getRuntime().addShutdownHook(new Thread(() -> {
				try {
					if (!serverSocket.isClosed()) {
						serverSocket.close();
					}
				} catch (IOException ex) {
					logger.log(Level.WARNING, "Failed to close ServerSocket during shutdown", ex);
				}
			}));
			serverSocket.setPerformancePreferences(1, 0, 0);
			serverSocket.setSoTimeout(0);
			awaitConnections(serverSocket);
		} catch (IOException ex) {
			logger.log(Level.SEVERE, "Failed to open ServerSocket on port " + PORT, ex);
			System.exit(1);
		} catch (SecurityException ex) {
			logger.log(Level.SEVERE, "Security issue", ex);
			System.exit(1);
		}
	}

	private static void awaitConnections(final ServerSocket serverSocket) {
		logger.info("Waiting for incoming connections");
		logger.fine("ServerServer connection details: " + serverSocket.toString());
		while (true) {
			try {
				Socket socket = serverSocket.accept();
				logger.info("Received connection from " + socket.toString());
				if (!socket.isClosed()) {
					socket.close();
				}
			} catch (IOException ex) {
				logger.log(Level.WARNING, "Failed to accept a connection", ex);
			} catch (SecurityException ex) {
				logger.log(Level.SEVERE, "Security issue", ex);
			}
		}
	}

	private static void setupLogger() {
		final int MB = 1024 * 1024;
		logger.setUseParentHandlers(false);
		logger.setLevel(Level.ALL);

		// Setup ConsoleHandler
		ConsoleHandler consoleHandler = new ConsoleHandler();
		consoleHandler.setFormatter(new ConsoleFormatter());
		consoleHandler.setLevel(Level.ALL);
		consoleHandler.setErrorManager(new ErrorManager());
		logger.addHandler(consoleHandler);

		// Setup FileHandlers
		try {
			new File("logs/").mkdirs();
			FileHandler fileHandler = new FileHandler("logs/debug-%g.log", 50 * MB, 1);
			fileHandler.setFormatter(new LogFileFormatter());
			fileHandler.setLevel(Level.ALL);
			fileHandler.setErrorManager(new ErrorManager());
			logger.addHandler(fileHandler);
		} catch (IOException e) {
			logger.log(Level.CONFIG, "Unable to setup FileHandler for logging", e);
		}

		try {
			new File("logs/").mkdirs();
			FileHandler fileHandler = new FileHandler("logs/server-%g.log", 50 * MB, 1);
			fileHandler.setFormatter(new LogFileFormatter());
			fileHandler.setLevel(Level.CONFIG);
			fileHandler.setErrorManager(new ErrorManager());
			logger.addHandler(fileHandler);
		} catch (IOException e) {
			logger.log(Level.CONFIG, "Unable to setup FileHandler for logging", e);
		}
	}
}
