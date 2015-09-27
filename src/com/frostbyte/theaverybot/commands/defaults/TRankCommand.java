package com.frostbyte.theaverybot.commands.defaults;

import com.frostbyte.theaverybot.bots.BotManager;
import com.frostbyte.theaverybot.commands.Command;

public class TRankCommand extends Command {
	@Override
	public void onCommand(String sender, String[] args) {
		if(args.length == 0){
			//getBotManager().getTriviaManager().getValue(sender)
		}else{

		}
	}

	public TRankCommand(BotManager botManager) {
		super(botManager, new String[] { "$trank", "$triviarank" });
	}
}
