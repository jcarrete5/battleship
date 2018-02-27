package me.jcarrete.battleship.client;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import me.jcarrete.battleship.common.logging.ConsoleFormatter;
import me.jcarrete.battleship.common.logging.LogFileFormatter;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.*;

public class BattleshipClient extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	public static final Logger LOGGER = Logger.getLogger(BattleshipClient.class.getPackage().getName());

	@Override
	public void init() throws Exception {
		final int MB = 1024 * 1024;
		LOGGER.setUseParentHandlers(false);
		LOGGER.setLevel(Level.ALL);

		ConsoleHandler consoleHandler = new ConsoleHandler();
		consoleHandler.setLevel(Level.ALL);
		consoleHandler.setFormatter(new ConsoleFormatter());
		consoleHandler.setErrorManager(new ErrorManager());
		LOGGER.addHandler(consoleHandler);

		try {
			new File("logs/").mkdirs();
			FileHandler fileHandler = new FileHandler("logs/client-%g.log", 50 * MB, 1);
			fileHandler.setFormatter(new LogFileFormatter());
			fileHandler.setLevel(Level.ALL);
			fileHandler.setErrorManager(new ErrorManager());
			LOGGER.addHandler(fileHandler);
		} catch (IOException e) {
			LOGGER.log(Level.CONFIG, "Unable to setup FileHandler for 'logs/client-%g.log'", e);
		}
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(ClassLoader.getSystemResource("fxml/main_menu.fxml"));

		primaryStage.setScene(new Scene(root, 800, 600));
		primaryStage.setTitle(BuildVersion.getImplTitle() + " v" + BuildVersion.getImplVersion());
		primaryStage.show();
		primaryStage.centerOnScreen();
	}

	@FXML
	private void onMultiPress(ActionEvent event) {
		try {
			Socket socket = new Socket(InetAddress.getLocalHost(), 10000);
			socket.close();
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Failed to establish a connection to server", e);
		}
	}
}
