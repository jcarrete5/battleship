package me.jcarrete.battleship.client.scene;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
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

		@FXML private Label turnIndicator;
		@FXML private BattleshipGrid grid;
		@FXML private ImageView carrierView, battleshipView, cruiserView, submarineView, destroyerView;

		@FXML
		private void initialize() {
			turnIndicator.setText("Waiting for opponent");

			grid.draw();

			//TODO setup some network event listeners to update screen when network events occur
		}

		@FXML
		private void onRandomPress(ActionEvent event) {
			LOGGER.fine("onRandomPress called");
			event.consume();
			grid.clear();

			// Inefficient but simple to understand
			while (!grid.isValid()) {
				grid.clear();
				// Randomly place carrier
				int rRow = (int)(Math.random() * (BattleshipGrid.ROWS / 2 - 4) + 10);
				int rCol = (int)(Math.random() * (BattleshipGrid.COLS - 4));
				grid.addShip(new Ship(carrierView.getImage(), rRow, rCol));

				// Randomly place battleship
				rRow = (int)(Math.random() * (BattleshipGrid.ROWS / 2 - 3) + 10);
				rCol = (int)(Math.random() * (BattleshipGrid.COLS - 3));
				grid.addShip(new Ship(battleshipView.getImage(), rRow, rCol));

				// Randomly place cruiser
				rRow = (int)(Math.random() * (BattleshipGrid.ROWS / 2 - 2) + 10);
				rCol = (int)(Math.random() * (BattleshipGrid.COLS - 2));
				grid.addShip(new Ship(cruiserView.getImage(), rRow, rCol));

				// Randomly place battleship
				rRow = (int)(Math.random() * (BattleshipGrid.ROWS / 2 - 2) + 10);
				rCol = (int)(Math.random() * (BattleshipGrid.COLS - 2));
				grid.addShip(new Ship(submarineView.getImage(), rRow, rCol));

				// Randomly place battleship
				rRow = (int)(Math.random() * (BattleshipGrid.ROWS / 2 - 1) + 10);
				rCol = (int)(Math.random() * (BattleshipGrid.COLS - 1));
				grid.addShip(new Ship(destroyerView.getImage(), rRow, rCol));
			}

			grid.draw();
		}
	}
}
