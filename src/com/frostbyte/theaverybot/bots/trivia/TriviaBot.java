package com.frostbyte.theaverybot.bots.trivia;

import com.frostbyte.theaverybot.irc.FrostBot;

public class TriviaBot extends FrostBot{
	TriviaManager triviaManager;
	
	public TriviaBot(TriviaManager triviaManager, String channel, String username, String oauth) {
		super(channel, username, oauth);
		
		this.triviaManager = triviaManager;
	}

	@Override
	protected void onMessage(String channel, String sender, String login, String hostname, String message) {
		triviaManager.onMessage(channel, sender, login, hostname, message);
	}

	@Override
	protected void onBotConnect() {
	}
}
