package com.avapir.snake.Core;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.util.Vector;

import com.avapir.snake.Actors.CellElement;
import com.avapir.snake.Actors.Updater;
import com.avapir.snake.Actors.NPC.Seed;
import com.avapir.snake.Actors.PC.Snake;
import com.avapir.snake.UI.Game;
import com.avapir.snake.UI.Main;
import com.avapir.snake.UI.Settings;
import com.avapir.snake.UI.Objects.Button;

/**
 * 
 * @author Alpen Ditrix
 */
public class Painter implements Runnable {

	@SuppressWarnings("unused")
	private static final class FramesCounter implements Runnable {
		public long[] tmpStartSteps = new long[100];
		public long lastSecondSPS = 0;
		public long[] tmpStartFrames = new long[100];
		public long lastSecondFPS = 0;

		@Override
		public void run() {
			int i = 0;
			while (true) {
				tmpStartSteps[i] = Updater.steps;
				tmpStartFrames[i] = frames;
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
				}
				i = (i + 1) % 100;
				lastSecondSPS = Updater.steps - tmpStartSteps[i];
				lastSecondFPS = frames - tmpStartFrames[i];
				System.out.print(lastSecondFPS);
				System.out.print(" : ");
				System.out.print(lastSecondSPS);
				System.out.print(" : ");
				System.out.println(Snake.aliveSnakesLeft);
			}
		}
	}

	/**
	 * It's bad idea to have non-integer ratio FPS/SPS. I strongly recommend to
	 * keep this ration at 1. And also I recommend to have FPS above 24.
	 * <p>
	 * Summary:
	 * <dd>SPS<24 ? FPS = 2*N*SPS, where N - natural number so as FPS>24<br>
	 * <dd>else FPR = N*SPS, where N - any natural number more than 2 Why more
	 * than 2? Because other way mouse controlling may be annoying
	 */
	private static int FPS = SettingsReciever.GFX_FRAMES_PER_SECOND == null ? 2 * Core
			.getSpeed() : SettingsReciever.GFX_FRAMES_PER_SECOND;

	/**
	 * Defines frame buffering strategy
	 */
	private static int buffers = SettingsReciever.GFX_BUFFERS_AMOUNT == null ? 2
			: SettingsReciever.GFX_BUFFERS_AMOUNT;
	/**
	 * It's time, which CPU will MUST spend on each frame painting.<br>
	 * If frame has been shown earlier, this thread will sleep <dd>
	 * {@code delay - elapsedTime}
	 */
	private static final long delay = 1000 / FPS;
	private static long enterTime;
	private static long sleepTime;

	private static long elapsed;

	/**
	 * Cell representation size in pixels
	 */
	private static final int CELL_SIZE = SettingsReciever.GFX_CELL_SIZE == null ? 10
			: SettingsReciever.GFX_CELL_SIZE;

	/**
	 * Window height in pixels
	 */
	private static final int HEIGHT = SettingsReciever.GFX_HEIGHT == null ? 480
			: SettingsReciever.GFX_HEIGHT;

	/**
	 * Window width in pixels
	 */
	private static final int WIDTH = SettingsReciever.GFX_WIDTH == null ? 640
			: SettingsReciever.GFX_WIDTH;

	/**
	 * In-game player stats panel height in pixels
	 */
	private static final int TOP_PANEL_Y_OFFSET = SettingsReciever.GFX_TOP_PANEL_Y_OFFSET == null ? 30
			: SettingsReciever.GFX_TOP_PANEL_Y_OFFSET;

	/**
	 * Menus background color
	 */
	private static final Color BACKGROUND_COLOR = Color.decode("#272822");

	/**
	 * Game background color
	 */
	private static final Color BACKGROUND_GAME_COLOR = Color.black;

	/**
	 * In-game player`s stats panel color
	 */
	private static final Color PANEL_COLOR = Color.gray;

	/**
	 * Default color for stats writing
	 */
	private static final Color STATS_COLOR = Color.orange;

	/**
	 * Background in-game grind color
	 */
	private static final Color GRID_COLOR = Color.decode("#111111");

	/**
	 * Color for some things about corpses
	 */
	private static final Color CORPSE_COLOR = Color.red;

	/**
	 * Let me think... It's {@link BufferStrategy}!
	 */
	private static BufferStrategy bufferStrategy;

	/**
	 * Let me think... It's {@linkplain Graphics}
	 */
	private static Graphics g;

	/**
	 * Let me think... It's {@linkplain Canvas}!
	 */
	private static Canvas canvas;

	/**
	 * I need to have some snakes to paint it, isn't me?
	 */
	private static Vector<Snake> snakes;

	/**
	 * Marker, which allow or deny showing score table
	 */
	private static boolean showMeScoreTable = false;

	/**
	 * Already painted frames. Used for FPS counting. At average 30FPS may count
	 * for about 10 millions milleniums. And twice if I'll set default as
	 * {@link Long#MIN_VALUE}
	 */
	public static long frames = 0;

	/**
	 * SETTINGS
	 */
	public static void decreaseBufferization() {
		buffers = 2;
	}

	/**
	 * Draws core settings
	 */
	public static void drawSettingsPanelForCore() {
		g.drawString("CORE", WIDTH / 2 - 40, HEIGHT / 4);
		g.drawString("Game speed:", WIDTH / 3, HEIGHT * 5 / 12);
		g.drawString(Integer.toString(Core.getSpeed()), WIDTH * 2 / 3,
				HEIGHT * 5 / 12);
		drawSettingsSwitchers(1);
	}

	/**
	 * Draws default settings screen
	 */
	public static void drawSettingsPanelForEmpty() {
		g.drawString(" Select menu", WIDTH / 2, HEIGHT / 2);
	}

	/**
	 * Draws game settings
	 */
	public static void drawSettingsPanelForGame() {
		g.drawString("GAME", WIDTH / 2 - 40, HEIGHT / 4);
		g.drawString("Start snake's length:", WIDTH / 3, HEIGHT * 5 / 12);
		g.drawString(Integer.toString(Game.getOnStartSnakeLength()),
				WIDTH * 2 / 3, HEIGHT * 5 / 12);
		g.drawString("Snakes amount:", WIDTH / 3, HEIGHT * 6 / 12);
		g.drawString(Integer.toString(Game.getSnakesAmount()), WIDTH * 2 / 3,
				HEIGHT * 6 / 12);
		g.drawString("Win score:", WIDTH / 3, HEIGHT * 7 / 12);
		g.drawString(Integer.toString(Game.getWinScore()), WIDTH * 2 / 3,
				HEIGHT * 7 / 12);
		g.drawString("Food Dispersion:", WIDTH / 3, HEIGHT * 8 / 12);
		g.drawString(Integer.toString(Game.getFoodDispersion()), WIDTH * 2 / 3,
				HEIGHT * 8 / 12);
		drawSettingsSwitchers(4);
	}

	/**
	 * Draws graphics settings
	 */
	public static void drawSettingsPanelForGFX() {
		g.drawString("GFX", WIDTH / 2 - 40, HEIGHT / 4);
		g.drawString("Bufferization:", WIDTH / 3, HEIGHT * 5 / 12);
		g.drawString(buffers == 2 ? "Double" : "Triple", WIDTH * 2 / 3,
				HEIGHT * 5 / 12);
		drawSettingsSwitchers(1);
	}

	/**
	 * @return height==width of cell
	 */
	public static int getCellSize() {
		return CELL_SIZE;
	}

	/**
	 * @return canvas height
	 */
	public static int getHeight() {
		return HEIGHT;
	}

	/**
	 * @return {@link #TOP_PANEL_Y_OFFSET}
	 */
	public static int getTopPanelHeight() {
		return TOP_PANEL_Y_OFFSET;
	}

	/**
	 * @return canvas width
	 */
	public static int getWidth() {
		return WIDTH;
	}

	/**
	 * Removes score table from screen
	 */
	public static void hideScores() {
		showMeScoreTable = false;
	}

	/**
	 * SETTINGS
	 */
	public static void increaseBufferization() {
		buffers = 3;
	}

	/**
	 * Main render method. At first it's creates buffer and reloads if it was,
	 * or continuing if buffer existed. Further it specifies current state and
	 * paints appropriate frame.
	 * <p>
	 * 
	 * @return If buffer existed and frame was painted successfully?
	 */
	public static boolean render() {
		if (precompute()) {
			// some magic hiding here
			return false;
		}

		switch (Core.getState()) {
		case PLAYING:
			drawBackground();
			drawGame();
			break;
		case MAIN_MENU:
			drawBackground();
			drawMain();
			break;
		case PAUSE_MENU:
			drawPause();
			break;
		case SETTING_MENU:
			drawBackground();
			drawSettings();
			break;
		}
		show();
		return true;
	}

	/**
	 * Shows pop-up score table
	 */
	public static void showScores() {
		showMeScoreTable = true;
	}

	/**
	 * @return current desired fps
	 */
	protected static int getFPS() {
		return FPS;
	}

	/**
	 * Invokes when {@link Core#SPS} has been changed in {@link Settings} to
	 * keep good ratio with {@link #FPS}
	 * 
	 * @param fps
	 */
	protected static void setFPS(int fps) {
		FPS = fps;
	}

	/**
	 * Sorts vector of snakes for paint it at pop-up score table
	 * 
	 * @return sorted by score snakes Vector
	 */
	@SuppressWarnings("unchecked")
	private static Vector<Snake> createSortedVectorOfSnakes() {
		// We must sort copy! Not the original vector!
		Vector<Snake> tmpSnakes = (Vector<Snake>) snakes.clone();
		Vector<Snake> tmp = new Vector<Snake>();
		for (int i = 0; i < tmpSnakes.size(); i++) {
			if (tmpSnakes.get(i).getScore() == Updater.getMaxScore()) {
				tmp.add(tmpSnakes.get(i));
				tmpSnakes.remove(i);
				break;
			}
		}
		for (int i = 0; i < tmpSnakes.size(); i++) {
			int j = 0;
			for (; j < tmp.size(); j++) {
				if (tmp.get(j).getScore() < tmpSnakes.get(i).getScore())
					break;
			}
			tmp.add(j, tmpSnakes.get(i));
		}
		return tmp;
	}

	/**
	 * Draws backgrounds for specified menu
	 */
	private static void drawBackground() {
		if (Core.getState() == Core.ScreenState.PLAYING)
			g.setColor(BACKGROUND_GAME_COLOR);
		else
			g.setColor(BACKGROUND_COLOR);
		g.fillRect(-1, -1, WIDTH + 1, HEIGHT + 1);
	}

	/**
	 * Draws all field cells
	 */
	private static void drawField() {
		for (int i = 0; i < CellElement.SIZE_HORIZONTAL; i++) {
			for (int j = 0; j < CellElement.SIZE_VERTICAL; j++) {
				CellElement.draw(i, j, g);
			}
		}
	}

	/**
	 * Draws game environment
	 */
	private static void drawGame() {
		drawTopPanel();
		drawGrid();
		drawField();
		if (showMeScoreTable) {
			drawScoresTable();
		}
	}

	/**
	 * Draws cells grid in game
	 */
	private static void drawGrid() {
		g.setColor(GRID_COLOR);
		for (int i = CELL_SIZE; i < WIDTH; i += CELL_SIZE) {
			g.drawLine(i, TOP_PANEL_Y_OFFSET, i, HEIGHT + CELL_SIZE);
		}
		for (int i = TOP_PANEL_Y_OFFSET + CELL_SIZE; i < HEIGHT; i += CELL_SIZE) {
			g.drawLine(-1, i, WIDTH + CELL_SIZE, i);
		}
	}

	/**
	 * Draws main menu
	 */
	private static void drawMain() {
		// draw buttons
		Vector<Button> buttons = Main.getButtons();
		for (Button btn : buttons) {
			btn.draw(g);
		}

		// draw splash
		Main.getSplash().draw(g);

		// draw current version
		if (Core.ver.contains("alpha")) {
			g.setColor(Color.red);
		} else if (Core.ver.contains("beta")) {
			g.setColor(Color.green);
		} else
			g.setColor(Color.white);
		g.drawString(Core.ver, Main.getSplash().getX1() + 4, Main.getSplash()
				.getY2() + 10);
	}

	/**
	 * @deprecated Pause menu is under development or forgotten
	 */
	@Deprecated
	private static void drawPause() {
	}

	/**
	 * Draws pop-up windows wits score table
	 */
	private static void drawScoresTable() {
		g.setColor(new Color(80, 80, 80, 150));
		int xOffset = WIDTH / 6;
		int yOffset = HEIGHT / 6;
		int limit = snakes.size() > 22 ? 22 : snakes.size();
		int s = limit * 15 + 30;
		int height = s;

		g.fillRoundRect(xOffset, yOffset, WIDTH * 2 / 3, height, WIDTH / 30,
				HEIGHT / 30);
		xOffset += 13;
		yOffset += 25;
		g.setColor(STATS_COLOR);
		Vector<Snake> sn = createSortedVectorOfSnakes();

		for (int i = 0; i < limit; i++) {
			if (!sn.get(i).isAlive())
				g.setColor(CORPSE_COLOR);
			// Имя
			g.drawString(sn.get(i).getPlayerName(), xOffset, yOffset + 15 * i);
			// Длина
			g.drawString(Integer.toString(sn.get(i).getLength()), xOffset
					+ WIDTH / 6, yOffset + 15 * i);
			// Очки
			g.drawString((Integer.toString(((Double) sn.get(i).getScore())
					.intValue())), xOffset + WIDTH / 6 + 30, yOffset + 15 * i);
			if (!sn.get(i).isAlive())
				g.setColor(Color.orange);
		}
	}

	/**
	 * Draws settings menu
	 */
	private static void drawSettings() {
		Vector<Button> buttons = Settings.getButtons();
		for (Button btn : buttons) {
			btn.draw(g);
		}
		g.setColor(Color.orange);
		// g.setFont(Core.settingsFont);
		switch (Settings.getPanelState()) {
		case EMPTY:
			drawSettingsPanelForEmpty();
			break;
		case CORE:
			drawSettingsPanelForCore();
			break;
		case GFX:
			drawSettingsPanelForGFX();
			break;
		case GAME:
			drawSettingsPanelForGame();
			break;
		default:
			break;
		}
	}

	/**
	 * Draws switchers for changing settings. Each screen has his own
	 * {@code amount} of needed switchers
	 * 
	 * @param amount
	 * @throws IllegalArgumentException
	 *             when {@code amount > 4}
	 */
	private static void drawSettingsSwitchers(int amount) {
		if (amount > 4) {
			throw new IllegalArgumentException(
					"Maximum switchers amount exceed");
		}
		Vector<Button> a = Settings.getArrows();
		for (int i = 0; i < amount; i++) {
			a.get(i).draw(g);
			a.get(i + 4).draw(g);
		}
	}

	/**
	 * Draws your stats on top panel
	 */
	private static void drawStats() {
		Snake mySnake = snakes.get(Game.getMySnake_ID());
		// Snake mySnake = snakes.get(i);
		if (!mySnake.isAlive()) {
			g.setColor(CORPSE_COLOR);
		} else if (mySnake.equals(Game.whoIsWinner())) {
			g.setColor(Color.magenta);
		} else if (mySnake.getScore() == Updater.getMaxScore()) {
			g.setColor(Color.green);
		} else {
			g.setColor(STATS_COLOR);
		}

		g.drawString(mySnake.toString(), 0, 10);
		g.drawString("Score: " + ((Double) mySnake.getScore()).intValue(), 0,
				25);
		g.drawString("Length: " + mySnake.getLength(), WIDTH / 3, 10);

		if (mySnake.getWeapon() != null) {
			if (mySnake.getWeapon().getType() == Seed.SeedType.BOMB) {
				g.drawString(Integer.toString(mySnake.getWeapon().getPower()),
						WIDTH - 45, (TOP_PANEL_Y_OFFSET - CELL_SIZE) / 2);
				Game.weaponSprite.draw(g, WIDTH - 30,
						(TOP_PANEL_Y_OFFSET - CELL_SIZE) / 2);
			}
		} else {
			g.drawString("0", WIDTH - 45, (TOP_PANEL_Y_OFFSET - CELL_SIZE) / 2);
			g.drawRect(WIDTH - 30, (TOP_PANEL_Y_OFFSET - CELL_SIZE) / 2,
					CELL_SIZE, CELL_SIZE);
		}
	}

	/**
	 * Draws background of top panel and fill it with data about your character
	 */
	private static void drawTopPanel() {
		g.setColor(PANEL_COLOR);
		g.fillRect(-1, -1, canvas.getWidth() + 1, 30);

		drawStats();
	}

	/**
	 * Creates buffer to paint new frame in
	 * 
	 * @return Is buffer was created or existed?
	 */
	@SuppressWarnings("unchecked")
	private static boolean precompute() {
		if (Core.getState() == Core.ScreenState.PLAYING) {
			snakes = (Vector<Snake>) Game.getSnakes().clone();
		}

		canvas = Core.getCanvas();

		try {
			bufferStrategy = canvas.getBufferStrategy();
			if (bufferStrategy == null) {
				canvas.createBufferStrategy(buffers);
				canvas.requestFocus();
				return true;
			}
			g = bufferStrategy.getDrawGraphics();
			return false;
		} catch (Exception e) {
			System.err.println(canvas);
			return true;
		}
	}

	/**
	 * Locates canvas and shows buffered frame
	 */
	private static void show() {
		g.dispose();
		bufferStrategy.show();
	}

	/**
	 * Begins the process of painting new frame. Method renders image into
	 * buffer if it exists and shows it or creates new buffer and paint anyway
	 */
	private static void startPainting() {
		if (render()) {
			show();
		} else {
			startPainting();
		}
	}

	/**
	 * Painting cycle with delays. If time spent on painting is lesser than
	 * defined delay, thread will wait a bit for next cycle.<br>
	 * Method will work after creating and starting {@code Painter} thread and
	 * stops only on exiting
	 */
	@Override
	public void run() {
		// new Thread(new Runnable() {
		// @Override
		// public void run() {
		// while (true) {
		// // try {
		// // Thread.sleep(100);
		// // } catch (InterruptedException e) {
		// // e.printStackTrace();
		// // }
		// System.err.println(Updater.steps + " : " + delay + " : " + elapsed +
		// " : "
		// + sleepTime + " : " + Snake.aliveSnakesLeft);
		// }
		// }
		// }).start();

		// new Thread(new FramesCounter()).start();

		while (true) {
			enterTime = System.currentTimeMillis();
			startPainting();
			frames++;
			elapsed = System.currentTimeMillis() - enterTime;
			sleepTime = delay - elapsed;
			try {
				if (sleepTime > 0) {
					Thread.sleep(sleepTime);
				} else {
					Thread.sleep(10);
				}
			} catch (Exception e) {
			}

		}
	}
}