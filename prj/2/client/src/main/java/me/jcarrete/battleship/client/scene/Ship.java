package me.jcarrete.battleship.client.scene;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import static me.jcarrete.battleship.client.BattleshipClient.LOGGER;

public class Ship {

	private static final double OFFSET = BattleshipGrid.CELL_SIZE / 2.0;

	private Image shipSprite;
	private Point2D orientation;  // Length and direction
	private Point2D pos;

	private int timesHit;

	public Ship(Image sprite, int row, int col) {
		shipSprite = sprite;
		timesHit = 0;

		// Randomly choose orientation
		if (Math.random() < 0.5) {
			// +Horizontal
			orientation = new Point2D(shipSprite.getWidth() / BattleshipGrid.CELL_SIZE, 0);
		} else {
			// -Vertical
			orientation = new Point2D(0, -shipSprite.getWidth() / BattleshipGrid.CELL_SIZE);
		}

		pos = new Point2D(col * BattleshipGrid.CELL_SIZE + OFFSET, row * BattleshipGrid.CELL_SIZE + OFFSET);
	}

	public void draw(GraphicsContext g) {
		g.save();
		g.translate(pos.getX(), pos.getY());
		g.rotate(orientation.angle(1, 0));
		g.drawImage(shipSprite, -OFFSET, -OFFSET);
		g.restore();
	}

	public void hit() {
		timesHit++;
	}

	public boolean isSunk() {
		return timesHit == length();
	}

	public int row() {
		return (int) ((pos.getY() - OFFSET) / BattleshipGrid.CELL_SIZE);
	}

	public int col() {
		return (int) ((pos.getX() - OFFSET) / BattleshipGrid.CELL_SIZE);
	}

	public int length() {
		return (int) orientation.magnitude();
	}

	public boolean isHorizontal() {
		return orientation.angle(1, 0) == 0;
	}
}
