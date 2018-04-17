package me.jcarrete.battleship.client.scene.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import me.jcarrete.battleship.client.BattleshipClient;
import me.jcarrete.battleship.client.net.ServerConnection;
import me.jcarrete.battleship.client.scene.GameScene;

import java.io.IOException;
import java.net.InetAddress;
import java.util.logging.Level;

import static me.jcarrete.battleship.client.BattleshipClient.LOGGER;

public class MainMenuSceneController {

	@FXML
	private void initialize() {

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
					BattleshipClient.setPartner(partner);
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
}
