package com.avapir.snake.Interfaces;

/**
 * All objects of classes, implemented {@code Clickable} must have some reactin
 * on some clicking (I think - clicking on it, so they also must have visual
 * representation on the screen)
 * 
 * @author Alpen Ditrix
 * 
 */
public interface Clickable {

	/**
	 * This method may be called when necessary conditions was meet after some
	 * clicking
	 */
	public void onClick();

}
