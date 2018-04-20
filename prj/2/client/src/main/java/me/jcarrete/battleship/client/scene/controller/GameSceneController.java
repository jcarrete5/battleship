package me.jcarrete.battleship.client.scene.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import me.jcarrete.battleship.client.BattleshipClient;
import me.jcarrete.battleship.client.net.NetMessage;
import me.jcarrete.battleship.client.net.PartnerConnection;
import me.jcarrete.battleship.client.scene.BattleshipGrid;
import me.jcarrete.battleship.client.scene.Ship;

import java.io.IOException;
import java.util.logging.Level;

import static me.jcarrete.battleship.client.BattleshipClient.LOGGER;

public class GameSceneController {

	private PartnerConnection partner;
	private boolean hasTurn;

	@FXML private BorderPane gameSceneLayout;
	@FXML private Button randomButton;
	@FXML private Label turnIndicator;
	@FXML private BattleshipGrid grid;
	@FXML private ImageView carrierView, battleshipView, cruiserView, submarineView, destroyerView;

	private Node leftNode;

	@FXML
	private void initialize() {
		leftNode = gameSceneLayout.getLeft();
	}

	/**
	 * Call after initialize. Can be called multiple times
	 * @param stage stage
	 * @param partner partner
	 * @param hasTurn <tt>true</tt> if I start the game, otherwise <tt>false</tt>.
	 */
	public void setup(Stage stage, PartnerConnection partner, boolean hasTurn) {
		this.partner = partner;
		this.hasTurn = hasTurn;

		// Handle partner quitting game
		partner.getFutureMessage(NetMessage.MSG_QUIT).thenAccept(msg -> {
			final String errMsg = "Partner left the game";
			LOGGER.info(errMsg);
			Platform.runLater(() -> {
				BattleshipClient.switchToMainMenuScene(stage);
				new Alert(Alert.AlertType.INFORMATION, errMsg).showAndWait();
			});
			try {
				partner.close();
			} catch (IOException e) {
				LOGGER.log(Level.WARNING, "Failed to close partner socket", e);
			}
		});

		turnIndicator.setText("Waiting for opponent");
		gameSceneLayout.setLeft(leftNode);
		randomButton.setDisable(false);
		grid.clear();
		grid.draw();
	}

	@FXML
	private void onRandomPress(ActionEvent event) {
		LOGGER.finest("onRandomPress called");
		event.consume();
		grid.clear();

		Ship ship;

		// Randomly place carrier
		do {
			int rRow = (int)(Math.random() * (BattleshipGrid.ROWS / 2 - 4) + 10);
			int rCol = (int)(Math.random() * (BattleshipGrid.COLS - 4));
			ship = new Ship(carrierView.getImage(), rRow, rCol);
		} while (!grid.addShip(ship));
		LOGGER.fine("Placed Carrier");

		// Randomly place battleship
		do {
			int rRow = (int)(Math.random() * (BattleshipGrid.ROWS / 2 - 3) + 10);
			int rCol = (int)(Math.random() * (BattleshipGrid.COLS - 3));
			ship = new Ship(battleshipView.getImage(), rRow, rCol);
		} while (!grid.addShip(ship));
		LOGGER.fine("Placed Battleship");

		// Randomly place cruiser
		do {
			int rRow = (int) (Math.random() * (BattleshipGrid.ROWS / 2 - 2) + 10);
			int rCol = (int) (Math.random() * (BattleshipGrid.COLS - 2));
			ship = new Ship(cruiserView.getImage(), rRow, rCol);
		} while (!grid.addShip(ship));
		LOGGER.fine("Placed Cruiser");

		// Randomly place submarine
		do {
			int rRow = (int) (Math.random() * (BattleshipGrid.ROWS / 2 - 2) + 10);
			int rCol = (int) (Math.random() * (BattleshipGrid.COLS - 2));
			ship = new Ship(submarineView.getImage(), rRow, rCol);
		} while (!grid.addShip(ship));
		LOGGER.fine("Placed Submarine");

		// Randomly place destroyer
		do {
			int rRow = (int) (Math.random() * (BattleshipGrid.ROWS / 2 - 1) + 10);
			int rCol = (int) (Math.random() * (BattleshipGrid.COLS - 1));
			ship = new Ship(destroyerView.getImage(), rRow, rCol);
		} while (!grid.addShip(ship));
		LOGGER.fine("Placed Destroyer");


		try {
			// Tell partner that I am ready to start
			partner.ready();
			partner.getFutureMessage(NetMessage.MSG_READY).thenAccept(msg -> {
				LOGGER.finer("I should start the game");
			}).thenRun(() -> Platform.runLater(() -> {
				// Remove side panel with ships and redraw grid
				gameSceneLayout.setLeft(null);
				grid.draw();
			})).exceptionally(e -> {
				if (!(e.getCause() instanceof InterruptedException)) {
					final String msg = "Error when waiting for partner to ready up";
					LOGGER.log(Level.WARNING, msg, e);
					Platform.runLater(() -> new Alert(Alert.AlertType.WARNING, msg).showAndWait());
					randomButton.setDisable(false);
				} else {
					LOGGER.info("Interrupted while waiting for partner to ready up");
					randomButton.setDisable(false);
				}
				return null;
			});

			randomButton.setDisable(true);
		} catch (IOException e) {
			final String msg = "Failed to send ready message";
			LOGGER.log(Level.WARNING, msg, e);
			new Alert(Alert.AlertType.WARNING, msg).showAndWait();
		}

		grid.draw();
	}

	@FXML
	private void onFirePress(ActionEvent event) {
		LOGGER.fine("onFirePress() called");

	}
}
