package com.avapir.snake.Networking;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Vector;

import com.avapir.snake.Actors.PC.Direction;
import com.avapir.snake.Actors.PC.Snake;
import com.avapir.snake.Core.Core;
import com.avapir.snake.UI.Game;

@SuppressWarnings("javadoc")
public class HostHandler implements Runnable {
	private static final int DEFAULT_PORT = 64961;
	private static final int PACKAGE_SIZE = 128;

	private static HashMap<InetAddress, Snake> playersSnakes = new HashMap<InetAddress, Snake>();
	private static ServerSocketChannel serverChannel = null;
	private static Vector<SocketChannel> playersChannels = new Vector<SocketChannel>();
	private static PrintWriter log = Core.log;

	public static void addPlayer(InetAddress address, Snake snake) {
		playersSnakes.put(address, snake);
	}

	// XXX: it's some alpha version: we cannot connect without host binding
	// place for us
	public static void initializate() {

		try {
			serverChannel = ServerSocketChannel.open();
			serverChannel.socket().bind(new InetSocketAddress(DEFAULT_PORT));
		} catch (IOException e) {
			System.err.println("Free the port!!");
			System.exit(-1);
		}

		for (Snake playerSnake : playersSnakes.values()) {
			try {
				SocketChannel tmp = serverChannel.accept();
				tmp.configureBlocking(false);
				playersChannels.add(tmp);

				ObjectOutputStream sender = new ObjectOutputStream(tmp.socket()
						.getOutputStream());
				sender.writeObject(Game.getSnakes());
				sender.writeObject(new Integer(Game.getSnakes().indexOf(
						playerSnake)));
				sender.close();
				log.println("New player connected! "
						+ tmp.socket().getInetAddress().toString());
			} catch (IOException e) {
				System.err.println("lolwut?! Can't accept");
				System.exit(-1);
			}
		}
	}

	@Override
	public void run() {
		ByteBuffer recievedInfo = ByteBuffer.allocate(PACKAGE_SIZE);
		ByteBuffer sendInfo = ByteBuffer.allocate(PACKAGE_SIZE);
		while (Core.getState() == Core.ScreenState.PLAYING) {
			recievedInfo.clear();
			sendInfo.clear();
			Vector<Direction> directions = Game.getDirections();
			for (Direction d : directions) {
				sendInfo.putInt(d.toInt());
			}
			for (SocketChannel playerChannel : playersChannels) {
				try {
					if (playerChannel.read(recievedInfo) != 0) {
						playersSnakes
								.get(playerChannel.socket().getInetAddress())
								.setDirection(
										Direction.fromInt(recievedInfo.getInt()));
					}
					playerChannel.write(sendInfo);
				} catch (IOException e) {
					System.err.println("Something got really wrong!");
					System.exit(-1);
				}
			}
		}
	}
}
