package com.frostbyte.theaverybot.commands.defaults.trivia;

import com.frostbyte.theaverybot.bots.BotManager;
import com.frostbyte.theaverybot.commands.Command;

public class TriviaCommand extends Command{
	public TriviaCommand(BotManager botManager) {
		super(botManager, "$trivia");
	}

	@Override
	public void onCommand(String sender, String[] args) {
		if(args.length == 0){
			getBotManager().getAveryBot().sendMessage("Usage: $Trivia (Type|Version|Question)");
		}
	}
}
