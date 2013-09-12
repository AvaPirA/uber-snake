package com.avapir.snake.Actors.NPC;

import java.awt.Graphics;

import com.avapir.snake.Actors.CellElement;
import com.avapir.snake.Actors.Updater;
import com.avapir.snake.Actors.PC.Snake;
import com.avapir.snake.Core.Core;
import com.avapir.snake.Core.Painter;
import com.avapir.snake.UI.Objects.Sprite;

/**
 * Objects of this class creates randomly as food.<br>
 * Then some was gathered it will appear as snake`s (who got this {@link Seed})
 * {@link Snake#weapon}
 * 
 * @author Alpen Ditrix
 */
public class Seed extends CellElement {

	/**
	 * Types of {@link Seed}
	 * 
	 * @author Alpen Ditrix
	 */
	public enum SeedType {
		/**
		 * Player will be able to install wall in few seconds at requested
		 * place.
		 */
		WALL,

		/**
		 * It will explode at requested place
		 */
		BOMB
	}

	private SeedType type;

	private int power;

	private static Sprite bomb = new Sprite("/img/cells/bomb-seed.png", 0, 0);
	/**
	 * How much seeds already at field
	 */
	private static int seedsCounter;
	private static final int MAX_SEEDS = CellElement.SIZE_HORIZONTAL
			* CellElement.SIZE_VERTICAL / 512;

	/**
	 * On every {@link Updater}`s cycle it gives a chance to spawn yet one, or
	 * maybe few seeds (but less than {@link CellElement#SIZE_HORIZONTAL}*@link
	 * {@link CellElement#SIZE_VERTICAL}/512)<br>
	 * Chance to appear at least something at one time is 1%
	 */
	public static void tryToSpawn() {
		int max = MAX_SEEDS - seedsCounter;
		if (max > -1) {
			int chances = Core.r.nextInt(5);
			for (; chances < 5; chances++) {
				int res = Core.r.nextInt(10000);
				if (res > 9900) {
					res -= 9900;
					int[] o = CellElement.randomEmptyXandY();
					if (res < 30) {
						new Seed(SeedType.BOMB, o[0], o[1]);
					} else if (res < 100) {
						new Seed(SeedType.WALL, o[0], o[1]);
					}
				}
			}
		}
	}

	/**
	 * Creates new seed and locates it on field
	 * 
	 * @param t
	 *            type
	 * @param x
	 * @param y
	 */
	public Seed(SeedType t, int x, int y) {
		super(x, y, false);
		type = t;
		power = Core.r.nextInt(50) + Core.r.nextInt(50);
		seedsCounter++;
	}

	@Override
	public void draw(Graphics g) {
		int x = this.x * CELL_SIZE;
		int y = this.y * CELL_SIZE + Painter.getTopPanelHeight();
		switch (type) {
		case BOMB:
			bomb.draw(g, x, y);
		}
	}

	/**
	 * @return {@link #power}
	 */
	public int getPower() {
		return power;
	}

	/**
	 * @return type of this {@link Seed}
	 */
	public SeedType getType() {
		return type;
	}
}
