package com.avapir.snake.Core.Listeners;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.PrintWriter;

import com.avapir.snake.Actors.PC.Direction;
import com.avapir.snake.Actors.PC.Snake;
import com.avapir.snake.Core.Core;
import com.avapir.snake.Core.Painter;
import com.avapir.snake.UI.Game;

/**
 * Keyboard handler
 * 
 * @author Alpen Ditrix
 */
public class KeyInputHandler implements KeyListener {

	/**
	 * System logger link
	 */
	PrintWriter log = Core.log;

	@Override
	public void keyPressed(KeyEvent e) {
		log.println(e.getKeyCode());
		switch (Core.getState()) {
		case PLAYING:
			switchPlaying(e);
			break;
		case PAUSE_MENU:
			switchPause(e);
			break;
		case MAIN_MENU:
			switchMain(e);
			break;
		case SETTING_MENU:
			switchSettings(e);
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			Painter.hideScores();
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	/**
	 * Handle hotkeys in main menu
	 * 
	 * @param e
	 */
	private void switchMain(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_ENTER:
			Core.invokeGame();
			break;
		case KeyEvent.VK_ESCAPE:
			Core.exit();
			break;
		}
	}

	/**
	 * Handle hotkeys in pause menu
	 * 
	 * @deprecated There's no pause menu available in stock
	 * @param e
	 */
	@Deprecated
	private void switchPause(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_ESCAPE:
			Core.invokeReturnToGame();
		}
	}

	/**
	 * Handle game controls
	 * 
	 * @param e
	 */
	private void switchPlaying(KeyEvent e) {
		Snake snake1 = Game.getSnakes().get(Game.getMySnake_ID());

		switch (e.getKeyCode()) {
		/* snake1 */
		case KeyEvent.VK_RIGHT:
			if (snake1.getDirection() != Direction.LEFT)
				snake1.setDirection(Direction.RIGHT);
			break;
		case KeyEvent.VK_UP:
			if (snake1.getDirection() != Direction.DOWN)
				snake1.setDirection(Direction.UP);
			break;
		case KeyEvent.VK_LEFT:
			if (snake1.getDirection() != Direction.RIGHT)
				snake1.setDirection(Direction.LEFT);
			break;
		case KeyEvent.VK_DOWN:
			if (snake1.getDirection() != Direction.UP)
				snake1.setDirection(Direction.DOWN);
			break;
		case KeyEvent.VK_SPACE:
			Painter.showScores();
			break;
		/* snake2 */
		// case KeyEvent.VK_D:
		// if (snake2.getDirection() != Direction.LEFT)
		// snake2.setDirection(Direction.RIGHT);
		// break;
		// case KeyEvent.VK_W:
		// if (snake2.getDirection() != Direction.DOWN)
		// snake2.setDirection(Direction.UP);
		// break;
		// case KeyEvent.VK_A:
		// if (snake2.getDirection() != Direction.RIGHT)
		// snake2.setDirection(Direction.LEFT);
		// break;
		// case KeyEvent.VK_S:
		// if (snake2.getDirection() != Direction.UP)
		// snake2.setDirection(Direction.DOWN);
		// break;
		case KeyEvent.VK_ESCAPE:
			Game.forcedGameOver();
		}
	}

	/**
	 * Handle hotkeys in settings menu
	 * 
	 * @param e
	 */
	private void switchSettings(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_ESCAPE:
			Core.invokeMainMenu();
		}
	}

}
