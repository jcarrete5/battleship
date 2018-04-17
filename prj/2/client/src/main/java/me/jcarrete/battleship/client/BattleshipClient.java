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
		Parent root = FXMLLoader.load(ClassLoader.getSystemResource("fxml/main_menu.fxml"));

		stage.setScene(new Scene(root, 400, 300));
		stage.setTitle(BuildVersion.getImplTitle() + " v" + BuildVersion.getImplVersion());
		stage.show();
		stage.centerOnScreen();
	}

	@FXML
	private void onSinglePress(ActionEvent event) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION, "Not implemented");
		alert.setTitle("Singleplayer");
		alert.setHeaderText("Unable to load singleplayer");
		alert.showAndWait();
	}

	@FXML
	private void onMultiPress(ActionEvent event) {
		Dialog<Void> loadingDialog = new Dialog<>();
		try (ServerConnection conn = ServerConnection.connectToGameServer(InetAddress.getLocalHost(), 10000)) {
			conn.isHost().thenAccept(isHost ->
				conn.findPartner(isHost).thenAccept(partner -> {
					LOGGER.info("Found a partner with address " + partner.remoteAddressAndPortAsString());
					LOGGER.info("Starting game setup");
					final Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
					BattleshipClient.partner = partner;
					LOGGER.info(Thread.currentThread().getName());
					Platform.runLater(() -> stage.setScene(new GameScene(stage, isHost, partner)));
				}).exceptionally(ex -> {
					String msg = "Failed to find a partner";
					LOGGER.log(Level.WARNING, msg, ex);
					Platform.runLater(() -> new Alert(Alert.AlertType.WARNING, msg).showAndWait());
					return null;
				}).thenRun(() -> Platform.runLater(loadingDialog::close))
			).exceptionally(ex -> {
				String msg = "Failed to determine who starts";
				LOGGER.log(Level.WARNING, msg, ex);
				Platform.runLater(() -> {
					loadingDialog.close();
					new Alert(Alert.AlertType.WARNING, msg).showAndWait();
				});
				return null;
			});

			// Display dialog while waiting for a partner
			loadingDialog.initOwner(((Node)event.getTarget()).getScene().getWindow());
			loadingDialog.initModality(Modality.WINDOW_MODAL);
			loadingDialog.setContentText("Waiting for a player...");
			ProgressIndicator progress = new ProgressIndicator();
			loadingDialog.getDialogPane().setContent(progress);
			loadingDialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);
			//TODO clean up some net code if the search was canceled
			loadingDialog.showAndWait();
			LOGGER.finer("After loadingDialog.showAndWait()");
		} catch (IOException e) {
			String msg = "Failed to establish a connection to server";
			LOGGER.log(Level.WARNING, msg, e);
			new Alert(Alert.AlertType.WARNING, msg).showAndWait();
			loadingDialog.close();
		}
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
