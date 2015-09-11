package com.frostbyte.theaverybot.commands;

import com.frostbyte.theaverybot.bots.BotManager;

public abstract class Command {
	private String command; 
	private String[] commands;
	private BotManager botManager;
	
	protected Command(BotManager botManager, String command) {
		this.botManager = botManager;
		this.command = command;
	}
	
	protected Command(BotManager botManager, String[] commands){
		this.botManager = botManager;
		this.setCommands(commands);
	}
	
	public abstract void onCommand(String sender, String[] args);
	
	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String[] getCommands() {
		return commands;
	}

	public void setCommands(String[] commands) {
		this.commands = commands;
	}

	public BotManager getBotManager() {
		return botManager;
	}

	public void setBotManager(BotManager botManager) {
		this.botManager = botManager;
	}
}
