package com.avapir.snake.Actors;

import java.io.PrintWriter;

import com.avapir.snake.Actors.NPC.Seed;
import com.avapir.snake.Actors.PC.Snake;
import com.avapir.snake.Core.Core;
import com.avapir.snake.Core.Core.ScreenState;
import com.avapir.snake.UI.Game;

/**
 * Method {@link #update()} from this class calls for every turn of game. It
 * checks if game must end now and counts actors positions for net turn
 * 
 * @author Alpen Ditrix
 */
public class Updater {

	private static final int limit = CellElement.SIZE_HORIZONTAL
			* CellElement.SIZE_VERTICAL - Game.getFOOD_AMOUNT();

	/**
	 * Logger will be informed if some "win"
	 */
	private static final PrintWriter log = Core.log;

	/**
	 * Debug only. Need to count SPS
	 */
	public static long steps = 0;

	/**
	 * Current maximum earned score
	 */
	private static double maxScore;

	/**
	 * @return current high score in game
	 */
	public static double getMaxScore() {
		return maxScore;
	}

	/**
	 * Main game-cycle method, which launches all pre2checks and counts up
	 * {@link CellElement#field}`s state after one discrete amount of time named
	 * "turn" Method checks condition of the game, moves snakes and feed them if
	 * possible
	 */
	public static void update() {
		if (Core.getState() == ScreenState.PLAYING) {
			checkForAppropriateSnakesCount();
			checkForGameEndByAliveLeftAmount();
			checkForGameEndByScoreThreshold();
			doTurn();
			Seed.tryToSpawn();
		}
		steps++;
	}

	/**
	 * RTFMethodName
	 */
	private static void checkForAppropriateSnakesCount() {
		if (Game.getSnakes().size() > limit) {
			throw new RuntimeException("Too much snakes to show! Limit is "
					+ CellElement.SIZE_HORIZONTAL + "x"
					+ CellElement.SIZE_VERTICAL + "-" + Game.getFOOD_AMOUNT()
					+ "=" + limit);
		}
	}

	/**
	 * RTFMethodName
	 */
	private static void checkForGameEndByAliveLeftAmount() {
		if (Snake.aliveSnakesLeft == 1) {
			Snake winner = null;
			for (Snake s : Game.getSnakes()) {
				if (s.isAlive()) {
					log.println("!!" + s.getPlayerName() + " had alive left");
					winner = s;
				}
			}
			Game.gameOver(winner);
		}
	}

	/**
	 * RTFMethodName
	 */
	private static void checkForGameEndByScoreThreshold() {
		for (Snake s : Game.getSnakes()) {
			double score = s.getScore();
			if (score > maxScore) {
				maxScore = score;
				if (score >= Game.getWinScore()) {
					log.println("!!" + s.getPlayerName() + " scored "
							+ s.getScore());
					Game.gameOver(s);
				}
			}
		}
	}

	/**
	 * Process snakes.<br>
	 * 1)Choose directions for all AI-using snakes<br>
	 * 2)Try to lunch from "next" cell <br>
	 * 3)Move all snake`s pieces to "next" cells
	 * 
	 */
	private static void doTurn() {
		for (Snake s : Game.getSnakes()) {
			if (s.isAlive())
				s.process();
		}
	}
}