package com.frostbyte.theaverybot.irc.threads;

import java.io.BufferedWriter;

import com.frostbyte.theaverybot.irc.Irc;
import com.frostbyte.theaverybot.irc.server.IQueue;

public class OutputThread extends Thread {
	private Irc irc = null;
	private IQueue queue = null;

	public OutputThread(Irc irc, IQueue queue) {
		this.irc = irc;
		this.queue = queue;
	}

	public static void sendRawLine(Irc irc, BufferedWriter bufferedWriter, String string) {
		if (string.length() > irc.getMaxLineLength() - 2) {
			string = string.substring(0, irc.getMaxLineLength() - 2);
		}

		synchronized (bufferedWriter) {
			try {
				bufferedWriter.write(string + "\r\n");
				bufferedWriter.flush();
				irc.log(">>>" + string);
			} catch (Exception ex) {
				throw new InternalError("Internal Error in Bot Output");
			}
		}
	}

	public void run() {
		while (irc.running) {
			try {
				int i = 1;
				while (i != 0 && irc.running) {
					Thread.sleep(irc.getMessageDelay());

					String string = (String) queue.next();
					if (string != null) {
						irc.sendRawLine(string);
					} else {
						i = 0;
					}
				}
			} catch (Exception ex) {
				throw new InternalError("Internal Error in Bot Output");
			}
		}
	}
}
