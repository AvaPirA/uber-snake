package com.avapir.snake.Networking;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Vector;

import com.avapir.snake.Actors.PC.Direction;
import com.avapir.snake.Actors.PC.Snake;
import com.avapir.snake.Core.Core;
import com.avapir.snake.UI.Game;

@SuppressWarnings("javadoc")
public class ClientHandler implements Runnable {

	private static final int DEFAULT_PORT = 64961;
	private static final int PACKAGE_SIZE = 48;
	private static SocketChannel hostChannel = null;

	@SuppressWarnings("unchecked")
	public static void initializate(InetAddress hostAddress) {
		try {
			hostChannel = SocketChannel.open();
			hostChannel
					.connect(new InetSocketAddress(hostAddress, DEFAULT_PORT));
			ObjectInputStream reciever = new ObjectInputStream(hostChannel
					.socket().getInputStream());
			Game.setSnakes((Vector<Snake>) reciever.readObject());
			Game.setMySnake_ID((Integer) reciever.readObject());
			hostChannel.configureBlocking(false);
		} catch (IOException e) {
			System.err.println("Free the port!!");
			System.exit(-1);
		} catch (ClassNotFoundException e) {
			System.err
					.println("Network is broken and I don't want to throw Runtime");
			System.exit(-1);
		}
	}

	@Override
	public void run() {
		ByteBuffer recievedInfo = ByteBuffer.allocate(PACKAGE_SIZE);
		ByteBuffer sendInfo = ByteBuffer.allocate(PACKAGE_SIZE);
		while (Core.getState() == Core.ScreenState.PLAYING) {
			recievedInfo.clear();
			sendInfo.clear();
			sendInfo.putInt(Game.getSnakes().get(Game.getMySnake_ID())
					.getDirection().toInt());
			try {
				if (hostChannel.read(recievedInfo) != 0) {
					for (Snake s : Game.getSnakes()) {
						s.setDirection(Direction.fromInt(recievedInfo.getInt()));
					}
				}
				hostChannel.write(sendInfo);
			} catch (IOException e) {
				System.err.println("Something got really wrong!");
				System.exit(-1);
			}
		}
	}
}
