package com.frostbyte.theaverybot.commands.defaults;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.frostbyte.theaverybot.bots.BotManager;
import com.frostbyte.theaverybot.commands.Command;
import com.frostbyte.theaverybot.sql.SqlHandler;
import com.frostbyte.theaverybot.util.ObjectUtil;
import com.frostbyte.theaverybot.util.PlayerUtil;

public class PointCommand extends Command {
	@Override
	public void onCommand(String sender, String[] args) {
		if(args.length == 0){
			Map<String, Object> objects = PlayerUtil.getCurrency(sender, getBotManager().channel);
			int amount = ObjectUtil.objectToInt(objects.get("points"));
			
			getBotManager().getAveryBot().sendMessage(sender + " has " + amount + " points!");
		}else{
			List<Map<String, Object>> users = SqlHandler.twitch_Accounts.get("username", args[0].toLowerCase());

			if(users.size() > 0){				
				Map<String, Object> where = new HashMap<String, Object>();
				where.put("channel", getBotManager().channel);
				where.put("twitch_id", users.get(0).get("twitch_id"));
				
				List<Map<String, Object>> currencies = SqlHandler.currencies.get(where);
				
				if(currencies.size() > 0){
					int amount = ObjectUtil.objectToInt(currencies.get(0).get("points"));
					getBotManager().getAveryBot().sendMessage(args[0].toLowerCase() + " has " + amount + " points!");
				}else{
					getBotManager().getAveryBot().sendMessage("Could not find user's points!");
				}
			}else{
				getBotManager().getAveryBot().sendMessage("Could not find user's points!");
			}
		}
	}

	public PointCommand(BotManager botManager) {
		super(botManager, new String[] { "$point", "$points" });
	}
}
