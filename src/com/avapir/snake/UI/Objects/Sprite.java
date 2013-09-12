package com.avapir.snake.UI.Objects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.PrintWriter;

import javax.imageio.ImageIO;

import com.avapir.snake.Core.Core;

/**
 * Representation of simple pictures with fixed position, which is given at
 * creation
 * 
 * @author Alpen Ditrix
 */
public class Sprite {

	/**
	 * System logger link
	 */
	private static PrintWriter log = Core.log;

	/**
	 * Spline rectangle upper left and lower right corners
	 */
	int x1, y1, x2, y2;

	/**
	 * Regular spline`s image
	 */
	private Image image;

	/**
	 * Spline`s name created from source image filename
	 */
	private String name;

	/**
	 * Default constructor creates spline by path to image, which converts to
	 * spline`s {@code name} and upper left corner coordinates
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
	public Sprite(String path, int nx1, int ny1) {
		try {
			image = ImageIO.read(getClass().getResourceAsStream(path));
		} catch (Exception e) {
			e.printStackTrace();
			log.println("Wrong path: " + path);

		}

		name = path.substring(path.lastIndexOf("/") + 1, path.indexOf("."));

		x1 = nx1 - getWidth() / 2;
		y1 = ny1 - getHeigth() / 2;
		y2 = y1 + getHeigth();
		x2 = x1 + getWidth();
	}

	/**
	 * Simply draws attached image
	 * 
	 * @param g
	 */
	public void draw(Graphics g) {
		draw(g, image);
	}

	/**
	 * Method specially for buttons draws some replacing image: "blured" or
	 * "pressed"
	 * 
	 * @param g
	 * @param replacingImage
	 */
	public void draw(Graphics g, Image replacingImage) {
		g.drawImage(replacingImage, x1, y1, null);
	}

	/**
	 * Custom position drawing
	 * 
	 * @param g
	 * @param x
	 * @param y
	 */
	public void draw(Graphics g, int x, int y) {
		g.drawImage(image, x, y, null);
	}

	/**
	 * Draws spline with string {@code title} over it
	 * 
	 * @deprecated Debug usage only
	 * @param g
	 * @param title
	 */
	@Deprecated
	public void draw(Graphics g, String title) {
		g.setColor(Color.red);
		g.drawString(title, x1, y1);
	}

	/**
	 * @return source image height in pixels
	 */
	public int getHeigth() {
		return image.getHeight(null);
	}

	/**
	 * @return given picture name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return source image width in pixels
	 */
	public int getWidth() {
		return image.getWidth(null);
	}

	/**
	 * @return upper left corner X position
	 */
	public int getX1() {
		return x1;
	}

	/**
	 * @return lower right corner X position
	 */
	public int getX2() {
		return x2;
	}

	/**
	 * @return upper left corner Y position
	 */
	public int getY1() {
		return y1;
	}

	/**
	 * @return lower right corner Y position
	 */
	public int getY2() {
		return y2;
	}

	/**
	 * Allows set your own name for button
	 * 
	 * @param n
	 *            new name
	 */
	public void rename(String n) {
		name = n;
	}

}