package me.jcarrete.battleship.client.scene;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;

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

	/**
	 * Adds a ship to the grid.
	 * @param ship
	 * @return <tt>true</tt> if the ship was placed successfullly, otherwise <tt>false</tt>.
	 */
	public boolean addShip(Ship ship) {
		Ship[][] oldCells = new Ship[ROWS][COLS];
		for (int row = 10; row < ROWS; row++) {
			System.arraycopy(cells[row], 0, oldCells[row], 0, COLS);
		}

		// Add ship to cell grid
		if (ship.isHorizontal()) {
			for (int col = 0; col < ship.length(); col++) {
				if (cells[ship.row()][ship.col() + col] != null) {
					cells = oldCells;
					return false;
				}

				cells[ship.row()][ship.col() + col] = ship;
			}
		} else {
			for (int row = 0; row < ship.length(); row++) {
				if (cells[ship.row() + row][ship.col()] != null) {
					cells = oldCells;
					return false;
				}

				cells[ship.row() + row][ship.col()] = ship;
			}
		}

		ships.add(ship);
		return true;
	}

	public void clear() {
		ships.clear();
		cells = new Ship[ROWS][COLS];
	}
}
