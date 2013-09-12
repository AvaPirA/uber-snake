package com.avapir.snake.Core.Listeners;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import com.avapir.snake.Actors.NPC.Food.FoodType;
import com.avapir.snake.Actors.NPC.Plant.PlantType;
import com.avapir.snake.Actors.NPC.Seed.SeedType;
import com.avapir.snake.Core.Core;
import com.avapir.snake.UI.Game;

/**
 * It's cheating class added to make debugging easier. Removing on release is
 * optional ;)
 * 
 * @author Alpen Ditrix
 */
@SuppressWarnings("all")
public class AdminListener implements KeyListener {

	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_1:
			Game.spawn(FoodType.VALHALLA);
			break;
		case KeyEvent.VK_0:
			Game.gameOver(Game.getSnakes().get(Game.getMySnake_ID()));
			break;
		case KeyEvent.VK_2:
			Game.spawn(FoodType.TYPICAL);
			break;
		case KeyEvent.VK_3:
			Game.spawn(FoodType.BIG);
			break;
		case KeyEvent.VK_4:
			Game.spawn(FoodType.INVULNERABILITY);
			break;
		case KeyEvent.VK_5:
			Game.spawn(FoodType.WALL);
			break;
		case KeyEvent.VK_6:
			Game.spawn(PlantType.CATCHER);
			break;
		case KeyEvent.VK_7:
			Game.spawn(SeedType.BOMB);
			break;
		case KeyEvent.VK_D:
			Game.getSnakes().get(0).addAdminDonut();
			break;
		case KeyEvent.VK_S:
			System.out.println(Core.getState());
			break;
		case KeyEvent.VK_F1:
			if (Core.getState() == Core.ScreenState.PLAYING) {
				Core.log.println("Paused");
				Core.invokePause();
			} else if (Core.getState() == Core.ScreenState.PAUSE_MENU) {
				Core.invokeReturnToGame();
				Core.log.println("Released");
			}
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

}
