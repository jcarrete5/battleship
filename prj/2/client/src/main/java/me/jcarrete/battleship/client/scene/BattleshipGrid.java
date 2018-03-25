package me.jcarrete.battleship.client.scene;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import static me.jcarrete.battleship.client.BattleshipClient.LOGGER;

/**
 * Represents the top and bottom grid and records information
 * about where shots have been fired and where pieces have been placed
 */
public class BattleshipGrid extends Canvas {

	private final int ROWS, COLS;
	private final int CELL_SIZE;

	public BattleshipGrid() {
		ROWS = 20;
		COLS = 10;
		CELL_SIZE = 25;
	}

	public void draw() {
		final GraphicsContext g = getGraphicsContext2D();

		// Draw grid
		g.setStroke(Color.GRAY);
		g.setLineWidth(2);
		for (int row = 0; row < ROWS; row++) {
			for (int col = 0; col < COLS; col++) {
				g.strokeRect(col * CELL_SIZE, row * CELL_SIZE, CELL_SIZE, CELL_SIZE);
			}
		}
	}
}
