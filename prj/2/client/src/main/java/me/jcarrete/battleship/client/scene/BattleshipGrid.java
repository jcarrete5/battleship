package me.jcarrete.battleship.client.scene;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.HashSet;

import static me.jcarrete.battleship.client.BattleshipClient.LOGGER;

/**
 * Represents the top and bottom grid and records information
 * about where shots have been fired and where pieces have been placed
 */
public class BattleshipGrid extends Canvas {

	public static final int MISS = 0, HIT = 1, SUNK = 2;
	public static final int ROWS = 20, COLS = 10;
	public static final int CELL_SIZE = 25;
	public static final int MAX_SHIP_COUNT = 5;

	private ArrayList<Ship> ships;
	private Ship[][] cells;
	// green = hit, red = miss, blue = mouseHover, orange = fireLocation
	private Color[][] cellHighlights;
	private int[] lastPos;
	private int[] targetPos;
	private HashSet<Integer> untargetable;

	public BattleshipGrid() {
		ships = new ArrayList<>(5);
		cells = new Ship[ROWS][COLS];
		cellHighlights = new Color[ROWS][COLS];
		lastPos = new int[] {-1, -1};
		targetPos = new int[] {-2, -2};
		untargetable = new HashSet<>();
		setOnMouseMoved(this::onMouseMoved);
		setOnMouseExited(this::onMouseExited);
		setOnMouseClicked(this::onMouseClicked);
	}

	public int getTargetPos() {
		return toIndex(targetPos[0], targetPos[1]);
	}

	private static int toIndex(int row, int col) {
		return row * COLS + col;
	}

	private static int[] toPoint(int index) {
		return new int[] {index / COLS, index % COLS};
	}

	private static int[] pointToGrid(double x, double y) {
		return new int[] {(int)(y / CELL_SIZE), (int)(x / CELL_SIZE)};
	}

	private void onMouseClicked(MouseEvent event) {
		int[] index = pointToGrid(event.getX(), event.getY());
		int curRow = index[0], curCol = index[1];

		// Ignore clicks on bottom half
		if (curRow >= ROWS / 2) return;
		// Don't allow targeting of already targeted cells
		if (untargetable.contains(toIndex(curRow, curCol))) return;
		// Unmark last target position
		if (targetPos[0] >= 0 && targetPos[1] >= 0) {
			cellHighlights[targetPos[0]][targetPos[1]] = null;
		}

		targetPos[0] = curRow;
		targetPos[1] = curCol;

		cellHighlights[curRow][curCol] = Color.color(1, 0.5, 0, 0.5);
		draw();
	}

	private void onMouseMoved(MouseEvent event) {
		int[] curPos = pointToGrid(event.getX(), event.getY());
		int curRow = curPos[0], curCol = curPos[1];

		// If we didn't move out of the last cell, then do nothing
		if (lastPos[0] == curRow && lastPos[1] == curCol) return;
		// Ignore movement on bottom half
		if (curRow >= ROWS / 2) {
			if (!untargetable.contains(toIndex(lastPos[0], lastPos[1]))) {
				cellHighlights[lastPos[0]][lastPos[1]] = null;
				draw();
			}
			return;
		}
		// Don't highlight cells which can't be targeted
		if (untargetable.contains(toIndex(curRow, curCol))) {
			cellHighlights[lastPos[0]][lastPos[1]] = null;
			draw();
			return;
		}

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
	 * Update the color of the target cell based on whether or not it was hit.
	 * @param targetIndex
	 * @param hitStatus
	 */
	public void setHitColor(int targetIndex, int hitStatus) {
		int[] pos = toPoint(targetIndex);
		int r = pos[0], c = pos[1];

		if (hitStatus == MISS) {
			cellHighlights[r][c] = Color.color(1, 0, 0, 0.5);
		} else if (hitStatus == HIT || hitStatus == SUNK) {
			cellHighlights[r][c] = Color.color(0, 1, 0, 0.5);
		}
		draw();
	}

	/**
	 * Check if a specific cell on the grid has been hit
	 * @param r
	 * @param c
	 * @return
	 */
	public int getHitStatus(int r, int c) {
		LOGGER.info(String.format("Checking if cells[%d][%d] has been hit...", r, c));

		int hit = MISS;
		if (cells[r][c] != null) {
			hit = HIT;
			cells[r][c].hit();
			if (cells[r][c].isSunk()) {
				LOGGER.info("You sunk my battleship!");
				LOGGER.info("Battleship length: " + cells[r][c].length());
				hit = SUNK;
			} else {
				LOGGER.info("Hit!");
			}
		} else {
			LOGGER.info("Miss!");
		}

		return hit;
	}

	public boolean checkLose() {
		int sunkCount = 0;
		for (Ship ship : ships) {
			if (ship.isSunk()) {
				sunkCount++;
			}
		}
		return sunkCount == MAX_SHIP_COUNT;
	}

	public void disableTarget() {
		untargetable.add(getTargetPos());
	}

	public void resetTargetPosition() {
		targetPos[0] = -2;
		targetPos[1] = -2;
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
		cellHighlights = new Color[ROWS][COLS];
		lastPos = new int[] {-1, -1};
		targetPos = new int[] {-2, -2};
		untargetable = new HashSet<>();
	}
}
