package com.frostbyte.theaverybot.commands.defaults;

import com.frostbyte.theaverybot.bots.BotManager;
import com.frostbyte.theaverybot.commands.Command;

public class DeveloperCommand extends Command{

	public DeveloperCommand(BotManager botManager) {
		super(botManager, new String[]{"$dev", "$developer"});
	}

	@Override
	public void onCommand(String sender, String[] args) {
		getBotManager().getAveryBot().sendMessage("The developer of TheAveryBot is Avery246813579. Twitter: https://twitter.com/Avery246813579 Twitch: http://www.twitch.tv/avery246813579");
	}
}
