package com.avapir.snake.UI;

import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.util.Vector;

import com.avapir.snake.Actors.CellElement;
import com.avapir.snake.Actors.NPC.Food;
import com.avapir.snake.Actors.NPC.Food.FoodType;
import com.avapir.snake.Actors.NPC.Plant;
import com.avapir.snake.Actors.NPC.Seed;
import com.avapir.snake.Actors.PC.Direction;
import com.avapir.snake.Actors.PC.Snake;
import com.avapir.snake.Core.Core;
import com.avapir.snake.Core.Painter;
import com.avapir.snake.Core.SettingsReciever;
import com.avapir.snake.Networking.HostHandler;
import com.avapir.snake.UI.Objects.Sprite;

/**
 * 
 * @author Alpen Ditrix
 */
public class Game {

	private static int onStartSnakeLength = SettingsReciever.GAME_ON_START_SNAKE_LENGTH == null ? 15
			: SettingsReciever.GAME_ON_START_SNAKE_LENGTH;

	private static int snakesAmount = SettingsReciever.GAME_SNAKES_AMOUNT == null ? 22
			: SettingsReciever.GAME_SNAKES_AMOUNT;

	private static int winScore = SettingsReciever.GAME_WIN_SCORE == null ? 200
			: SettingsReciever.GAME_WIN_SCORE;

	private static int foodDispersion = SettingsReciever.GAME_FOOD_DISPERSION == null ? 75000
			: SettingsReciever.GAME_FOOD_DISPERSION;

	private static int FOOD_AMOUNT = Painter.getWidth() * Painter.getHeight()
			/ foodDispersion;

	/* Networking */
	private static int mySnake_ID = 0;

	/**
	 * Used to store a bunch of snake in "short range"
	 */
	private static Vector<Snake> snakes;

	/**
	 * Snake, which scored the most points, left alive or eaten
	 * {@link FoodType#VALHALLA} buff
	 */
	private static Snake winner;

	/* Logging */
	private static PrintWriter log = Core.log;

	/**
	 * While we have only one weapon, we'll have only one sprite for it
	 */
	public static Sprite weaponSprite = new Sprite("/img/cells/bomb-seed.png",
			0, 0);

	/**
	 * Time, when current game started. Necessary for FPS counting
	 */
	public static long startTime;

	private static Object gameOverLocker = new Object();

	/**
	 * Used on every game start to initialize game field and actors
	 */
	public static void create() {
		CellElement.clearAll();
		setMySnake_ID(0);
		log.print("Game is loading:");
		if (Snake.aliveSnakesLeft > 22)
			throw new IllegalArgumentException(
					"It'll have bad appearance at score table. Please try to play with lesser snakes amount");
		winner = null;
		log.print(".");
		createSnakes();
		log.print(".");
		createFood();
		log.print(".");
		// try {
		// // RTF "Loading..."
		// Thread.sleep(1000);
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }
		log.print(".");
		log.println("Done!");
		log.println("Will be spawn " + FOOD_AMOUNT + " food");
		startTime = System.currentTimeMillis();
	}

	/**
	 * @deprecated Under development
	 */
	@Deprecated
	public static void createMultiplayer() {
		setMySnake_ID(0);
		log.print("Game is loading:");
		if (Snake.aliveSnakesLeft > 22)
			throw new IllegalArgumentException(
					"It'll have bad appearance at score table. Please try to play with lesser snakes amount");
		winner = null;
		log.print(".");
		createDumbSnakes();
		log.print(".");
		HostHandler.addPlayer(new InetSocketAddress(64961).getAddress(),
				snakes.get(mySnake_ID));
	}

	/**
	 * @param i
	 *            decreases {@link #foodDispersion}
	 */
	public static void decFoodDispersion(int i) {
		foodDispersion -= i;
	}

	/**
	 * @param i
	 *            decreases {@link #onStartSnakeLength}
	 */
	public static void decOnStartSnakeLength(int i) {
		onStartSnakeLength -= i;
	}

	/**
	 * @param i
	 *            decreases {@link #snakesAmount}
	 */
	public static void decSnakesAmount(int i) {
		snakesAmount -= i;
	}

	/**
	 * @param i
	 *            decreases {@link #winScore}
	 */
	public static void decWinScore(int i) {
		winScore -= i;
	}

	/**
	 * Method is used for manual exit on Main Menu screen from Game <br>
	 * <b>First</b> snake from {@code snakes} sets as winner
	 */
	public static void forcedGameOver() {
		log.println("Game force closed");
		gameOver(snakes.get(0), 0);
	}

	/**
	 * Method is used, when some snake takes "Valhalla" buff
	 * 
	 * @param winner
	 */
	public static void forceWin(Snake winner) {
		gameOver(winner, 2500);
	}

	/**
	 * Method is used for typical Game ends: only one snake is alive or<BR>
	 * someone crossed the threshold of {@code WinScore} It makes default 2.5
	 * sec delay before exiting on Main Menu screen
	 * 
	 * @param i
	 *            winner
	 */
	public static void gameOver(Snake i) {
		gameOver(i, 2500);
	}

	/**
	 * @return Returns direction of all snakes for sending by network
	 */
	public static Vector<Direction> getDirections() {
		Vector<Direction> res = new Vector<Direction>();
		for (Snake s : snakes) {
			res.add(s.getDirection());
		}
		return res;
	}

	/**
	 * @return {@link #FOOD_AMOUNT}
	 */
	public static int getFOOD_AMOUNT() {
		return FOOD_AMOUNT;
	}

	/**
	 * @return {@link #foodDispersion}
	 */
	public static int getFoodDispersion() {
		return foodDispersion;
	}

	/**
	 * @return {@link #mySnake_ID}
	 */
	public static int getMySnake_ID() {
		return mySnake_ID;
	}

	/**
	 * @return {@link #onStartSnakeLength}
	 */
	public static int getOnStartSnakeLength() {
		return onStartSnakeLength;
	}

	/**
	 * @return {@link #snakes}
	 */
	public static Vector<Snake> getSnakes() {
		return snakes;
	}

	/**
	 * @return {@link #snakesAmount}
	 */
	public static int getSnakesAmount() {
		return snakesAmount;
	}

	/**
	 * @return {@link #winScore}
	 */
	public static int getWinScore() {
		return winScore;
	}

	/**
	 * @param i
	 *            increases {@link #foodDispersion}
	 */
	public static void incFoodDispersion(int i) {
		foodDispersion += i;
	}

	/**
	 * @param i
	 *            increases {@link #onStartSnakeLength}
	 */
	public static void incOnStartSnakeLength(int i) {
		onStartSnakeLength += i;
	}

	/**
	 * @param i
	 *            increases {@link #snakesAmount}
	 */
	public static void incSnakesAmount(int i) {
		snakesAmount += i;
	}

	/**
	 * @param i
	 *            increases {@link #winScore}
	 */
	public static void incWinScore(int i) {
		winScore += i;
	}

	/**
	 * Used when some {@link Food} was eaten
	 * 
	 * @return created piece of food
	 */
	public static Food recreateFood() {
		return Food.createNewFood();
	}

	/**
	 * NETWORKING
	 * 
	 * @param mySnakeID
	 */
	public static void setMySnake_ID(int mySnakeID) {
		Game.mySnake_ID = mySnakeID;
	}

	/**
	 * NETWORKING
	 * 
	 * @param snakes
	 */
	public static void setSnakes(Vector<Snake> snakes) {
		Game.snakes = snakes;
	}

	/**
	 * Spawns new {@link CellElement} on field specefied by {@code c}
	 * 
	 * @param c
	 *            new element type
	 */
	public static void spawn(@SuppressWarnings("rawtypes") Enum c) {
		int x = Core.r.nextInt(CellElement.SIZE_HORIZONTAL);
		int y = Core.r.nextInt(CellElement.SIZE_VERTICAL);
		while (!CellElement.isEmpty(x, y)) {
			x = Core.r.nextInt(CellElement.SIZE_HORIZONTAL);
			y = Core.r.nextInt(CellElement.SIZE_VERTICAL);
		}
		if (c instanceof Food.FoodType) {
			new Food((Food.FoodType) c, x, y);
		} else if (c instanceof Plant.PlantType) {
			new Plant((Plant.PlantType) c, x, y, Core.r.nextInt(50)
					+ Core.r.nextInt(50));
		} else if (c instanceof Seed.SeedType) {
			new Seed((Seed.SeedType) c, x, y);
		}
	}

	/**
	 * @return {@link #winner}
	 */
	public static Snake whoIsWinner() {
		return winner;
	}

	/**
	 * Creates snakes for game beginning
	 */
	private static void createDumbSnakes() {
		snakes = new Vector<Snake>();
		boolean ai = false;
		for (int i = 0; i < 2; i++) {
			int xx = Core.r.nextInt(Painter.getWidth());
			// I don't want start game from stats-panel >_<
			int yy = Core.r.nextInt(Painter.getHeight() - 30) + 30;
			snakes.add(new Snake(Integer.toString(i + 1), xx - xx
					% Painter.getCellSize(), yy - yy % Painter.getCellSize(),
					onStartSnakeLength, ai));
		}
		snakes.get(0).setPlayerName(System.getProperty("user.name"));
		mySnake_ID = 0;
	}

	/**
	 * INITIALIZATION
	 * 
	 * Creates {@code FOOD_AMOUNT} pieces of food at random positions
	 */
	private static void createFood() {
		for (int i = 0; i < FOOD_AMOUNT; i++) {
			Food.createNewFood();
		}
		for (int i = 0; i < FOOD_AMOUNT * 5; i++) {
			spawn(FoodType.WALL);
		}
	}

	/**
	 * INITIALIZATION
	 * 
	 * Creates {@code snakesAmount} snakes at random positions,<BR>
	 * sets your {@code mySnakeID} and names it as OS user name
	 * 
	 * All created snakes excluding first are AI-users
	 */
	private static void createSnakes() {
		snakes = new Vector<Snake>();
		boolean ai = false;

		for (int i = 0; i < snakesAmount; i++) {
			int x = Core.r.nextInt(CellElement.SIZE_HORIZONTAL);
			int y = Core.r.nextInt(CellElement.SIZE_VERTICAL);
			snakes.add(new Snake(Integer.toString(i + 1), x, y,
					onStartSnakeLength, ai));
			ai = true;
		}
		snakes.get(0).setPlayerName(System.getProperty("user.name"));
		mySnake_ID = 0;
	}

	/**
	 * 
	 * @param i
	 *            winner
	 * @param delay
	 *            delay before exit to Main Menu
	 */
	private static void gameOver(Snake i, int delay) {
		synchronized (gameOverLocker) {
			Snake.aliveSnakesLeft = 0;
			if (i != null) {
				winner = i;
			}
			// just in case
			for (Snake s : snakes) {
				s.setDirection(Direction.STOP);
			}
			log.println("!! GameOver");
			Painter.showScores();
			try {
				Thread.sleep(delay);
				Painter.hideScores();
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			Core.invokeMainMenu();
		}
	}
}