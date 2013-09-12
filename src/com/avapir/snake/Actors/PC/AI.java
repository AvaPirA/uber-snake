package com.avapir.snake.Actors.PC;

import com.avapir.snake.Actors.CellElement;
import com.avapir.snake.Actors.NPC.Food;
import com.avapir.snake.Actors.NPC.Food.FoodType;
import com.avapir.snake.Actors.PC.Snake.Piece;

/**
 * Simple AI for snakes It computes behavior for only one snake at one time so I
 * use cycle for all AI-controlled snakes
 * 
 * @author Alpen Ditrix
 */
public class AI {
	// TODO add react on seeds and maybe plants

	/**
	 * Link to snake, which behavior is computing now
	 */
	private static Snake snake;

	/**
	 * It's temporary {@link Direction} object. It stores current direction
	 * value which will be given to {@link #snake}
	 */
	private static Direction dir;

	/**
	 * We must know it, because snakes unable changing direction to opposite in
	 * one move. In case of choosing opposite direction, will be returned
	 * rotated for 90 degrees CCW
	 */
	private static Direction currentDir;

	private static final double SCALER_BIG = 0.3333333d;
	private static final double SCALER_RAGNARÖK = 100;
	private static final double SCALER_VALHALLA = 0;
	private static final double SCALER_INVULNERABILITY = 0.1d;
	private static final double SCALER_DEAD = 5;

	/**
	 * Method paves shortest way without wall-wraps and correct if it is suicide
	 * 
	 * @param s
	 *            snake, which will be directed
	 * @return chosen direction
	 */
	public static Direction paveWayFrom(Snake s) {
		snake = s;
		currentDir = s.getDirection();
		getDirectionTo(targetFrom(s.getHead()));

		correction();
		return dir;
	}

	/**
	 * Switches for all available {@link Direction} values, while there isn't
	 * {@link #snake}`s piece in the next cell
	 * 
	 * @return new value if staying alive is available or old value if it isn't
	 */
	private static boolean checkForSuicide() {
		CellElement headTo = snake.getHead().lookForward(dir);
		if (headTo != null && headTo instanceof Piece
				&& ((Piece) headTo).getParent().equals(snake)) {
			dir = Direction.next(dir);
			return true;
		} else
			return false;
	}

	/**
	 * Switches for all available {@link Direction} values, while there isn't
	 * {@link FoodType#WALL} element into next cell
	 * 
	 * @return ew value if staying alive is available or old value if it isn't
	 */
	private static boolean checkForWall() {
		CellElement headTo = snake.getHead().lookForward(dir);
		if (headTo != null && headTo instanceof Food
				&& ((Food) headTo).getType() == FoodType.WALL) {
			dir = Direction.next(dir);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * AI looks forward and checks two mortal cases: <li>if next cell is
	 * occupied directly by the same snake <li>if next cell contains wall
	 * <p>
	 * When bad conditions has been found, direction will be changed CCW and
	 * test will be conducted again
	 */
	private static void correction() {
		for (int i = 0; i < 4; i++) {
			if (!checkForSuicide())
				break;
		}
		for (int i = 0; i < 4; i++) {
			if (!checkForWall()) {
				break;
			}
		}
	}

	/**
	 * Calculates shortest non-wrap way from {@code location} to {@code target} <br>
	 * Obviously it appears as ladder approximated to straight line
	 * 
	 * @param target
	 *            position of food piece chosen as target
	 */
	private static void getDirectionTo(Food food) {
		int x = snake.getHead().getX();
		int y = snake.getHead().getY();

		if (food.getX() != x) {
			float tan = (y - food.getY()) / (food.getX() - x);
			if (food.getX() > x) {
				if (tan > 1)
					dir = Direction.turnUp(currentDir);
				else if (tan <= -1)
					dir = Direction.turnDown(currentDir);
				else
					dir = Direction.turnRight(currentDir);
			} else {
				if (tan > 1)
					dir = Direction.turnDown(currentDir);
				else if (tan <= -1)
					dir = Direction.turnUp(currentDir);
				else
					dir = Direction.turnLeft(currentDir);
			}
		} else {
			if (food.getY() < y)
				dir = Direction.turnUp(currentDir);
			else
				dir = Direction.turnDown(currentDir);
		}
	}

	/**
	 * This method scans all game field and searches for good target. Costs of
	 * target counts from multiplying distance from {@code here} and some
	 * scaling parameter
	 * 
	 * @param here
	 *            head of {@link #snake}
	 * @return
	 */
	private static Food targetFrom(Piece here) {
		Food target = null;
		double distance = Integer.MAX_VALUE;
		for (int j = 0; j < CellElement.SIZE_VERTICAL; j++) {
			for (int i = 0; i < CellElement.SIZE_HORIZONTAL; i++) {
				CellElement c = CellElement.get(i, j);
				if (c != null && c instanceof Food) {
					Food f = (Food) c;
					double newDistance = Math.sqrt((f.getX() - here.getX())
							* (f.getX() - here.getX())
							+ (f.getY() - here.getY())
							* (f.getY() - here.getY()));

					// Applying modifiers of priority
					switch (f.getType()) {
					case BIG:
						newDistance *= SCALER_BIG;
						break;
					case WALL:
						newDistance *= SCALER_RAGNARÖK;
						break;
					case VALHALLA:
						newDistance *= SCALER_VALHALLA;
						break;
					case INVULNERABILITY:
						newDistance *= SCALER_INVULNERABILITY;
					case DEAD_SNAKE_PIECE:
						newDistance *= SCALER_DEAD;
					}
					if (newDistance < distance) {
						target = f;
						distance = newDistance;
					}
				}
			}
		}
		return target;
	}
}