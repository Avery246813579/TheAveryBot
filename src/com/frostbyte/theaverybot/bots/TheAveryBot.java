package com.frostbyte.theaverybot.bots;

import com.frostbyte.theaverybot.irc.FrostBot;

public class TheAveryBot extends FrostBot{
	BotManager botManager; 
	
	public TheAveryBot(BotManager botManager, String channel, String username, String oauth) {
		super(channel, username, oauth);
		
		this.botManager = botManager;
	}

	@Override
	protected void onMessage(String channel, String sender, String login, String hostname, String message) {
		botManager.onMessage(channel, sender, message);
	}

	@Override
	protected void onBotConnect() {
	}
	
	@Override
	protected void onSubscribe(String channel, String user) {
		botManager.onSubscribe(channel, user);
	}
}
