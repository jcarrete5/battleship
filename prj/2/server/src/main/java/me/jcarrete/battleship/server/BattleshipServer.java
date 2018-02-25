package me.jcarrete.battleship.server;

import me.jcarrete.battleship.server.logging.ConsoleFormatter;
import me.jcarrete.battleship.server.logging.LogFileFormatter;

import java.io.File;
import java.io.IOException;
import java.util.logging.*;

public class BattleshipServer {

	public static final Logger logger;

	static {
		final int MB = 1048576;
		logger = Logger.getLogger(BattleshipServer.class.getPackage().getName());
		logger.setUseParentHandlers(false);
		logger.setLevel(Level.ALL);

		// Setup ConsoleHandler
		ConsoleHandler consoleHandler = new ConsoleHandler();
		consoleHandler.setFormatter(new ConsoleFormatter());
		consoleHandler.setLevel(Level.ALL);
		consoleHandler.setErrorManager(new ErrorManager());
		logger.addHandler(consoleHandler);

		// Setup FileHandler
		try {
			new File("logs/").mkdirs();
			FileHandler fileHandler = new FileHandler("logs/server-%g.log", 50 * MB, 3);
			fileHandler.setFormatter(new LogFileFormatter());
			fileHandler.setLevel(Level.ALL);
			fileHandler.setErrorManager(new ErrorManager());
			logger.addHandler(fileHandler);
		} catch (IOException e) {
			logger.log(Level.FINE, "Unable to setup FileHandler for logging", e);
		}
	}

	public static void main(String[] args) {

	}
}
