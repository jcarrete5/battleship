package me.jcarrete.battleship.client.scene;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import me.jcarrete.battleship.client.net.PartnerConnection;

import java.io.IOException;

import static me.jcarrete.battleship.client.BattleshipClient.LOGGER;

public class GameScene extends Scene {

	private static Parent gameSceneRoot;

	static {
		try {
			gameSceneRoot = FXMLLoader.load(ClassLoader.getSystemResource("fxml/game_scene.fxml"));
		} catch (IOException e) {
			LOGGER.severe("Unable to load fxml/game_scene.fxml");
			throw new RuntimeException(e);
		}
	}

	private Stage stage;

	public GameScene(Stage stage, boolean isHost, PartnerConnection partner) {
		super(gameSceneRoot);
		this.stage = stage;
		stage.centerOnScreen();
	}

	public static class GameSceneController {

		@FXML
		private Label turnIndicator;

		@FXML
		public void initialize() {
			turnIndicator.setText("Waiting for opponent");
		}
	}
}
