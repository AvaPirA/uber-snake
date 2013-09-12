package com.avapir.snake.Actors.PC;

/**
 * 
 * @author Alpen Ditrix
 */

public enum Direction {

	@SuppressWarnings("javadoc")
	DOWN, @SuppressWarnings("javadoc")
	LEFT, @SuppressWarnings("javadoc")
	RIGHT, @SuppressWarnings("javadoc")
	STOP, @SuppressWarnings("javadoc")
	UP;

	/**
	 * NETWORKING
	 * 
	 * @param direction
	 *            in int
	 * @return (DIrection) int
	 */
	public static Direction fromInt(int direction) {
		switch (direction) {
		case 1:
			return DOWN;
		case 2:
			return LEFT;
		case 3:
			return RIGHT;
		case 4:
			return STOP;
		case 5:
			return UP;
		default:
			return null;
		}
	}

	/**
	 * Turns direction CCW
	 * 
	 * @param dir
	 *            direction to turn
	 * @return new direction
	 */
	public static Direction next(Direction dir) {
		switch (dir) {
		case RIGHT:
			return Direction.UP;
		case UP:
			return Direction.LEFT;
		case LEFT:
			return Direction.DOWN;
		case DOWN:
			return Direction.RIGHT;
		default:
			return Direction.STOP;
		}
	}

	/**
	 * Turns down or right(CCW) if impossible
	 * 
	 * @param dir
	 *            current direction
	 * @return turned direction
	 */
	public static Direction turnDown(Direction dir) {
		if (dir != Direction.UP)
			return Direction.DOWN;
		else
			return Direction.RIGHT;
	}

	/**
	 * Turns left or down(CCW) if impossible
	 * 
	 * @param dir
	 *            current direction
	 * @return turned direction
	 */
	public static Direction turnLeft(Direction dir) {
		if (dir != Direction.RIGHT)
			return Direction.LEFT;
		else
			return Direction.DOWN;
	}

	/**
	 * Turns right or up(CCW) if impossible
	 * 
	 * @param dir
	 *            current direction
	 * @return turned direction
	 */
	public static Direction turnRight(Direction dir) {
		if (dir != Direction.LEFT)
			return Direction.RIGHT;
		else
			return Direction.UP;
	}

	/**
	 * Turns up or left(CCW) if impossible
	 * 
	 * @param dir
	 *            current direction
	 * @return turned direction
	 */
	public static Direction turnUp(Direction dir) {
		if (dir != Direction.DOWN)
			return Direction.UP;
		else
			return Direction.LEFT;
	}

	/**
	 * NETWORKING
	 * 
	 * @return (int) Direction
	 */
	public int toInt() {
		switch (this) {
		case DOWN:
			return 1;
		case LEFT:
			return 2;
		case RIGHT:
			return 3;
		case STOP:
			return 4;
		case UP:
			return 5;
		}
		return 0;
	}
}