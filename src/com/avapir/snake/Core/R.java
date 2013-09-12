package com.avapir.snake.Core;

import java.io.IOException;

/**
 * It's the main class of application, which loads first.
 * 
 * This class is made for , which is to load preferences from file or, if it
 * isn't exist, or to load them from default values from each class. When
 * preferences and properties are gathered, it will start new {@link Core}
 * thread.
 * 
 * After finishing game it will also save preferences into new file, or rewrite
 * old, if exists.
 * 
 * Also it must have manual exit, because there is {@code while(true)} cycle in
 * {@link Painter} which must be stopped and I don't want to catch some
 * exceptions.
 * 
 * @author Alpen Ditrix
 * 
 */
public class R {

	/**
	 * Main application thread
	 */
	public static Thread core = new Thread(new Core());

	/**
	 * Exit code. If it's not 0 or -1, then application exited with some error
	 * or warning
	 */
	public static int exitCode = -1;

	/**
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		SettingsReciever.recieveSettings();
		core.start();
		try {
			core.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("211313");
		SettingsReciever.rewriteSettings();
		System.exit(exitCode);
	}
}
