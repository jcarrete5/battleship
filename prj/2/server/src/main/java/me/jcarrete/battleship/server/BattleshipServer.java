package me.jcarrete.battleship.server;

import me.jcarrete.battleship.common.logging.ConsoleFormatter;
import me.jcarrete.battleship.common.logging.LogFileFormatter;

import java.io.File;
import java.io.IOException;
import java.util.logging.*;

public class BattleshipServer {

	public static final Logger logger = Logger.getLogger(BattleshipServer.class.getPackage().getName());

	public static void main(String[] args) {
		setupLogger();
		logger.info("Test");
		try {
			throw new Exception("Error");
		} catch (Exception e) {
			logger.log(Level.FINE, "Error test", e);
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
