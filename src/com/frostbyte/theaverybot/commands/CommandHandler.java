package com.frostbyte.theaverybot.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.frostbyte.theaverybot.bots.BotManager;
import com.frostbyte.theaverybot.commands.defaults.DeveloperCommand;
import com.frostbyte.theaverybot.commands.defaults.PointCommand;
import com.frostbyte.theaverybot.sql.SqlHandler;
import com.frostbyte.theaverybot.util.ObjectUtil;

public class CommandHandler {
	private List<Command> commands = new ArrayList<Command>();
	
	public CommandHandler(final BotManager botManager) {
		/** Adds Default Commands **/
		commands.add(new PointCommand(botManager));
		commands.add(new DeveloperCommand(botManager));
		
		for(final Map<String, Object> commands : SqlHandler.commands.get("account_id", botManager.getAccount_id())){
			this.commands.add(new Command(botManager, ObjectUtil.objectToString(commands.get("command"))) {
				
				@Override
				public void onCommand(String sender, String[] args) {
					botManager.getAveryBot().sendMessage(ObjectUtil.objectToString(commands.get("content")));
				}
			});
		}
	}

	public void onMessage(String sender, String longCommand) {
		try {
			for (Command command : commands) {
				if (command.getCommands() != null) {
					for(String string: command.getCommands()){
						if (longCommand.split(" ")[0].equalsIgnoreCase(string)) {
							command.onCommand(sender, Arrays.copyOfRange(longCommand.split(" "), 1, longCommand.split(" ").length));
						}
					}
				} else {
					if (longCommand.split(" ")[0].equalsIgnoreCase(command.getCommand())) {
						command.onCommand(sender, Arrays.copyOfRange(longCommand.split(" "), 1, longCommand.split(" ").length));
					}
				}
			}
		} catch (Exception ex) {

		}
	}
}
