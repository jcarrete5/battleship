package me.jcarrete.battleship.client.scene;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Arrays;

import static me.jcarrete.battleship.client.BattleshipClient.LOGGER;

/**
 * Represents the top and bottom grid and records information
 * about where shots have been fired and where pieces have been placed
 */
public class BattleshipGrid extends Canvas {

	public static final int ROWS = 20, COLS = 10;
	public static final int CELL_SIZE = 25;

	private ArrayList<Ship> ships;
	private Ship[][] cells;

	public BattleshipGrid() {
		ships = new ArrayList<>(5);
		cells = new Ship[ROWS][COLS];
	}

	public void draw() {
		final GraphicsContext g = getGraphicsContext2D();
		g.save();

		g.clearRect(0, 0, getWidth(), getHeight());

		// Draw grid
		g.setStroke(Color.GRAY);
		g.setLineWidth(2);
		for (int row = 0; row < ROWS; row++) {
			for (int col = 0; col < COLS; col++) {
				g.strokeRect(col * CELL_SIZE, row * CELL_SIZE, CELL_SIZE, CELL_SIZE);
			}

			if (row == 10) {
				g.save();
				g.setStroke(Color.BLACK);
				g.strokeLine(0, row * CELL_SIZE, COLS * CELL_SIZE, row * CELL_SIZE);
				g.restore();
			}
		}

		// Draw ships
		for (Ship s : ships) {
			s.draw(g);
		}

		g.restore();
	}

	public void addShip(Ship ship) {
		ships.add(ship);

		// Add ship to cell grid
		if (ship.isHorizontal()) {
			for (int col = 0; col < ship.length(); col++) {
				cells[ship.row()][ship.col() + col] = ship;
			}
		} else {
			for (int row = 0; row < ship.length(); row++) {
				cells[ship.row() + row][ship.col()] = ship;
			}
		}
	}

	public void clear() {
		ships.clear();
		cells = new Ship[ROWS][COLS];
	}

	/**
	 * Validates the grid based on where the ships are placed.
	 * @return <tt>true</tt> if the grid is valid, otherwise <tt>false</tt>.
	 */
	public boolean isValid() {
		int count = 0;
		for (Ship[] row : cells) {
			for (Ship s : row) {
				if (s != null) {
					count++;
				}
			}
		}
		return count == 17;  // Should be exactly 17 cells with Ships
	}
}
