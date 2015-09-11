package com.frostbyte.theaverybot.loading;

import com.frostbyte.theaverybot.loading.bots.BotLoading;

public class Loader {
	private boolean running;
	private LoadThread loadThread = new LoadThread(this);
	private BotLoading botLoading = new BotLoading();
	
	public Loader(){
		start();
		loadThread.start();
	}
	
	public void start(){
		running = true;
	}
	
	public void stop(){
		running = false;
	}
	
	public void tick(){}
	
	public void update(){
		botLoading.update();
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	public LoadThread getLoadThread() {
		return loadThread;
	}

	public void setLoadThread(LoadThread loadThread) {
		this.loadThread = loadThread;
	}
}
