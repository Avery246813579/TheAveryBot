package com.frostbyte.theaverybot.bots;

public class BotThread extends Thread implements Runnable {
	private BotManager botManager;
	private int FPS = 30, FRAME = 0;
	private long targetTime = 1000 / FPS;
	private long SECOND_START;

	public BotThread(BotManager botManager) {
		this.botManager = botManager;
	}

	public void run() {
		long start = 0;
		long elapsed;
		long wait;

		while (botManager.isEnabled()) {
			synchronized (this) {
				FRAME++;

				if (FRAME >= (FPS)) {
					FRAME = 0;
				}

				int SECOND = Math.round(((System.nanoTime() / 1000000000L) - (SECOND_START / 1000000000L)));
				if (SECOND >= 1) {
					SECOND_START = System.nanoTime();
					botManager.update();
				}

				elapsed = System.nanoTime() - start;
				wait = targetTime - elapsed / 1000000;

				if (wait < 0) {
					wait = 5;
				}

				try {
					Thread.sleep(wait);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
	}
}
