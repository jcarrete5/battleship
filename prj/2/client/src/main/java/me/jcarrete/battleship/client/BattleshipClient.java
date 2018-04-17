package me.jcarrete.battleship.client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import me.jcarrete.battleship.client.net.PartnerConnection;
import me.jcarrete.battleship.client.net.ServerConnection;
import me.jcarrete.battleship.client.scene.GameScene;
import me.jcarrete.battleship.client.scene.MainMenuScene;
import me.jcarrete.battleship.common.logging.ConsoleFormatter;
import me.jcarrete.battleship.common.logging.LogFileFormatter;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.logging.*;

public class BattleshipClient extends Application {

	public static void main(String[] args) {
		launch(args);
		LOGGER.finest("After launch in main()");
	}

	public static final Logger LOGGER = Logger.getLogger(BattleshipClient.class.getPackage().getName());

	private static PartnerConnection partner;

//	public static void onPartnerQuit() {
//		final String msg = "Partner left the game";
//		LOGGER.info(msg);
//		Platform.runLater(() -> new Alert(Alert.AlertType.INFORMATION, msg).showAndWait());
//		try {
//			partner.close();
//		} catch (IOException e) {
//			LOGGER.log(Level.WARNING, "Failed to close partner socket", e);
//		}
//	}

	public static void setPartner(PartnerConnection partner) {
		BattleshipClient.partner = partner;
	}

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

		Thread.setDefaultUncaughtExceptionHandler((thread, ex) ->
				LOGGER.log(Level.SEVERE, "Uncaught exception on " + thread.getName() + " thread", ex));
	}

	@Override
	public void start(Stage stage) throws Exception {
		stage.setScene(new MainMenuScene(stage));
		stage.setTitle(BuildVersion.getImplTitle() + " v" + BuildVersion.getImplVersion());
		stage.show();
		stage.centerOnScreen();
	}

	@Override
	public void stop() throws Exception {
		LOGGER.fine("Stop called");
		LOGGER.finest("partner is stop() = " + partner);
		if (partner != null) {
			LOGGER.finest("Before partner.close()");
			//TODO send a quit message if I leave
			partner.close();
		}
	}
}
