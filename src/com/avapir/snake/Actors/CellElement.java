package com.avapir.snake.Actors;

import java.awt.Graphics;

import com.avapir.snake.Actors.PC.Direction;
import com.avapir.snake.Core.Core;
import com.avapir.snake.Core.Painter;
import com.avapir.snake.Interfaces.Paintable;

/**
 * This class describes typical behavior of all elements, which are stored if
 * {@link #field} and will be painted when game is running
 * 
 * @author Alpen Ditrix
 */
public abstract class CellElement implements Paintable {

	/**
	 * Used in scaling coordinates for painting
	 */
	public static final int CELL_SIZE = Painter.getCellSize();

	/**
	 * Amount of vertical cells
	 */
	public static final int SIZE_VERTICAL = (Painter.getHeight() - Painter
			.getTopPanelHeight()) / CELL_SIZE;

	/**
	 * Amount of horizontal cells
	 */
	public static final int SIZE_HORIZONTAL = Painter.getWidth() / CELL_SIZE;

	/**
	 * Matrix, which stores game field
	 */
	private static final CellElement[][] field = new CellElement[SIZE_VERTICAL][SIZE_HORIZONTAL];

	/**
	 * It's not necessary but I want be sure, what field is empty on start
	 */
	static {
		for (int i = 0; i < field.length; i++) {
			for (int j = 0; j < field[i].length; j++) {
				field[i][j] = null;
			}
		}
	}

	/**
	 * Field coordinates of the horizontal axis
	 */
	protected int x;

	/**
	 * Field coordinates of the vertical axis
	 */
	protected int y;

	/**
	 * Sets all {@link #field}`s values as {@code null} (usually after end of
	 * game)
	 */
	public static void clearAll() {
		for (int i = 0; i < SIZE_VERTICAL; i++) {
			for (int j = 0; j < SIZE_HORIZONTAL; j++) {
				field[i][j] = null;
			}
		}
	}

	/**
	 * Tries to draw element in specified cell (if there is stored something)
	 * 
	 * @param x
	 * @param y
	 * @param g
	 */
	public static void draw(int x, int y, Graphics g) {
		if (field[y][x] != null)
			field[y][x].draw(g);
	}

	/**
	 * I don't want to use (Actually I don't want create some access right to
	 * {@link #field}) it, but I haven't found another solving of problem
	 * 
	 * @param x
	 * @param y
	 * @return element in specified cell.
	 */
	public static CellElement get(int x, int y) {
		try {
			return field[y][x];
		} catch (NullPointerException e) {
			return null;
		}
	}

	/**
	 * @param x
	 * @param y
	 * @return Is specified cell`s storage {@code == null}?
	 */
	public static boolean isEmpty(int x, int y) {
		try {
			return field[y][x] == null;
		} catch (ArrayIndexOutOfBoundsException e) {
			return true;
		}
	}

	/**
	 * Prints into console (it will not logged) storage of specified cell
	 * 
	 * @deprecated only for debugging
	 * @param x
	 * @param y
	 */
	@Deprecated
	public static void print(int x, int y) {
		try {
			System.out.println(field[y][x]);
		} catch (NullPointerException e) {
		}
	}

	/**
	 * @return array, which consists of two random numbers from 0 to
	 *         {@link #field}`s borders. Also {@code field[y][x] == null}
	 */
	public static int[] randomEmptyXandY() {
		int x = Core.r.nextInt(SIZE_HORIZONTAL);
		int y = Core.r.nextInt(SIZE_VERTICAL);
		while (!CellElement.isEmpty(x, y)) {
			x = Core.r.nextInt(SIZE_HORIZONTAL);
			y = Core.r.nextInt(SIZE_VERTICAL);
		}
		int[] out = { x, y };
		return out;
	}

	/**
	 * Removes old and puts new element into cell
	 * 
	 * @param e
	 * @param x
	 * @param y
	 */
	public static void set(CellElement e, int x, int y) {
		if (x >= 0 && x < SIZE_HORIZONTAL && y >= 0 && y < SIZE_VERTICAL)
			field[y][x] = e;
	}

	/**
	 * Default "two-arrow"(this.x == field[this.x].x) constructor used always
	 * while creating new {link CellElement} to initialize parameters and place
	 * element into {@link field}
	 * 
	 * @param x
	 * @param y
	 */
	protected CellElement(int x, int y, boolean check) {
		this.x = x;
		this.y = y;
		try {
			if (check) {
				if (field[y][x] != null) {
					field[y][x] = this;
				}
			} else {

				field[y][x] = this;
			}
		} catch (ArrayIndexOutOfBoundsException e) {
		}
	}

	/**
	 * Every element of field must be drawn on screen. Certainly, if it may be
	 * invisible nyohhohooo...
	 * 
	 * @param g
	 */
	@Override
	public abstract void draw(Graphics g);

	/**
	 * @return Coordinates of the horizontal axis
	 */
	public int getX() {
		return x;
	}

	/**
	 * @return Coordinates of the vertical axis
	 */
	public int getY() {
		return y;
	}

	/**
	 * @param direction
	 *            where we must look?
	 * @return storage of cell, which is next to cell of this
	 *         {@link CellElement} resident, specified by {@code direction}
	 */
	public CellElement lookForward(Direction direction) {
		int x = this.x;
		int y = this.y;
		switch (direction) {
		case DOWN:
			y = (y + 1 + SIZE_VERTICAL) % SIZE_VERTICAL;
			break;
		case LEFT:
			x = (x - 1 + SIZE_HORIZONTAL) % SIZE_HORIZONTAL;
			break;
		case RIGHT:
			x = (x + 1 + SIZE_HORIZONTAL) % SIZE_HORIZONTAL;
			break;
		case UP:
			y = (y - 1 + SIZE_VERTICAL) % SIZE_VERTICAL;
			break;
		case STOP:
			// do nothing
			break;
		}
		return field[y][x];
	}

	/**
	 * Copies current element into neighbor cell selected by {@code dir} and
	 * return old one
	 * 
	 * @param dir
	 *            where to move
	 * @return original element in old position
	 */
	public CellElement move(Direction dir) {
		int oldX = x;
		int oldY = y;
		switch (dir) {
		case DOWN:
			y = (y + 1 + SIZE_VERTICAL) % SIZE_VERTICAL;
			break;
		case LEFT:
			x = (x - 1 + SIZE_HORIZONTAL) % SIZE_HORIZONTAL;
			break;
		case RIGHT:
			x = (x + 1 + SIZE_HORIZONTAL) % SIZE_HORIZONTAL;
			break;
		case UP:
			y = (y - 1 + SIZE_VERTICAL) % SIZE_VERTICAL;
			break;
		case STOP:
			// do nothing
			break;
		}
		field[y][x] = this;
		return field[oldY][oldX];
	}

}
