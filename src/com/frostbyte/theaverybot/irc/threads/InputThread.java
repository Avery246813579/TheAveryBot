package com.frostbyte.theaverybot.irc.threads;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.net.Socket;

import com.frostbyte.theaverybot.irc.Irc;

public class InputThread extends Thread {
	private Irc irc;
	private Socket socket = null;
	private BufferedReader bufferedReader = null;
	private BufferedWriter bufferedWriter = null;
	private boolean connected = true, disposed = false;
	public static final int MAX_LINE_LENGTH = 512;

	public InputThread(Irc irc, Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
		this.irc = irc;
		this.socket = socket;
		this.bufferedReader = bufferedReader;
		this.bufferedWriter = bufferedWriter;
		setName(getClass() + "-Thread");
	}

	public void sendRawLine(String rawLine) {
		OutputThread.sendRawLine(irc, bufferedWriter, rawLine);
	}

	public void run() {
		while (irc.running) {
			try {
				int i = 1;

				while (i != 0 && irc.running) {
					try {
						String string = null;

						while ((string = bufferedReader.readLine()) != null && irc.running) {
							try {
								irc.handleLine(string);
							} catch (Exception exception) {
								exception.printStackTrace();
								throw new InternalError("Error inside Input Thread!");
							}
						}

						if (string == null) {
							i = 0;
						}
					} catch (Exception exception) {
						sendRawLine("PING " + System.currentTimeMillis() / 1000L);
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			try {
				socket.close();
			} catch (Exception exception) {
				exception.printStackTrace();
			}

			if (!disposed) {
				irc.log("Bot has been disconnected!");
				connected = false;
				irc.onDisconnect();
			}
		}
	}

	public void dispose() {
		try {
			disposed = true;
			socket.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public Irc getBot() {
		return irc;
	}

	public void setBot(Irc bot) {
		this.irc = bot;
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public BufferedReader getBufferedReader() {
		return bufferedReader;
	}

	public void setBufferedReader(BufferedReader bufferedReader) {
		this.bufferedReader = bufferedReader;
	}

	public BufferedWriter getBufferedWriter() {
		return bufferedWriter;
	}

	public void setBufferedWriter(BufferedWriter bufferedWriter) {
		this.bufferedWriter = bufferedWriter;
	}

	public boolean isConnected() {
		return connected;
	}

	public void setConnected(boolean connected) {
		this.connected = connected;
	}

	public boolean isDisposed() {
		return disposed;
	}

	public void setDisposed(boolean disposed) {
		this.disposed = disposed;
	}
}
