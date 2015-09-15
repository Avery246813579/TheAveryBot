package com.frostbyte.theaverybot.commands.defaults;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.frostbyte.theaverybot.bots.BotManager;
import com.frostbyte.theaverybot.commands.Command;
import com.frostbyte.theaverybot.sql.SqlHandler;
import com.frostbyte.theaverybot.util.ObjectUtil;
import com.frostbyte.theaverybot.util.PlayerUtil;

public class CreditCommand extends Command {
	@Override
	public void onCommand(String sender, String[] args) {
		if(args.length == 0){
			Map<String, Object> objects = PlayerUtil.getCurrency(sender, getBotManager().channel);
			int amount = ObjectUtil.objectToInt(objects.get("credits"));
			
			getBotManager().getAveryBot().sendMessage(sender + " has " + amount + " credits!");
		}else{
			List<Map<String, Object>> users = SqlHandler.twitch_Accounts.get("username", args[0].toLowerCase());

			if(users.size() > 0){				
				Map<String, Object> where = new HashMap<String, Object>();
				where.put("channel", getBotManager().channel);
				where.put("twitch_id", users.get(0).get("twitch_id"));
				
				List<Map<String, Object>> currencies = SqlHandler.currencies.get(where);
				
				if(currencies.size() > 0){
					int amount = ObjectUtil.objectToInt(currencies.get(0).get("credits"));
					getBotManager().getAveryBot().sendMessage(args[0].toLowerCase() + " has " + amount + " credits!");
				}else{
					getBotManager().getAveryBot().sendMessage("Could not find user's credits!");
				}
			}else{
				getBotManager().getAveryBot().sendMessage("Could not find user's credits!");
			}
		}
	}

	public CreditCommand(BotManager botManager) {
		super(botManager, new String[] { "$credit", "$credits" });
	}
}
