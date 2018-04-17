package me.jcarrete.battleship.client.scene;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import static me.jcarrete.battleship.client.BattleshipClient.LOGGER;

public class MainMenuScene extends Scene {

	private static FXMLLoader fxmlLoader;
	private static Parent mainMenuSceneRoot;

	static {
		try {
			fxmlLoader = new FXMLLoader(ClassLoader.getSystemResource("fxml/main_menu.fxml"));
			mainMenuSceneRoot = fxmlLoader.load();
		} catch (IOException e) {
			LOGGER.severe("Unable to load fxml/main_menu.fxml");
			throw new RuntimeException(e);
		}
	}

	private Stage stage;

	public MainMenuScene(Stage stage) {
		super(mainMenuSceneRoot, 400, 300);
		this.stage = stage;
	}
}
