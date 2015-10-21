package com.frostbyte.theaverybot.loading.bots;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.frostbyte.theaverybot.sql.SqlHandler;
import com.frostbyte.theaverybot.util.ObjectUtil;

public class BotLoading {
	public static List<ActiveBot> bots = new ArrayList<ActiveBot>();
	
	public void update(){
		List<Map<String, Object>> tables = SqlHandler.byte_Bots.get();
		
		List<ActiveBot> activeBots = new ArrayList<ActiveBot>();
		for(Map<String, Object> table : tables){
			ActiveBot activeBot = get(ObjectUtil.objectToInt(table.get("account_id")));
			
			if(activeBot == null){
				ActiveBot botActive = new ActiveBot(ObjectUtil.objectToInt(table.get("byte_id")), ObjectUtil.objectToInt(table.get("account_id")));
				bots.add(botActive);
				activeBots.add(botActive);
			}else{
				activeBots.add(activeBot);
			}
		}
		
		for(ActiveBot activeBot : bots){
			if(!activeBots.contains(activeBot)){
				activeBot.getBotManager().shutdown();
				bots.remove(activeBot);
				break;
			}
		}
	}
	
	public ActiveBot get(int acccount_id){
		for(ActiveBot activeBot : bots){
			if(activeBot.getAccount_id() == acccount_id){
				return activeBot;
			}
		}
		
		return null;
	}
}