package com.frostbyte.theaverybot.bots.chance;

import com.frostbyte.theaverybot.irc.FrostBot;

public class ChanceBot extends FrostBot{
	ChanceManager chanceManager;
	
	public ChanceBot(ChanceManager chanceManager, String channel, String username, String oauth) {
		super(channel, username, oauth);
		
		this.chanceManager = chanceManager;
	}

	@Override
	protected void onMessage(String channel, String sender, String login, String hostname, String message) {
		chanceManager.onMessage(channel, sender, login, hostname, message);
	}

	@Override
	protected void onBotConnect() {
	}
}
