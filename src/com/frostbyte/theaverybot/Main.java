package com.frostbyte.theaverybot;

import com.frostbyte.theaverybot.debug.Console;
import com.frostbyte.theaverybot.games.trivia.TriviaHandler;
import com.frostbyte.theaverybot.loading.Loader;
import com.frostbyte.theaverybot.sql.SqlHandler;
import com.frostbyte.theaverybot.updator.CheckUpdator;
import com.frostbyte.theaverybot.updator.UpdateManager;
import com.frostbyte.theaverybot.util.CloseThread;

public class Main {
	public static String version = "v1.0.0";
	
	public static void main(String[] args) {
		new SqlHandler();
		new TriviaHandler();
		new Loader();
		
		new UpdateManager();
		new CheckUpdator();
		
		Runtime.getRuntime().addShutdownHook(new CloseThread());
		
		new Console().start();
	}
}
