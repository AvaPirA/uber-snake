package com.avapir.snake.Actors.PC;

import java.awt.Color;
import java.awt.Graphics;
import java.net.InetAddress;

import javax.lang.model.type.UnknownTypeException;

import com.avapir.snake.Actors.CellElement;
import com.avapir.snake.Actors.NPC.Food;
import com.avapir.snake.Actors.NPC.Food.FoodType;
import com.avapir.snake.Actors.NPC.Plant;
import com.avapir.snake.Actors.NPC.Plant.PlantType;
import com.avapir.snake.Actors.NPC.Seed;
import com.avapir.snake.Core.Core;
import com.avapir.snake.Core.Painter;
import com.avapir.snake.Interfaces.Paintable;
import com.avapir.snake.Interfaces.Relocatable;
import com.avapir.snake.Networking.HostHandler;
import com.avapir.snake.UI.Game;

/**
 * 
 * @author Alpen Ditrix
 * 
 */
public class Snake implements Paintable {

	/**
	 * 
	 * @author Alpen Ditrix
	 * 
	 */
	public final class Piece extends CellElement implements Relocatable {

		/**
		 * Link to {@link Snake}, which is owner of this {@link Piece}
		 */
		private Snake parent;

		/**
		 * Link to previous {@link Piece} in list. Obviously, "head"-piece has
		 * this field equated to null
		 */
		private Piece previous;

		/**
		 * Link to previous {@link Piece} in list. Obviously, last piece has
		 * this field equated to null
		 */
		private Piece next;

		// private Piece(Snake s, int x, int y) {
		// super(x, y);
		// parent = s;
		// }

		/**
		 * After calling superclass constructor, this method sets {@code s} as
		 * {@link #parent} and {@code prev} as {@link previous} <br>
		 * Also, to attach this {@link Piece} to other, {@link #previous}.
		 * {@linkplain #next} takes link on {@code this}
		 * 
		 * @param s
		 * @param prev
		 * @param x
		 * @param y
		 */
		private Piece(Snake s, Piece prev, int x, int y) {
			super(x, y, false);
			parent = s;

			previous = prev;
			try {
				previous.next = this;
			} catch (NullPointerException e) {
				// head created
			}
		}

		@Override
		public void draw(Graphics g) {
			int x = this.x * CELL_SIZE;
			int y = this.y * CELL_SIZE + Painter.getTopPanelHeight();

			if (previous != null) {//
				g.setColor(tailColor);
				g.fillRect(x + CELL_SIZE / 5, y + CELL_SIZE / 5,
						CELL_SIZE * 3 / 5, CELL_SIZE * 3 / 5); // tail
			} else {
				g.setColor(headColor);
				g.fillRect(x, y, CELL_SIZE, CELL_SIZE); // head
			}
		}

		@Override
		public boolean equals(Object p) {
			if (p instanceof CellElement) {
				if (p instanceof Piece) {
					return ((Piece) p).x == this.x && ((Piece) p).y == this.y;
				}
			}
			return false;
		}

		/**
		 * @return {@link #parent}
		 */
		public Snake getParent() {
			return parent;
		}

		/**
		 * @return true is previous piece does not exist
		 */
		public boolean isHead() {
			return previous == null;
		}

		@Override
		public void move() {
			int oldX = x;
			int oldY = y;
			CellElement back = move(direction);
			if (back instanceof Piece) {
				Piece piece = (Piece) back;
				while (piece.next != null) {
					piece = piece.next;
					CellElement.set(piece, oldX, oldY);
					int tmp = piece.x;
					piece.x = oldX;
					oldX = tmp;
					tmp = piece.y;
					piece.y = oldY;
					oldY = tmp;
				}
				if (piecesToGrow > 0) {
					new Piece(Snake.this, piece, oldX, oldY);
					piecesToGrow--;
					length++;
				} else {
					CellElement.set(null, oldX, oldY);
				}
			}
		}

		@Override
		public String toString() {
			return "P:" + Snake.this.getPlayerName() + " (" + x + "; " + y
					+ ")";
		}
	}

	/**
	 * Snakes population
	 */
	public static int aliveSnakesLeft;

	/**
	 * Corpses will be painted in this color
	 */
	public static final Color CORPSE_COLOR = new Color(255, 0, 0, 128);

	/**
	 * Name of this snake
	 */
	public String playerName;

	/**
	 * A distinctive feature used for comparing snakes by
	 * {@code #equals(Object)} method
	 */
	private int ID = Core.r.nextInt();

	/**
	 * Link to the first {@link Piece}
	 */
	private Piece head;

	/**
	 * Score collected by this snake
	 */
	private double score = 0;

	/**
	 * Direction of this snake's head
	 */
	private Direction direction = Direction.STOP;

	/**
	 * Current snake`s weapon
	 */
	private Seed weapon = new Seed(Seed.SeedType.BOMB, 0, 0);

	/**
	 * Current snake`s length. On start all snakes consists of minimal to left
	 * alive amount of pieces - 1. If {@link Game.ON_START_SNAKE_LENGTH} was
	 * more than it, snake will grow, until it will reach required length
	 */
	private int length = 1;

	/**
	 * Shows how many pieces should grow more
	 */
	private int piecesToGrow;

	/**
	 * Is this snake AI-controlled?
	 */
	private boolean usingAI;

	/**
	 * Affects on snake`s tail opacity
	 */
	private static final byte tailOpacity = 100;

	/**
	 * Each snake has her own color
	 */
	private Color headColor = Color.decode(Integer.toString(this.hashCode()));

	/**
	 * Each snake also has her own tail and color for painting it
	 */
	private Color tailColor = new Color(headColor.getRed(),
			headColor.getGreen(), headColor.getBlue(), tailOpacity);

	/**
	 * Score modifier
	 */
	private static final double STD_SCORE = 1;

	/* Scores */
	/* DO NOT FORGET TO FIX AI-TARGETING WIEGHT AFTER SCORES EDITING!!! */

	/**
	 * Score modifier
	 */
	private static final double BIG_MULTIPLIER = 3 * STD_SCORE;
	/**
	 * Score modifier
	 */
	private static final double VALHALLA_MULTIPLIER = 50 * STD_SCORE;
	/**
	 * Score modifier
	 */
	private static final double DEAD_TAIL_MULTIPLER = 0.2 * STD_SCORE;
	/**
	 * Score modifier
	 */
	private static final double ALIVE_TAIL_MULTIPLIER = 1.5 * STD_SCORE;
	/**
	 * Score modifier
	 */
	private static final double HEAD_MULTIPLIER = 2 * STD_SCORE;
	/**
	 * Amount of pieces, which will grow for each score collected
	 */
	private static final int STD_GROWER = 1;

	/**
	 * @param c
	 *            cell on the game field
	 * @return if {@code c} stores {@link Piece}
	 */
	public static boolean isPiece(CellElement c) {
		return c instanceof Piece;
	}

	/**
	 * Creates named snake with specified length
	 * 
	 * @param name
	 *            specified player name
	 * @param x
	 *            X head position
	 * @param y
	 *            Y head position
	 * @param size
	 *            length of created snake
	 * @param ai
	 *            AI-controlled?
	 */
	public Snake(String name, int x, int y, int size, boolean ai) {
		playerName = name;
		init(x, y, size, ai);
	}

	/**
	 * NETWORKING for adding alive player TODO: Check if it right written
	 * 
	 * @param name
	 * @param x
	 * @param y
	 * @param size
	 * @param playerAdress
	 */
	public Snake(String name, int x, int y, int size, InetAddress playerAdress) {
		playerName = name;
		init(x, y, size, false);
		HostHandler.addPlayer(playerAdress, this);
	}

	/**
	 * Gives additional 500 points to user
	 */
	@Deprecated
	public void addAdminDonut() {
		score += 500;
	}

	@Override
	public void draw(Graphics g) {
		Piece piece = head;
		while (piece != null) {
			piece.draw(g);
			piece = piece.next;
		}
	}

	@Override
	public boolean equals(Object obj) {
		// TODO I don't know how to write equals, but checking equaling for
		// Pieces strictly locks thread and I dunno why and don't research why -
		// it's monkey`s job IMO
		if (obj instanceof Snake) {
			return ((Snake) obj).ID == this.ID;
		} else
			return false;
	}

	/**
	 * @return current snake`s movement direction
	 */
	public Direction getDirection() {
		return direction;
	}

	/**
	 * @return amount of pieces
	 */
	public int getLength() {
		return length;
	}

	/**
	 * @return name of this snake
	 */
	public String getPlayerName() {
		return playerName;
	}

	/**
	 * @return Score of this snake
	 */
	public double getScore() {
		return score;
	}

	/**
	 * @return the weapon
	 */
	public Seed getWeapon() {
		return weapon;
	}

	/**
	 * Snakes remains alive, while her head != null (or length>0)
	 * 
	 * @return Is this snake alive?
	 */
	public boolean isAlive() {
		return head != null;
	}

	/**
	 * Used when snake is killed anyway <br>
	 * Replaces all pieces by synonymous food pieces
	 */
	public void kill() {
		Piece piece = head;
		while (piece != null) {
			piece.previous = null;
			new Food(FoodType.DEAD_SNAKE_PIECE, piece.getX(), piece.getY());
			piece = piece.next;
		}
		aliveSnakesLeft--;
		head = null;
		length = 0;
	}

	/**
	 * Carrying out calculations of lunching and moving snakes
	 */
	public void process() {
		if (usingAI) {
			direction = AI.paveWayFrom(this);
		}
		lunch();
		if (head != null) {
			move();
		}
	}

	/**
	 * Used when player used the weapon
	 */
	public void removeUsed() {
		weapon = null;
	}

	/**
	 * Used to stop snake or to manual control by user
	 * 
	 * @param direction
	 *            new direction to go
	 * @return old direction
	 */
	public Direction setDirection(Direction direction) {
		Direction old = this.direction;
		this.direction = direction;
		return old;
	}

	/**
	 * Gives snake ID manually
	 * 
	 * @param i
	 *            new ID
	 */
	public void setID(int i) {
		ID = i;
	}

	/**
	 * Used on snake creation
	 * 
	 * @param playerName
	 *            new name
	 */
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	@Override
	public String toString() {
		return playerName;
	}

	/**
	 * Link to head-{@link Piece} of this {@link Snake}
	 * 
	 * @return
	 */
	Piece getHead() {
		return head;
	}

	/**
	 * Used when some snake tries to move it`s head into cell, where already
	 * some {@link Food} is stored
	 * 
	 * @param food
	 *            what stored into next cell
	 */
	private void dinner(Food food) {
		eat(food);
		takeBuff(food);
		CellElement.set(null, food.getX(), food.getY());
		if (food.getType() != FoodType.DEAD_SNAKE_PIECE) {
			Food f = Game.recreateFood();
			while (f.getType() == FoodType.WALL) {
				f = Game.recreateFood();
			}
		}
	}

	/**
	 * Used when some snake tries to move it`s head into cell, where already
	 * another snake`s head located
	 * 
	 * @param s
	 *            {@link Snake}, who owns head into "next" cell
	 */
	private void duelWith(Snake s) {
		Snake winner = length > s.length ? this : s;
		Snake loser = length > s.length ? s : this;
		winner.score += loser.length * HEAD_MULTIPLIER;
		winner.grow(loser.length / 10);
		loser.kill();// transform loser into food
		winner.lunch();// retries lunching for winner, because new food piece
						// appeared from corpse eat it
	}

	/**
	 * Used to decide what to do with this meal
	 */
	private void eat(Food f) {
		switch (f.getType()) {
		case TYPICAL:
			grow(STD_GROWER);
			score += STD_SCORE;
			break;
		case BIG:
			grow(((Double) (STD_GROWER * BIG_MULTIPLIER)).intValue());
			score += STD_SCORE * BIG_MULTIPLIER;
			break;
		case WALL:
			kill();
			break;
		case VALHALLA:
			score += STD_SCORE * VALHALLA_MULTIPLIER;
			break;
		case DEAD_SNAKE_PIECE:
			score += STD_SCORE * DEAD_TAIL_MULTIPLER;
			grow(STD_GROWER);
		}
	}

	/**
	 * Increases amount of pieces, which will be grown (one piece for one turn).
	 * It means, what snakes tail will not move, when head will
	 * 
	 * @param amount
	 */
	private void grow(int amount) {
		piecesToGrow += amount;
	}

	/**
	 * Default snake initialization used by all constructors
	 * 
	 * @param x
	 * @param y
	 * @param size
	 * @param ai
	 */
	private void init(int x, int y, int size, boolean ai) {
		aliveSnakesLeft++;
		head = new Piece(this, null, x, y);
		piecesToGrow = size;
		usingAI = ai;
	}

	/**
	 * Used on every game turn. Method checks "next" by direction cell for some
	 * {@link CellElement} and if it have found - decides what to do with it.
	 * 
	 * @throws UnknownTypeException
	 *             If not found what to do with found {@link CellElement}
	 */
	private void lunch() {
		if (direction == Direction.STOP) {
			return;
		}
		CellElement headTo = head.lookForward(direction);
		if (headTo != null) {
			if (headTo instanceof Piece) {
				Piece toPiece = (Piece) headTo;
				if (toPiece.parent.equals(this)) {
					kill();// ate himself
				} else if (toPiece.isHead()) {
					duelWith(toPiece.parent);// someone will die
				} else {
					int cutLength = toPiece.parent.nipOff(toPiece);
					score += cutLength * ALIVE_TAIL_MULTIPLIER;
					grow(cutLength / 2);
					// ate someone`s tail
				}

			} else if (headTo instanceof Food) {
				dinner((Food) headTo);
			} else if (headTo instanceof Seed) {
				weapon = (Seed) headTo;
			} else if (headTo instanceof Plant) {
				if (((Plant) headTo).getType() == PlantType.CATCHER) {
					kill();
				}
			} else {
				throw new UnknownTypeException(null, headTo);
			}
		}
	}

	/**
	 * This method moves all snake forward in specified direction and grows new
	 * pieces on tail if needed
	 */
	private void move() {
		if (direction == Direction.STOP) {
			return;
		}
		head.move();
	}

	/**
	 * Used when some snake tries to move it`s head into cell, where already
	 * another snake`s non-head {@link Piece} located
	 * 
	 * @param toPiece
	 *            piece into "next" cell
	 * @return how long is nipped tail
	 */
	private int nipOff(Piece toPiece) {
		Piece piece = toPiece;
		int cutLength = 0;
		while (piece != null) {
			piece.previous.next = null;
			piece.previous = null;
			CellElement.set(null, piece.getX(), piece.getY());
			piece = piece.next;
			cutLength++;
		}
		length -= cutLength;
		return cutLength;
	}

	/**
	 * Eating {@linkplain Food} which looks as buffs
	 * 
	 * @param i
	 */
	private void takeBuff(Food f) {
		switch (f.getType()) {
		case VALHALLA:
			Core.log.println("!!" + playerName + " acquried blessing");
			Game.forceWin(this);
			break;
		}

	}
}