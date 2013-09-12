package com.avapir.snake.Interfaces;

import java.awt.Graphics;

/**
 * All objects of classes implemented {@code Paintable} must have visual
 * representation which will be shown by {@link #draw(Graphics)} method
 * 
 * @author Alpen Ditrix
 */
public interface Paintable {

	/**
	 * This method creates and draws into {@code g} graphical representation of
	 * the object.
	 * 
	 * @param g
	 */
	public void draw(Graphics g);

}
