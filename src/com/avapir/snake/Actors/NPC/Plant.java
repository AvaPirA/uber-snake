package com.avapir.snake.Actors.NPC;

import java.awt.Color;
import java.awt.Graphics;

import com.avapir.snake.Actors.CellElement;
import com.avapir.snake.Actors.Updater;
import com.avapir.snake.Core.Core;
import com.avapir.snake.Core.Core.ScreenState;
import com.avapir.snake.Core.Painter;
import com.avapir.snake.Interfaces.Service;

/**
 * @author Alpen Ditrix
 * 
 */
public class Plant extends CellElement {

	/**
	 * This service checks if some plant life is ended up and it must be removed
	 * from field
	 * 
	 * @author Alpen Ditrix
	 */
	public static class PlantsDestroyer implements Service {

		private static long prevProceedStep = Updater.steps;
		private static boolean paused = false;
		private static boolean returned = false;
		private static long pauseTimeElapsed = 0;

		/**
		 * Called on {@link com.avapir.snake.Core.Core.ScreenState#PAUSE_MENU}
		 * invocation to mark current time
		 */
		public static void pauseIntent() {
			paused = true;
		}

		/**
		 * Called on {@link com.avapir.snake.Core.Core.ScreenState#PAUSE_MENU}
		 * invocation to compute new {@link #pauseTimeElapsed}
		 */
		public static void returnIntent() {
			returned = true;
		}

		@Override
		public void run() {
			while (Core.getState() != ScreenState.PLAYING) {
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			while (true) {
				if (prevProceedStep == Updater.steps) {
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					continue;
				}

				if (paused) {
					pauseTimeElapsed -= System.currentTimeMillis();
					paused = false;
				}
				if (returned) {
					pauseTimeElapsed += System.currentTimeMillis();
					returned = false;
				}
				if (Core.getState() == ScreenState.PLAYING) {
					// TODO NIHUYA NE UDOLYAET, KOGDA PYTALSYA ZAPILIT`
					// THREASHOLD I KOMPANIYU
					CellElement tmpCell;
					Plant tmpPlant;
					for (int i = 0; i < SIZE_HORIZONTAL; i++) {
						for (int j = 0; j < SIZE_VERTICAL; j++) {
							tmpCell = CellElement.get(i, j);
							long time = System.currentTimeMillis();
							if (tmpCell instanceof Plant) {
								tmpPlant = (Plant) tmpCell;
								Core.log.println("!!"
										+ time
										+ " - "
										+ pauseTimeElapsed
										+ " - "
										+ (tmpPlant.power * 100 + tmpPlant.setUpTime)
										+ "="
										+ (time - pauseTimeElapsed
												- tmpPlant.power * 100 - tmpPlant.setUpTime)
										+ "   :  " + tmpPlant.power * 100);
								if (time - pauseTimeElapsed > tmpPlant.power
										* 100 + tmpPlant.setUpTime) {
									Core.log.println("ended up at " + i + " " //$NON-NLS-1$ //$NON-NLS-2$
											+ j);
									CellElement.set(null, i, j);
								}
							}
						}
					}
				}
				// Equivalent to
				// prevCheckedStep++;
				prevProceedStep = Updater.steps;
			}
		}
	}

	/**
	 * @author Alpen Ditrix
	 */
	public enum PlantType {
		/**
		 * It will explode at requested place
		 */
		CATCHER,
	}

	private long power;
	private long setUpTime;
	private PlantType type;

	/**
	 * Plants specified {@link Plant} in specified location with specified
	 * {@link #power}
	 * 
	 * @param t
	 *            type of {@link Plant}
	 * @param x
	 *            coordinate
	 * @param y
	 *            coordinate
	 * @param p
	 *            power
	 */
	public static void onPlant(PlantType t, int x, int y, int p) {
		if (t == PlantType.CATCHER) {
			engrow(x, y, p);
		} else {

		}
	}

	/**
	 * Called on first game start to Start {@link PlantsDestroyer}
	 */
	public static void startDestroyer() {
		new Thread(new PlantsDestroyer()).start();
	}

	private static void engrow(int x, int y, int power) {
		int size = 2;
		System.out.println("(x,y): (" + x + ", " + y + ")");
		System.out.println((x - 2) + " " + (y - 2));
		System.out.println((x + 2) + " " + (y + 2));
		// synchronized (CellElement.locker) {
		Core.log.println("Checking area");
		for (int i = x - size; i <= x + size; i++) {
			for (int j = y - size; j <= y + size; j++) {
				if (!CellElement.isEmpty(i, j)) {
					Core.log.println("Area is not empty!");
					return;
				}
			}
		}
		Core.log.println("Filling area");
		for (int i = x - size; i <= x + size; i++) {
			for (int j = y - size; j <= y + size; j++) {
				new Plant(PlantType.CATCHER, i, j, power);
			}
		}
		// }
	}

	/**
	 * @param type
	 * @param x
	 * @param y
	 * @param power
	 */
	public Plant(PlantType type, int x, int y, int power) {
		super(x, y, false);
		this.type = type;
		this.power = power;
		setUpTime = System.currentTimeMillis();
	}

	@Override
	public void draw(Graphics g) {
		switch (type) {
		case CATCHER:
			g.setColor(Color.orange);
		}
		g.fillRect(x * CELL_SIZE, y * CELL_SIZE + Painter.getTopPanelHeight(),
				CELL_SIZE, CELL_SIZE);
	}

	/**
	 * @return {@link #power}
	 */
	public long getPower() {
		return power;
	}

	/**
	 * @return {@link #type}
	 */
	public PlantType getType() {
		return type;
	}

	@Override
	public String toString() {
		return type.toString() + " at " + x + " " + y + " Set at " + setUpTime
				+ " for " + power * 100;
	}

}
