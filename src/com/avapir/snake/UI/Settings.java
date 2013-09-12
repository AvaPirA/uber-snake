package com.avapir.snake.UI;

import java.util.Vector;

import com.avapir.snake.Core.Core;
import com.avapir.snake.Core.Painter;
import com.avapir.snake.UI.Objects.Button;

/**
 * Settings screen. Here user may change some settings of game, core and
 * graphical parts of application
 * 
 * @author Alpen Ditrix
 */
public class Settings {

	@SuppressWarnings("javadoc")
	public enum PanelState {
		CORE, GAME, GFX, EMPTY
	}

	/**
	 * Current panel-switcher state
	 */
	public static PanelState state = PanelState.EMPTY;

	/**
	 * Button`s image paths array
	 */
	private static final String[] btns = { "/img/buttons/settings/gfx.png",// 0
			"/img/buttons/settings/game.png", // 1
			"/img/buttons/settings/back.png", // 2
			"/img/buttons/settings/core.png", // 3
	};

	/**
	 * Arrow-Switcher`s image paths array
	 */
	private static final String[] arws = { "/img/buttons/arrows/l.png", // 0
			"/img/buttons/arrows/r.png", // 1
	};

	/**
	 * Storage of all buttons at current screen
	 */
	private static Vector<Button> buttons;

	/**
	 * Self-initialized (by anonymous class) arrow-switchers vector
	 */
	private static Vector<Button> arrows = new Vector<Button>() {
		private static final long serialVersionUID = 1L;

		{
			add(new Button(arws[0], Painter.getWidth() * 2 / 3 - 20,
					Painter.getHeight() * 5 / 12 - 8) {
				@Override
				public void onClick() {
					switch (state) {
					case EMPTY:
						break;
					case CORE:
						Core.decreaseSpeed();
						break;
					case GAME:
						Game.decOnStartSnakeLength(1);
						break;
					case GFX:
						Painter.decreaseBufferization();
						break;
					}
				}
			});
			add(new Button(arws[0], Painter.getWidth() * 2 / 3 - 20,
					Painter.getHeight() * 6 / 12 - 8) {
				@Override
				public void onClick() {
					switch (state) {
					case EMPTY:
						break;
					case GAME:
						Game.decSnakesAmount(1);
						break;
					}
				}
			});
			add(new Button(arws[0], Painter.getWidth() * 2 / 3 - 20,
					Painter.getHeight() * 7 / 12 - 8) {
				@Override
				public void onClick() {
					switch (state) {
					case EMPTY:
						break;
					case GAME:
						Game.decWinScore(10);
						break;
					}
				}
			});
			add(new Button(arws[0], Painter.getWidth() * 2 / 3 - 20,
					Painter.getHeight() * 8 / 12 - 8) {
				@Override
				public void onClick() {
					switch (state) {
					case EMPTY:
						break;
					case GAME:
						Game.decFoodDispersion(1000);
						break;
					}
				}
			});
			add(new Button(arws[1], Painter.getWidth() * 2 / 3 + 70,
					Painter.getHeight() * 5 / 12 - 8) {
				@Override
				public void onClick() {
					switch (state) {
					case EMPTY:
						break;
					case CORE:
						Core.increaseSpeed();
						break;
					case GAME:
						Game.incOnStartSnakeLength(1);
						break;
					case GFX:
						Painter.increaseBufferization();
						break;
					}
				}
			});
			add(new Button(arws[1], Painter.getWidth() * 2 / 3 + 70,
					Painter.getHeight() * 6 / 12 - 8) {
				@Override
				public void onClick() {
					switch (state) {
					case EMPTY:
						break;
					case GAME:
						Game.incSnakesAmount(1);
						break;
					}
				}
			});

			add(new Button(arws[1], Painter.getWidth() * 2 / 3 + 70,
					Painter.getHeight() * 7 / 12 - 8) {
				@Override
				public void onClick() {
					switch (state) {
					case EMPTY:
						break;
					case GAME:
						Game.incWinScore(10);
						break;
					}
				}
			});
			add(new Button(arws[1], Painter.getWidth() * 2 / 3 + 70,
					Painter.getHeight() * 8 / 12 - 8) {
				@Override
				public void onClick() {
					switch (state) {
					case EMPTY:
						break;
					case GAME:
						Game.incFoodDispersion(1000);
						break;
					}
				}
			});
		}
	};

	/**
	 * Loads images into RAM and sets up buttons
	 */
	@SuppressWarnings("serial")
	public static void create() {
		final Button btn1 = new Button(btns[0], Painter.getWidth() / 10,
				Painter.getHeight() / 2) {
			@Override
			public void onClick() {
				Settings.showGFX();
			}
		};
		final Button btn2 = new Button(btns[1], Painter.getWidth() / 10,
				btn1.getY2() + btn1.getHeigth()) {
			@Override
			public void onClick() {
				Settings.showGame();
			}
		};
		final Button btn3 = new Button(btns[2], Painter.getWidth() / 10,
				btn2.getY2() + btn2.getHeigth()) {
			@Override
			public void onClick() {
				Core.invokeMainMenu();
			}
		};
		final Button btn4 = new Button(btns[3], Painter.getWidth() / 10,
				btn1.getY2() - btn1.getHeigth() * 2) {
			@Override
			public void onClick() {
				Settings.showCore();
			}
		};

		buttons = new Vector<Button>() {
			{
				add(btn1);
				add(btn2);
				add(btn3);
				add(btn4);
			}
		};

	}

	/**
	 * @return switching arrows vector
	 */
	public static Vector<Button> getArrows() {
		return arrows;
	}

	/**
	 * @return regular buttons
	 */
	public static Vector<Button> getButtons() {
		return buttons;
	}

	/**
	 * Need for {@link Painter}
	 * 
	 * @return current state of panel
	 */
	public static PanelState getPanelState() {
		return state;
	}

	/**
	 * Switches current panel to "Core" or to empty if "Core" already opened.
	 */
	public static void showCore() {
		state = (state != PanelState.CORE) ? PanelState.CORE : PanelState.EMPTY;
	}

	/**
	 * Switches current panel to "Game" or to empty if "Game" already opened.
	 */
	public static void showGame() {
		state = (state != PanelState.GAME) ? PanelState.GAME : PanelState.EMPTY;
	}

	/**
	 * Switches current panel to "GFX" or to empty if "GFX" already opened.
	 */
	public static void showGFX() {
		state = (state != PanelState.GFX) ? PanelState.GFX : PanelState.EMPTY;
	}
}