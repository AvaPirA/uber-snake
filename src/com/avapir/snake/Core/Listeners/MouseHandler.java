package com.avapir.snake.Core.Listeners;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.PrintWriter;
import java.util.Vector;

import com.avapir.snake.Actors.NPC.Plant;
import com.avapir.snake.Actors.NPC.Plant.PlantType;
import com.avapir.snake.Actors.NPC.Seed;
import com.avapir.snake.Core.Core;
import com.avapir.snake.Core.Painter;
import com.avapir.snake.UI.Game;
import com.avapir.snake.UI.Main;
import com.avapir.snake.UI.Pause;
import com.avapir.snake.UI.Settings;
import com.avapir.snake.UI.Objects.Button;

/**
 * Handles all mouse actions in menus and in game
 * 
 * @author Alpen Ditrix
 */
public class MouseHandler implements MouseListener, MouseMotionListener {

	/**
	 * System logger link
	 */
	public PrintWriter log = Core.log;

	/**
	 * Link on button, which is blured by mouse right now
	 */
	Button blured;

	/**
	 * Link on button, which is pressed right now
	 */
	Button pressed;

	/**
	 * @return default buttons map of current menu
	 */
	private static Vector<Button> getButtons() {
		switch (Core.getState()) {
		case MAIN_MENU:
			return Main.getButtons();
		case SETTING_MENU:
			Vector<Button> tm = new Vector<Button>();
			tm.addAll(Settings.getButtons());
			tm.addAll(Settings.getArrows());
			return tm;
		case PAUSE_MENU:
			return Pause.getButtons();
		default:
			return null;
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseDragged(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if (Core.getState() != Core.ScreenState.PLAYING) {
			Vector<Button> buttons = getButtons();
			if (buttons == null) {
				return;
			}
			if (blured != null) {
				blured.unblur();
			}
			for (int i = 0; i < buttons.size(); i++) {
				if (buttons.get(i).goal(e.getX(), e.getY())) {
					blured = buttons.get(i).blur();
					log.println(buttons.get(i) + " blured");
				}
			}
		} else {
			blured = null;
			pressed = null;
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (Core.getState() == Core.ScreenState.PLAYING) {
			int x = e.getX() / Painter.getCellSize();
			int y = (e.getY() - Painter.getTopPanelHeight())
					/ Painter.getCellSize();
			Seed w = Game.getSnakes().get(0).getWeapon();
			if (w != null) {
				if (w.getType() == Seed.SeedType.BOMB) {
					Plant.onPlant(PlantType.CATCHER, x, y, w.getPower());
					Game.getSnakes().get(0).removeUsed();
				}
			}
		} else {
			Vector<Button> buttons = getButtons();
			for (int i = 0; i < buttons.size(); i++) {
				if (buttons.get(i).isBlured()) {
					pressed = buttons.get(i).press();
					log.println(buttons.get(i) + " pressed");
				}
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (pressed != null) {
			pressed.release();
			if (pressed.goal(e.getX(), e.getY())) {
				pressed.onClick();
			}
			log.println(pressed + " released");
		}
		mouseMoved(e);

	}
}
