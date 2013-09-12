package com.avapir.snake.Actors.NPC;

import java.awt.Color;
import java.awt.Graphics;

import com.avapir.snake.Actors.CellElement;
import com.avapir.snake.Core.Core;
import com.avapir.snake.Core.Painter;

/**
 * This class describes food pieces and their creating
 * 
 * 
 * @author Alpen Ditrix
 */
public class Food extends CellElement {

	/**
	 * 
	 * @author Alpen Ditrix
	 */
	public enum FoodType {
		/**
		 * Gives immediate victory. /TODO It will be recreated as Seed soon...
		 */
		VALHALLA,

		/**
		 * Kills eater
		 */
		WALL,

		/**
		 * Gives typical score and typical growth
		 */
		TYPICAL,

		/**
		 * Gives x3 times more score and growth than {@link #TYPICAL}
		 */
		BIG,

		/**
		 * The pieces of food which was created from some snake when it died
		 * Gives x0.2 times score and growth
		 */
		DEAD_SNAKE_PIECE,

		/**
		 * Gives invulnerability
		 */
		INVULNERABILITY
	}

	/**
	 * Type of this food piece
	 */
	protected FoodType type;

	/**
	 * Precomputes random coordinates for new {@link Food} piece, while free
	 * cell not chosen.<br>
	 * This method is necessary because in constructor {@link #Food(int, int)}
	 * first code-line must be a superclass constructor call and there I've only
	 * one time to chose random coordinates, and if it will be snakes`s head...<br>
	 * So you've got it
	 * 
	 * @return created food object
	 */
	public static Food createNewFood() {
		int[] o = CellElement.randomEmptyXandY();
		return new Food(o[0], o[1]);
	}

	/**
	 * Hard manual food injecting.
	 * 
	 * @param type
	 * @param x
	 * @param y
	 */
	public Food(FoodType type, int x, int y) {
		super(x, y, false);
		this.type = type;
	}

	/**
	 * Soft manual food injecting.
	 * 
	 * @param x
	 * @param y
	 */
	public Food(int x, int y) {
		super(x, y, false);
		type = getRandomType();
	}

	/**
	 * Selects the appropriate paint color and draws object in appropriate way
	 * 
	 * @param g
	 */
	@Override
	public void draw(Graphics g) {
		Color c = null;
		int x = this.x * CELL_SIZE;
		int y = this.y * CELL_SIZE + Painter.getTopPanelHeight();
		switch (type) {
		case TYPICAL:
			c = Color.gray;
			break;
		case BIG:
			c = Color.darkGray;
			break;
		case WALL:
			c = Color.gray;
			break;
		case VALHALLA:
			c = Color.yellow;
			break;
		case INVULNERABILITY:
			c = Color.green;
			break;
		case DEAD_SNAKE_PIECE:
			c = Color.red;
			break;
		}
		try {
			g.setColor(c);
		} catch (NullPointerException e) {
			Core.log.println("Non-applicated food type used!!");
			System.exit(0);
		}

		switch (type) {
		case VALHALLA:
			g.fillOval(x, y, CELL_SIZE, CELL_SIZE);
			break;
		case BIG:
		case TYPICAL:
			g.drawOval(x, y, CELL_SIZE, CELL_SIZE);
			break;
		case WALL:
			g.fillRect(x, y, CELL_SIZE, CELL_SIZE);
			break;
		case DEAD_SNAKE_PIECE:
			g.fillRect(x + CELL_SIZE / 5, y + CELL_SIZE / 5, CELL_SIZE * 3 / 5,
					CELL_SIZE * 3 / 5);
			break;
		}
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Food) {
			return x == ((Food) o).x && y == ((Food) o).y;
		} else
			return false;
	}

	/**
	 * Used in AI targeting, scoring and buffing
	 * 
	 * @return type of this food piece
	 */
	public FoodType getType() {
		return type;
	}

	/**
	 * Used in AI targeting and lunching
	 * 
	 * @return X position
	 */
	@Override
	public int getX() {
		return x;
	}

	/**
	 * Used in AI targeting and lunching
	 * 
	 * @return Y position
	 */
	@Override
	public int getY() {
		return y;
	}

	@Override
	public String toString() {
		return "F:" + type == null ? "null" : type.toString() + " (" + x + "; "
				+ y + ")";
	}

	/**
	 * OMFG!! A LITTLE BIT OF KOREAN RANDOM DETECTED HERE!
	 * 
	 * @return
	 */
	private FoodType getRandomType() {
		int res = Core.r.nextInt(10000);
		if (res < 50)
			// return FoodType.VALHALLA;
			return FoodType.TYPICAL;
		else if (res < 200)
			return FoodType.WALL;
		else if (res < 3500)
			return FoodType.BIG;
		else if (res < 3575)
			return FoodType.INVULNERABILITY;
		else
			return FoodType.TYPICAL;
	}
}