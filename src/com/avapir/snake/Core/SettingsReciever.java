package com.avapir.snake.Core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.StringTokenizer;

import com.avapir.snake.UI.Game;

//TODO look howto making Externalize Strings and rewrite this class
@SuppressWarnings("javadoc")
/**
 * @author Alpen Ditrix
 */
public class SettingsReciever {

	private static enum Classification {
		GAME, CORE, GFX
	}

	/**
	 * Current parsing phase
	 */
	private static Classification state;

	/**
	 * Markers allows to rewrite config file on exit
	 */
	private static boolean rewrite = true;

	/* Game */
	public static Integer GAME_ON_START_SNAKE_LENGTH;

	public static Integer GAME_SNAKES_AMOUNT;
	public static Integer GAME_WIN_SCORE;
	public static Integer GAME_FOOD_DISPERSION;
	/* GFX */
	protected static Integer GFX_CELL_SIZE;

	protected static Integer GFX_HEIGHT;
	protected static Integer GFX_WIDTH;
	protected static Integer GFX_TOP_PANEL_Y_OFFSET;
	protected static Integer GFX_FRAMES_PER_SECOND;
	protected static Integer GFX_BUFFERS_AMOUNT;
	/* Core */
	protected static String CORE_VERSION;

	protected static Integer CORE_SPS;

	/**
	 * Used, if there's no already created config
	 */
	@Deprecated
	private static final String dfltSettings = "[Core]\nVersion=ver. 0.9.2 beta\ngame_speed=15\n[Mechanics]\nSnake_Length=15\nSnakes_Amount=22\nWin_Score=200\nFoodDispersion=75000\n[Graphics]\nCell_Size=10\nWidth=640\nHeight=480\nTop_Panel=30\n";
	@SuppressWarnings("unused")
	@Deprecated
	private static final String dfltSettingsFilePath = "set.gs";

	/**
	 * Path to config file
	 */
	private static final String settingsPath = "set.gs";
	/**
	 * Splits string with specified delimiters on few tokens
	 */
	private static StringTokenizer st;

	/**
	 * Allows fast file reading
	 */
	private static BufferedReader in;

	/**
	 * Allows fast file writing
	 */
	private static PrintWriter out;

	/**
	 * String in which the data will be read
	 */
	private static String s = "Praise the sun!";

	/**
	 * Tries to read settings from file. If fails - creates new.
	 * 
	 * @throws IOException
	 */
	public static void recieveSettings() throws IOException {
		if (new File(settingsPath).exists())
			recieveSettings(settingsPath);
	}

	public static void rewriteSettings() throws IOException {
		if (rewrite) {
			try {
				out = new PrintWriter(new FileWriter(settingsPath), false);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			String output = "[Core]\nVersion=" + Core.ver + "\nGame_Speed="
					+ Core.getSpeed() + "\n[Mechanics]\nSnake_Length="
					+ Game.getOnStartSnakeLength() + "\nSnakes_Amount="
					+ Game.getSnakesAmount() + "\nWin_Score="
					+ Game.getWinScore() + "\nFood_Dispersion="
					+ Game.getFoodDispersion()
					+ "\n[Graphics]\nFrames_Per_Second=" + Painter.getFPS()
					+ "\nCell_Size=" + Painter.getCellSize() + "\nWidth="
					+ Painter.getWidth() + "\nHeight=" + Painter.getHeight()
					+ "\nTop_Panel=" + Painter.getTopPanelHeight();
			out.print(output);
			out.flush();
			out.close();
		}
	}

	/**
	 * Opens {@link BufferedReader} and begins processing input. This method
	 * sets {@code state} marker in specified position to know what variables
	 * may be read now
	 * 
	 * @param path
	 * @throws IOException
	 */
	protected static void recieveSettings(String path) throws IOException {
		try {
			in = new BufferedReader(new FileReader(new File(path)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		s = in.readLine();

		while (s != null) {
			while (s != null && !s.contains("[")) {
				s = in.readLine();
			}
			if (s != null && s.charAt(0) == '['
					&& s.charAt(s.length() - 1) == ']') {
				s = s.substring(1, s.length() - 1);
				s = s.toLowerCase();
				if (s.equals("mechanics")) {
					state = Classification.GAME;
				} else if (s.equals("core")) {
					state = Classification.CORE;
				} else if (s.equals("graphics")) {
					state = Classification.GFX;
				}
				getTokens();
			}
		}
		in.close();
	}

	/**
	 * Compares received tag with others designed for Core needs
	 * 
	 * @param token1
	 *            variable tag
	 * @param token2
	 *            desired value
	 */
	private static void caseMeBabyForCore(String token1, String token2) {
		if (token1.equals("version")) {
			CORE_VERSION = token2;
		} else if (token1.equals("game_speed")) {
			CORE_SPS = Integer.parseInt(token2);
		}
	}

	/**
	 * Compares received tag with others designed for Game needs
	 * 
	 * @param token1
	 *            variable tag
	 * @param token2
	 *            desired value
	 */
	private static void caseMeBabyForGame(String token1, String token2) {
		if (token1.equals("snake_length")) {
			GAME_ON_START_SNAKE_LENGTH = Integer.parseInt(token2);
		} else if (token1.equals("snakes_amount")) {
			GAME_SNAKES_AMOUNT = Integer.parseInt(token2);
		} else if (token1.equals("win_score")) {
			GAME_WIN_SCORE = Integer.parseInt(token2);
		} else if (token1.equals("food_dispersion")) {
			GAME_FOOD_DISPERSION = Integer.parseInt(token2);
		}
	}

	/**
	 * Compares received tag with others designed for Graphics needs
	 * 
	 * @param token1
	 *            variable tag
	 * @param token2
	 *            desired value
	 */
	private static void caseMeBabyForGFX(String token1, String token2) {
		if (token1.equals("cell_size")) {
			GFX_CELL_SIZE = Integer.parseInt(token2);
		} else if (token1.equals("height")) {
			GFX_HEIGHT = Integer.parseInt(token2);
		} else if (token1.equals("width")) {
			GFX_WIDTH = Integer.parseInt(token2);
		} else if (token1.equals("top_panel")) {
			GFX_TOP_PANEL_Y_OFFSET = Integer.parseInt(token2);
		} else if (token1.equals("frames_per_second")) {
			GFX_FRAMES_PER_SECOND = Integer.parseInt(token2);
		} else if (token1.equals("triple_buffer")) {
			if (token2.equals("true"))
				GFX_BUFFERS_AMOUNT = 3;
			else
				GFX_BUFFERS_AMOUNT = 2;
		}
	}

	/**
	 * Invoked in case of non-existing another config file to create it from
	 * template
	 * 
	 * @throws IOException
	 * @deprecated
	 */
	@Deprecated
	@SuppressWarnings("unused")
	private static void generateSettingsFile() throws IOException {
		try {
			out = new PrintWriter(new FileWriter(settingsPath), false);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		out.println(dfltSettings);
		out.flush();
		out.close();
	}

	/**
	 * While not found next classification string (string, which contains '['),
	 * method will parse strings with previously selected way
	 * 
	 * @throws IOException
	 */
	private static void getTokens() throws IOException {
		s = in.readLine();
		while (s != null && !s.contains("[")) {
			String token1 = nextToken(), token2 = nextToken();
			switch (state) {
			case CORE:
				caseMeBabyForCore(token1.toLowerCase(), token2);
				break;
			case GAME:
				caseMeBabyForGame(token1.toLowerCase(), token2);
				break;
			case GFX:
				caseMeBabyForGFX(token1.toLowerCase(), token2);
				break;
			}

			s = in.readLine();
			while (s != null && (s.equals("") || s.charAt(0) == '#'))
				s = in.readLine();
		}
	}

	/**
	 * All appropriate to the format non-empty and non-classification string
	 * consists of 2 tokens: variable tag at left side from '=' and desired
	 * value.<br>
	 * This method takes raw string and splits it on 2 tokens. Each of them will
	 * be returned on calling this method.
	 * 
	 * @return
	 * @throws IOException
	 */
	private static String nextToken() throws IOException {
		if (st == null || !st.hasMoreTokens())
			st = new StringTokenizer(s, "=");
		return st.nextToken();
	}
}
