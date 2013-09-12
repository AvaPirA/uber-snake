package com.avapir.snake.UI;

import java.util.Vector;

import com.avapir.snake.Core.Core;
import com.avapir.snake.Core.Painter;
import com.avapir.snake.UI.Objects.Button;
import com.avapir.snake.UI.Objects.Sprite;

/**
 * Represents on-start main menu. Have buttons to start single- or multi-player
 * games, settings and exit
 * 
 * @author Alpen Ditrix
 */
public class Main {

	/**
	 * Button`s image paths array
	 */
	private static final String[] btns = { "/img/buttons/main/new-game.png",// 0
			"/img/buttons/main/settings.png",// 1
			"/img/buttons/main/exit.png",// 2
			"/img/buttons/main/multiplayer.png"// 3
	};

	/**
	 * Splash`s image paths array
	 */
	private static final String[] imgs = { "/img/splash.png",// 0
	};

	/**
	 * Storage of all buttouns mapped by spline names
	 */
	private static Vector<Button> environment;

	/**
	 * Used pictures
	 */
	private static Sprite splash;

	/**
	 * Loads images into RAM and sets up buttons
	 */
	@SuppressWarnings({ "serial" })
	public static void create() {
		final Button btn1 = new Button(btns[0], Painter.getWidth() / 2 - 75,
				Painter.getHeight() * 5 / 8) {
			@Override
			public void onClick() {
				Core.invokeGame();
			}
		};
		final Button btn2 = new Button(btns[1], Painter.getWidth() / 2,
				Painter.getHeight() * 6 / 8) {
			@Override
			public void onClick() {
				Core.invokeSettings();
			}
		};
		final Button btn3 = new Button(btns[2], Painter.getWidth() / 2,
				Painter.getHeight() * 7 / 8) {
			@Override
			public void onClick() {
				Core.exit();
			}
		};
		final Button btn4 = new Button(btns[3], Painter.getWidth() / 2 + 75,
				Painter.getHeight() * 5 / 8) {
			@SuppressWarnings("deprecation")
			@Override
			public void onClick() {
				Core.invokeMultiplayerGame();
			}
		};

		splash = new Sprite(imgs[0], Painter.getWidth() / 2,
				Painter.getHeight() * 2 / 8);

		environment = new Vector<Button>() {
			{
				add(btn1);
				add(btn2);
				add(btn3);
				add(btn4);
			}
		};
	}

	/**
	 * @return buttons map
	 */
	public static Vector<Button> getButtons() {
		return environment;
	}

	/**
	 * @return main application splash
	 */
	public static Sprite getSplash() {
		return splash;
	}

}