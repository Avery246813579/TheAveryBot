package com.frostbyte.theaverybot.bots;

import java.util.Map;

import com.frostbyte.theaverybot.bots.trivia.TriviaManager;
import com.frostbyte.theaverybot.commands.CommandHandler;
import com.frostbyte.theaverybot.loading.bots.ActiveBot;
import com.frostbyte.theaverybot.sql.SqlHandler;
import com.frostbyte.theaverybot.util.ObjectUtil;

public class BotManager {
	private BotThread botThread;
	public String channel, regular_username, regular_oauth, trivia_username, trivia_oauth;
	private boolean enabled = false;
	private TriviaManager triviaManager;
	private CommandHandler commandHandler;
	private TheAveryBot averyBot;
	private int account_id;

	public BotManager(ActiveBot activeBot) {
		channel = ObjectUtil.objectToString(SqlHandler.twitch_Accounts.get("account_id", ObjectUtil.intToObject(activeBot.getAccount_id())).get(0).get("username"));
		account_id = activeBot.getAccount_id();
		
		Map<String, Object> table = SqlHandler.bot_Type.get("bot_id", ObjectUtil.intToObject(activeBot.getBot_id())).get(0);
		trivia_username = ObjectUtil.objectToString(table.get("trivia_username"));
		trivia_oauth = ObjectUtil.objectToString(table.get("trivia_oauth"));
		regular_username = ObjectUtil.objectToString(table.get("regular_username"));
		regular_oauth = ObjectUtil.objectToString(table.get("regular_oauth"));
		
		this.enabled = true;
		
		this.averyBot = new TheAveryBot(this, channel, regular_username, regular_oauth);
		
		triviaManager = new TriviaManager(this);
		commandHandler = new CommandHandler(this);
		
		botThread = new BotThread(this);
		botThread.start();
	}
	
	public void shutdown(){
		enabled = false;
		triviaManager.stop();
	}

	public void update() {
		if (triviaManager != null) {
			triviaManager.update();
		}
	}
	
	public void onMessage(String channel, String sender, String message) {
		commandHandler.onMessage(sender, message);
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public TriviaManager getTriviaManager() {
		return triviaManager;
	}

	public void setTriviaManager(TriviaManager triviaManager) {
		this.triviaManager = triviaManager;
	}

	public CommandHandler getCommandHandler() {
		return commandHandler;
	}

	public void setCommandHandler(CommandHandler commandHandler) {
		this.commandHandler = commandHandler;
	}

	public TheAveryBot getAveryBot() {
		return averyBot;
	}

	public void setAveryBot(TheAveryBot averyBot) {
		this.averyBot = averyBot;
	}

	public int getAccount_id() {
		return account_id;
	}

	public void setAccount_id(int account_id) {
		this.account_id = account_id;
	}
}
