package me.jcarrete.battleship.client.scene;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
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
	// green = hit, red = miss, blue = mouseHover, orange = fireLocation
	private Color[][] cellHighlights;
	private int[] lastPos;
	private int[] targetPos;

	public BattleshipGrid() {
		ships = new ArrayList<>(5);
		cells = new Ship[ROWS][COLS];
		cellHighlights = new Color[ROWS / 2][COLS];
		lastPos = new int[] {-1, -1};
		targetPos = new int[] {-2, -2};
		setOnMouseMoved(this::onMouseMoved);
		setOnMouseExited(this::onMouseExited);
		setOnMouseClicked(this::onMouseClicked);
	}

	private int[] pointToGrid(double x, double y) {
		return new int[] {(int)(y / CELL_SIZE), (int)(x / CELL_SIZE)};
	}

	private void onMouseClicked(MouseEvent event) {
		int[] index = pointToGrid(event.getX(), event.getY());
		int curRow = index[0], curCol = index[1];

		// Ignore clicks on bottom half
		if (curRow >= ROWS / 2) return;

		if (targetPos[0] >= 0 && targetPos[1] >= 0) {
			// Unmark last target position
			cellHighlights[targetPos[0]][targetPos[1]] = null;
		}

		targetPos[0] = curRow;
		targetPos[1] = curCol;

		cellHighlights[curRow][curCol] = Color.color(1, 0.5, 0, 0.5);
		draw();
	}

	private void onMouseMoved(MouseEvent event) {
		int[] index = pointToGrid(event.getX(), event.getY());
		int curRow = index[0], curCol = index[1];

		// If we didn't move out of the last cell, then do nothing
		if (lastPos[0] == curRow && lastPos[1] == curCol) return;
		// Ignore movement on bottom half
		if (curRow >= ROWS / 2) return;

		// Remove highlight on last position if it exists and isn't the target
		if (lastPos[0] >= 0 && lastPos[1] >= 0 && !(lastPos[0] == targetPos[0] && lastPos[1] == targetPos[1])) {
			cellHighlights[lastPos[0]][lastPos[1]] = null;
		}

		// If target position is not the current position
		if (!(targetPos[0] == curRow && targetPos[1] == curCol)) {
			// Then highlight current position
			cellHighlights[curRow][curCol] = Color.color(0, 0, 1, 0.5);
		}

		lastPos[0] = curRow;
		lastPos[1] = curCol;

		draw();
	}

	private void onMouseExited(MouseEvent event) {
		if (lastPos[0] >= 0 && lastPos[1] >= 0 && !(lastPos[0] == targetPos[0] && lastPos[1] == targetPos[1])) {
			cellHighlights[lastPos[0]][lastPos[1]] = null;
		}
		draw();
	}

	public void draw() {
		final GraphicsContext g = getGraphicsContext2D();
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

		// Draw highlights
		g.save();
		for (int row = 0; row < ROWS / 2; row++) {
			for (int col = 0; col < COLS; col++) {
				if (cellHighlights[row][col] == null) continue;
				g.setFill(cellHighlights[row][col]);
				g.fillRect(col * CELL_SIZE, row * CELL_SIZE, CELL_SIZE, CELL_SIZE);
			}
		}
		g.restore();
	}

	/**
	 * Adds a ship to the grid.
	 * @param ship
	 * @return <tt>true</tt> if the ship was placed successfully, otherwise <tt>false</tt>.
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
