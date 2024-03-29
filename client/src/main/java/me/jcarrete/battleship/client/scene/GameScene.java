package me.jcarrete.battleship.client.scene;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import me.jcarrete.battleship.client.net.PartnerConnection;
import me.jcarrete.battleship.client.scene.controller.GameSceneController;

import java.io.IOException;
import java.util.logging.Level;

import static me.jcarrete.battleship.client.BattleshipClient.LOGGER;

public class GameScene extends Scene {

	private static FXMLLoader fxmlLoader;
	private static Parent gameSceneRoot;

	static {
		try {
			fxmlLoader = new FXMLLoader(ClassLoader.getSystemResource("fxml/game_scene.fxml"));
			gameSceneRoot = fxmlLoader.load();
		} catch (IOException e) {
			LOGGER.severe("Unable to load fxml/game_scene.fxml");
			throw new RuntimeException(e);
		}
	}

	private Stage stage;

	public GameScene(Stage stage) {
		super(gameSceneRoot);
		this.stage = stage;
	}

	public void setup(boolean isHost, PartnerConnection partner) {
		fxmlLoader.<GameSceneController>getController().setup(stage, partner, isHost);
	}
}
