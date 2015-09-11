package com.frostbyte.theaverybot.loading;

public class LoadThread extends Thread implements Runnable{
	private Loader botLoader;
	private int FPS = 30, FRAME = 0;
	private long targetTime = 1000 / FPS;
	
	
	public LoadThread(Loader botLoader){
		this.botLoader = botLoader;
	}
	
	public void run() {
		long start = 0;
		long elapsed;
		long wait;
		
		while(botLoader.isRunning()){
			synchronized (this) {
				FRAME++;
				botLoader.tick();
				
				if(FRAME >= (FPS * 5)){
					FRAME = 0;
					botLoader.update();
				}
				
				elapsed = System.nanoTime() - start;
				wait = targetTime - elapsed / 1000000;
				
				if(wait < 0){
					wait = 5;
				}
				
				try{
					Thread.sleep(wait);
				}catch(Exception ex){
					ex.printStackTrace();
				}
			}
		}
	}
}
