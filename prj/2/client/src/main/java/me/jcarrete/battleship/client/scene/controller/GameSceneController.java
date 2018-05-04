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
import java.nio.ByteBuffer;
import java.util.logging.Level;

import static me.jcarrete.battleship.client.BattleshipClient.LOGGER;
import static me.jcarrete.battleship.client.scene.BattleshipGrid.*;

public class GameSceneController {

	private PartnerConnection partner;
	private boolean hasTurn;
	private Stage stage;

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
		this.stage = stage;

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

		// Handle partner losing the game
		partner.getFutureMessage(NetMessage.MSG_LOSE).thenAccept(msg -> {
			onWin();
		});

		turnIndicator.setText("Waiting for opponent");
		gameSceneLayout.setLeft(leftNode);
		randomButton.setDisable(false);
		grid.clear();
		grid.draw();
	}

	private void takeTurn() {
		Platform.runLater(() -> turnIndicator.setText("Make your move!"));
		partner.getFutureMessage(NetMessage.MSG_FIRE_RESULT).thenAccept(netmsg -> {
			// Must save body as variable because getBody() returns different
			// read only byte buffer each time
			ByteBuffer body = netmsg.getBody();
			int targetIndex = body.getInt();
			int hitStatus = body.getInt();

			LOGGER.info(String.format("Got MSG_FIRE_RESULT. targetIndex: %d, hitStatus: %d", targetIndex, hitStatus));

			Platform.runLater(() -> {
				// Update my Battleship grid with the result of the firing
				grid.setHitColor(targetIndex, hitStatus);
				grid.draw();

				String msg = "";
				if (hitStatus == MISS) {
					msg = "Miss!";
				} else if (hitStatus == HIT) {
					msg = "Hit!";
				} else if (hitStatus == SUNK) {
					msg = "You sunk your foe's battleship!";
				}
				new Alert(Alert.AlertType.INFORMATION, msg).showAndWait();
			});

			hasTurn = false;
			waitForTurn();
		});
	}

	private void waitForTurn() {
		Platform.runLater(() -> turnIndicator.setText("Waiting..."));
		partner.getFutureMessage(NetMessage.MSG_FIRE).thenAccept(netmsg -> {
			int index = netmsg.getBody().getInt();

			// Check if a ship has been hit
			int r = index / BattleshipGrid.COLS, c = index % BattleshipGrid.COLS;
			// hitStatus can be MISS = 0, HIT = 1, or SUNK = 2
			int hitStatus = grid.getHitStatus(r, c);

			if (grid.checkLose()) {
				onLose();
			} else {
				try {
					// Convert index back to refer to partners top of grid for marking
					int newIndex = (r - 10) * BattleshipGrid.COLS + c;
					partner.respondToFire(newIndex, hitStatus);
					hasTurn = true;
					takeTurn();
				} catch (IOException e) {
					final String msg = "Failed to send fire response to partner";
					LOGGER.log(Level.WARNING, msg, e);
					Platform.runLater(() -> new Alert(Alert.AlertType.WARNING, msg).showAndWait());
				}
			}
		});
	}

	private void onWin() {
		Platform.runLater(() -> {
			new Alert(Alert.AlertType.INFORMATION, "You Won!").showAndWait();
			BattleshipClient.switchToMainMenuScene(stage);
			try {
				partner.close();
			} catch (IOException e) {
				LOGGER.log(Level.WARNING, "Failed to close connection to partner", e);
			}
		});
	}

	private void onLose() {
		try {
			partner.sendLose();
			Platform.runLater(() -> {
				new Alert(Alert.AlertType.INFORMATION, "You Lost!").showAndWait();
				BattleshipClient.switchToMainMenuScene(stage);
			});
		} catch (IOException e) {
			LOGGER.log(Level.WARNING, "Failed to send lose message to partner", e);
			Platform.runLater(() -> BattleshipClient.switchToMainMenuScene(stage));
		} finally {
			try {
				partner.close();
			} catch (IOException e) {
				LOGGER.log(Level.WARNING, "Failed to close connection to partner", e);
			}
		}
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
				Platform.runLater(() -> {
					// Remove side panel with ships and redraw grid
					gameSceneLayout.setLeft(null);
					grid.draw();

					//TODO Might have to avoid making these calls recursive
					if (hasTurn) {
						takeTurn();
					} else {
						waitForTurn();
					}
				});
			}).exceptionally(e -> {
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
		event.consume();
		// Sanitize target position so it targets the correct spot for the partner
		int targetIndex = grid.getTargetPos() + BattleshipGrid.ROWS / 2 * BattleshipGrid.COLS;

		if (grid.getTargetPos() < 0) {
			new Alert(Alert.AlertType.INFORMATION, "No target selected").showAndWait();
			return;
		}

		if (!hasTurn) {
			grid.resetTargetPosition();
			grid.draw();
			new Alert(Alert.AlertType.INFORMATION, "It is not your turn").showAndWait();
			return;
		}

		try {
			partner.fireAt(targetIndex);

			// Mark the target as untargetable now so the user doesn't fire at the same spot
			grid.disableTarget();
			grid.resetTargetPosition();
			grid.draw();
		} catch (IOException e) {
			LOGGER.log(Level.WARNING, "Failed to send fire message", e);
		}
	}
}
