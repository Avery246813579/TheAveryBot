package com.frostbyte.theaverybot.updator;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.frostbyte.theaverybot.loading.bots.ActiveBot;
import com.frostbyte.theaverybot.loading.bots.BotLoading;

public class CheckUpdator implements Runnable{
	public CheckUpdator() {
		ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
		executor.scheduleAtFixedRate(this, 0, 5, TimeUnit.SECONDS);
	}

	public void run(){
		for(ActiveBot activeBot : BotLoading.bots){
			activeBot.getBotManager().getTriviaManager().checkStatus();
			activeBot.getBotManager().loadBotSettings();
		}
	}
}
