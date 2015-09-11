package com.frostbyte.theaverybot.commands.defaults;

import com.frostbyte.theaverybot.bots.BotManager;
import com.frostbyte.theaverybot.commands.Command;

public class PointCommand extends Command {
	@Override
	public void onCommand(String sender, String[] args) {
		if(args.length > 0){
			
		}
	}

	public PointCommand(BotManager botManager) {
		super(botManager, new String[] { "point", "points" });
	}
}
