package com.avapir.snake.Core;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Random;

import com.avapir.snake.Actors.Updater;
import com.avapir.snake.Actors.NPC.Plant;
import com.avapir.snake.Core.Listeners.AdminListener;
import com.avapir.snake.Core.Listeners.KeyInputHandler;
import com.avapir.snake.Core.Listeners.MouseHandler;
import com.avapir.snake.UI.Game;
import com.avapir.snake.UI.Main;
import com.avapir.snake.UI.Pause;
import com.avapir.snake.UI.Settings;

/**
 * Main class which create application`s window, controls what to display now
 * and allows logging
 * 
 * @author Alpen Ditrix
 */
public class Core implements Runnable {

	/**
	 * All available screen states
	 * 
	 * @author Alpen Ditrix
	 */
	public enum ScreenState {
		/**
		 * Start screen
		 */
		MAIN_MENU,
		/**
		 * Break in game which may be continued
		 */
		PAUSE_MENU,
		/**
		 * Changing applications setting
		 */
		SETTING_MENU,
		/**
		 * Here game is going
		 */
		PLAYING,
	}

	/**
	 * Battle log encrypting password
	 * 
	 * @deprecated Java bugs with chars representation causes en/decrypting bugs
	 *             due
	 */
	@Deprecated
	@SuppressWarnings("unused")
	private static final String battleLogPassword = "we are";

	/**
	 * System log encrypting password
	 * 
	 * @deprecated due Java bugs with chars representation causes en/decrypting
	 *             bugs
	 */
	@Deprecated
	@SuppressWarnings("unused")
	private static final String systemLogPassword = "amazing";

	/**
	 * Current build version. {@code Painter} will print it under game splash at
	 * main menu
	 */
	public static final String ver = SettingsReciever.CORE_VERSION == null ? "ver.0.9.2beta"
			: SettingsReciever.CORE_VERSION;

	/**
	 * Current screen {@link #state}
	 */
	private static ScreenState state = ScreenState.MAIN_MENU;

	/**
	 * Veeeeery low-level "steps per second" variable. I willn't touch it as far
	 * as I don't want to do something with painting "game cycle"
	 */
	private static int SPS = SettingsReciever.CORE_SPS == null ? 15
			: SettingsReciever.CORE_SPS;

	/**
	 * Here I'll paint some cool stuff
	 */
	private static Canvas canvas;

	/**
	 * Here canvas will be placed
	 */
	private static Frame frame;

	/**
	 * System logger
	 */
	public static PrintWriter log;

	/**
	 * Musthave
	 */
	private static boolean adminMode;

	/**
	 * This marker defines is application is running yet
	 */
	private static boolean running = false;

	/**
	 * It's source of random numbers for whole application, because I don't want
	 * to create each one for every class, {@link Updater}`s iteration or
	 * something else<br>
	 * P.S. but for need of parallel threads I'll create each one new
	 * {@link Random} generators
	 */
	public static Random r = new Random();

	/**
	 * Date formatter for logging
	 */
	private static final SimpleDateFormat sdf = new SimpleDateFormat(
			"dd.mm.yy_hh:mm:ss_a");

	/**
	 * Logger initialization
	 */
	static {
		try {
			log = new PrintWriter(new FileWriter(".log"), true) {

				@Override
				/**
				 * 1) I want to print time before log message
				 * 2) I want encrypt log file
				 * 3) I want see warnings marked by "!!" at console
				 * 4) Praise the sun!
				 */
				public void print(String s) {
					super.print("\n");
					super.print("[");
					super.print(sdf.format(java.util.Calendar.getInstance()
							.getTime()));
					super.print("]: ");
					// try {
					// super.print(new Crypter(systemLogPassword).crypt(s,
					// "encrypt"));
					// } catch (UnsupportedEncodingException e) {
					// e.printStackTrace();
					// }
					super.print(s);
					if (s.contains("!!"))
						System.err.print(s.substring(2));
					else
						System.out.print(s);
				}

				/**
				 * To understand this method you must catch sight of "\n" at
				 * console is not "\n" at log-file
				 */
				@Override
				public void println(String s) {
					super.println(s);
					if (s.contains("!!"))
						System.err.println();
					else
						System.out.println();
				}

			};
		} catch (IOException e) {
			System.err.println("Uncaught I-O exception thrown!!");
			System.exit(100500);
		}
	}

	private static long delay = 1000 / SPS;

	private static long enterTime;

	private static long elapsed;

	private static long sleepTime;

	/**
	 * Decreases {@link Painter#getFPS()} when Game#SPS was
	 */
	public static void decreaseSpeed() {
		Painter.setFPS(Painter.getFPS() + 2 * Painter.getFPS() / SPS);
		SPS--;
	}

	/**
	 * Invoked on proper game exiting
	 */
	public static void exit() {
		log.println("Application destroying... ");
		// do something
		if (R.exitCode == -1) // if exitCode hasn't changed
			R.exitCode = 0;
		log.println("Exit: " + R.exitCode);
		log.println(System.getProperty("java.class.path"));
		running = false;
	}

	/**
	 * @return Can I use admininistrator`s tricks?
	 */
	public static boolean getAdminRightsAvailability() {
		return adminMode;
	}

	/**
	 * @return current canvas
	 */
	public static Canvas getCanvas() {
		return canvas;
	}

	/**
	 * @return {@link #SPS}
	 */
	public static int getSpeed() {
		return SPS;
	}

	/**
	 * @return {@link #state}
	 */
	public static ScreenState getState() {
		return state;
	}

	/**
	 * Increases speed to maintain {@link Painter#FPS}:{@link #SPS} ratio
	 */
	public static void increaseSpeed() {
		Painter.setFPS(Painter.getFPS() - 2 * Painter.getFPS() / SPS);
		SPS++;
	}

	/**
	 * Invoking when game start requested
	 */
	public static void invokeGame() {
		log.println("Invoked Game start");
		Game.create();
		state = ScreenState.PLAYING;
		Plant.startDestroyer();
	}

	/**
	 * Invoking when return to main menu requested
	 */
	public static void invokeMainMenu() {
		log.println("Invoked switch to Main Menu");
		state = ScreenState.MAIN_MENU;
	}

	/**
	 * @deprecated not used and not developed yet
	 */
	@Deprecated
	public static void invokeMultiplayerGame() {
	}

	/**
	 * Invoking when pause in game requested
	 * 
	 * @deprecated no pause menu created yet
	 */
	@Deprecated
	public static void invokePause() {
		log.println("Invoked Pause");
		Plant.PlantsDestroyer.pauseIntent();
		state = ScreenState.PAUSE_MENU;
	}

	/**
	 * Invoking then return to game from pause requested
	 * 
	 * @deprecated no pause menu created yet
	 */
	@Deprecated
	public static void invokeReturnToGame() {
		log.println("Invoked return to Game from Pause");
		Plant.PlantsDestroyer.returnIntent();
		state = ScreenState.PLAYING;
	}
	/**
	 * Invoking when seetings menu opening requested
	 */
	public static void invokeSettings() {
		log.println("Invoked switch to Settings Menu");
		state = ScreenState.SETTING_MENU;
	}
	/**
	 * Creates and configures application interface
	 */
	private static void configureWindow() {
		log.print("Configuring interface");
		frame = new Frame("UberSnake");
		log.print(".");
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				try {
					SettingsReciever.rewriteSettings();
				} catch (IOException e1) {
				}
				System.exit(0);
			}
		});
		log.print(".");
		frame.setLayout(new BorderLayout());
		log.print(".");
		frame.add(canvas, BorderLayout.CENTER);
		log.print(".");
		frame.pack();
		log.print(".");
		frame.setResizable(false);
		log.print(".");
		frame.setVisible(true);
		log.print(".");
		log.println("Done!");
	}
	/**
	 * Ooh~ It'll work up to the <s>world`s</s> game`s end and yet few times
	 * afterwards
	 */
	private static void cycle() {
		// new Thread(new Runnable() {
		// @Override
		// public void run() {
		// while (true) {
		// // try {
		// // Thread.sleep(100);
		// // } catch (InterruptedException e) {
		// // e.printStackTrace();
		// // }
		// System.err.println(Updater.steps + " : " + delay
		// + " : " + elapsed + " : " + sleepTime + " : "
		// + Snake.aliveSnakesLeft);
		// }
		// }
		// }).start();

		while (running) {

			enterTime = System.currentTimeMillis();

			try {
				Updater.update();
			} catch (NullPointerException e) {
				log.println("Lazy loading: state==null");
			}
			elapsed = System.currentTimeMillis() - enterTime;
			sleepTime = delay - elapsed;
			try {
				if (sleepTime > 0) {
					Thread.sleep(sleepTime);
				} else {
					Thread.sleep(10);
				}
			} catch (Exception e) {
				log.println("Error in game-updater`s sleeping part is going!!");
			}
		}
	}

	/**
	 * This method will call anther to load some resources from /res/ folder<br>
	 * I like it, but I thing some tons of Indian code may be found here ^_^
	 */
	private static void loadResources() {
		log.print("Loading resources");

		Main.create();
		log.print(".");
		Pause.create();
		log.print(".");
		Settings.create();
		log.print(".");
		log.println("Done!");
	}

	/**
	 * Creates and prepares canvas on window`s frame to paint some amazing
	 * graphics
	 */
	private static void setCanvas() {
		log.print("Canvas creating");
		canvas = new Canvas();
		log.print(".");
		canvas.setPreferredSize(new Dimension(Painter.getWidth()
				- Painter.getCellSize(), Painter.getHeight()
				- Painter.getCellSize()));
		log.print(".");
		MouseHandler mh = new MouseHandler();
		log.print(".");
		canvas.addMouseListener(mh);
		log.print(".");
		canvas.addMouseMotionListener(mh);
		log.print(".");
		canvas.addKeyListener(new KeyInputHandler());
		if (adminMode) {
			canvas.addKeyListener(new AdminListener());
		}
		log.print(".");
		log.println("Done!");
	}

	/**
	 * Invoking on application start after immediately after configuration file
	 * reading.<br>
	 * It prepares application window and starts infinite "game cycle"
	 */
	@Override
	public void run() {
		// log.println("Какая-то <amazing> инфа о состоянии системы");
		File file = new File("ohMyGodIAmAdmin.neversoft");
		if (file.exists()) {
			adminMode = true;
			log.println("!! ▲");
			log.println("!!▲ ▲");
		} else {
			log.println("Typical start performing...");
		}

		log.println("Application loading:");

		setCanvas();
		loadResources();
		configureWindow();

		log.println("Application loaded!");

		// Очевидно же
		(new Thread(new Painter(), "Painter")).start();
		log.println("Let's rock!\n===========================");
		state = ScreenState.MAIN_MENU;
		/* Some debug preconfig */
		// amazingDebugWillPass();
		/* End of debug config! */
		running = true;
		cycle();
	}
}