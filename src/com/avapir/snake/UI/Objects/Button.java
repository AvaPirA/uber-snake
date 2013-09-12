package com.avapir.snake.UI.Objects;

import java.awt.Graphics;
import java.awt.Image;

import javax.imageio.ImageIO;

import com.avapir.snake.Interfaces.Clickable;

/**
 * Abstract class created to add react on press for simple {@link Sprite}s
 * 
 * @author Alpen Ditrix
 */
public abstract class Button extends Sprite implements Clickable {

	/**
	 * Is this button blured by mouse now?
	 */
	boolean blured = false;

	/**
	 * is this button pressed by mouse now?
	 */
	boolean pressed = false;

	/**
	 * This image will shown then this button is blured
	 */
	private Image bluredImage;

	/**
	 * This image will shown then this button is pressed
	 */
	private Image pressedImage;

	/**
	 * Default constructor. It loads "default", "blured" and "pressed" images
	 * and saves where button must be placed on the canvas (coordinates in
	 * pixels)
	 * 
	 * @param path
	 *            in-jar path to image file into /res file. It means to acces
	 *            file into {@code "YourJARFile.jar\\res/pics/pic.png"} you need
	 *            to type here path <dd>{@code "/pics/pic.png"}
	 * @param nx1
	 *            upper left corner X position
	 * @param ny1
	 *            upper left corner Y position
	 */
	public Button(String path, int nx1, int ny1) {
		super(path, nx1, ny1);
		String bluredPath = path.substring(0, path.indexOf("."))
				.concat("-blured").concat(path.substring(path.indexOf(".")));
		String pressedPath = path.substring(0, path.indexOf("."))
				.concat("-pressed").concat(path.substring(path.indexOf(".")));
		try {
			bluredImage = ImageIO.read(getClass().getResourceAsStream(
					bluredPath));
		} catch (Exception e) {
			System.err.println("Wrong bluredPath: " + bluredPath);
		}
		try {
			pressedImage = ImageIO.read(getClass().getResourceAsStream(
					pressedPath));
		} catch (Exception e) {
			System.err.println("Wrong pressedPath: " + pressedPath);
		}
	}

	/**
	 * Invoked when button is blured. Sets specified marker "true"
	 * 
	 * @return this button
	 */
	public Button blur() {
		blured = true;
		pressed = false;
		return this;
	}

	/**
	 * Draws specified by markers image
	 */
	@Override
	public void draw(Graphics g) {
		if (blured) {
			// super.draw(g, "b");
			if (pressed) {
				super.draw(g, pressedImage);
				// super.draw(g, "p");
			} else {
				super.draw(g, bluredImage);
			}
		} else
			super.draw(g);
		// super.draw(g, getName());
	}

	/**
	 * Check if given (by coordinates) dot lies within button area (defined by
	 * spline rectangle)
	 * 
	 * @param nx1
	 *            X dot position
	 * @param ny1
	 *            Y dot position
	 * @return Is it goals?
	 */
	public boolean goal(int nx1, int ny1) {
		return nx1 >= x1 && nx1 <= x2 && ny1 >= y1 && ny1 <= y2;
	}

	/**
	 * @return Is this button blured?
	 */
	public boolean isBlured() {
		return blured;
	}

	/**
	 * Method must be called on button`s click
	 */
	@Override
	public abstract void onClick();

	/**
	 * Invoked when button has been pressed byt mouse. Sets specified marker
	 * "true"
	 * 
	 * @return this button
	 */
	public Button press() {
		pressed = true;
		return this;
	}

	/**
	 * Invoked when button has been pressed byt mouse. Sets specified marker
	 * "true"
	 * 
	 * @return this button
	 */
	public Button release() {
		pressed = false;
		return this;
	}

	@Override
	public String toString() {
		return super.getName();
	}

	/**
	 * Invoked when button is blured. Sets specified marker "true"
	 * 
	 * @return this button
	 */
	public Button unblur() {
		blured = false;
		return this;
	}
}
