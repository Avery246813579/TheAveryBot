package com.frostbyte.theaverybot.bots.chance;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.frostbyte.theaverybot.bots.BotManager;
import com.frostbyte.theaverybot.games.chance.Chance;
import com.frostbyte.theaverybot.games.chance.ChanceHandler;
import com.frostbyte.theaverybot.games.chance.ChanceResponse;
import com.frostbyte.theaverybot.sql.SqlHandler;
import com.frostbyte.theaverybot.util.ObjectUtil;

public class ChanceManager {
	private Map<String, Integer> credits = new LinkedHashMap<String, Integer>();
	private int delay = 5, type_id = 1, second = 0;
	private boolean enabled, reset;
	private BotManager botManager;
	private ChanceBot chanceBot;

	public ChanceManager(BotManager botManager, int type_id) {
		this.type_id = type_id;
		this.botManager = botManager;

		loadBot();
		checkStatus();
		resetChance();
		loadCredits();
	}

	public void update(){
		if(enabled){
			if(!reset){
				second--;
				
				if(second <= 0){
					reset = true;
				}
			}
		}
	}
	
	public void resetChance() {
		second = -delay;
	}

	public void stopChance(){
		chanceBot.running = false;
	}
	
	public void loadBot() {
		boolean found = true;
		if (ChanceHandler.chances.get(type_id) == null) {
			this.type_id = 1;
			found = false;
		}

		Chance chance = ChanceHandler.chances.get(type_id);
		chanceBot = new ChanceBot(this, botManager.channel, chance.getUsername(), chance.getOauth());

		if (!found) {
			chanceBot.sendMessage("Could not find Selected Chance Bot! Setting to default!");
		}
	}

	public void checkStatus() {
		try {
			/** Declares Player Finder **/
			Map<String, Object> playerFinders = new HashMap<String, Object>();
			playerFinders.put("account_id", botManager.getAccount_id());

			/** Checks Trivia Information **/
			List<Map<String, Object>> chanceInfo = SqlHandler.chance_Settings.get(playerFinders);
			if (chanceInfo.isEmpty()) {
				Map<String, Object> chanceCreate = new HashMap<String, Object>();
				chanceCreate.put("account_id", botManager.getAccount_id());
				chanceCreate.put("active", 0);
				chanceCreate.put("type", 1);
				chanceCreate.put("delay", 5);
				SqlHandler.chance_Settings.create(chanceCreate);

				chanceInfo = SqlHandler.chance_Settings.get(playerFinders);
			}

			/** Sets Trivia Information **/
			if (enabled != (boolean) chanceInfo.get(0).get("active")) {
				enabled = (boolean) chanceInfo.get(0).get("active");
				resetChance();

				if (enabled) {
					chanceBot.sendMessage(ChanceHandler.chances.get(type_id).getMessages().get("JOIN"));
				} else {
					chanceBot.sendMessage(ChanceHandler.chances.get(type_id).getMessages().get("LEAVE"));
				}
			}

			delay = ObjectUtil.objectToInt(chanceInfo.get(0).get("delay"));
		} catch (Exception ex) {
			chanceBot.sendMessage("Error enabling Chance! Setting to default!");
			ex.printStackTrace();
		}
	}

	public void loadCredits() {
		Map<String, Object> where = new HashMap<String, Object>();
		where.put("channel", botManager.channel);
		for (Map<String, Object> users : SqlHandler.currencies.get(where)) {
			List<Map<String, Object>> tables = SqlHandler.twitch_Accounts.get("twitch_id",
					ObjectUtil.objectToInt(users.get("twitch_id")));

			if (tables.size() <= 0) {
				Map<String, Object> table = new HashMap<String, Object>();
				table.put("twitch_id", ObjectUtil.objectToInt(users.get("twitch_id")));
				SqlHandler.twitch_Accounts.create(table);

				tables = SqlHandler.twitch_Accounts.get("twitch_id", ObjectUtil.objectToInt(users.get("twitch_id")));
			}

			credits.put(ObjectUtil.objectToString(tables.get(0).get("username")),
					ObjectUtil.objectToInt(users.get("credits")));
		}

		sortCredits();
	}

	public void sortCredits() {
		List<Map.Entry<String, Integer>> entries = new LinkedList<Map.Entry<String, Integer>>(credits.entrySet());

		Collections.sort(entries, new Comparator<Map.Entry<String, Integer>>() {
			public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
				return o2.getValue().compareTo(o1.getValue());
			}
		});

		credits.clear();

		for (Map.Entry<String, Integer> entry : entries) {
			credits.put(entry.getKey(), entry.getValue());
		}
	}

	public void onMessage(String channel, String sender, String login, String hostname, String message) {
		if(reset && enabled && message.split(" ")[0].equalsIgnoreCase("$" + ChanceHandler.chances.get(type_id).getStart())){
			giveResponse(sender);
		}
	}
	
	public void giveResponse(String user){
		ChanceResponse response = ChanceHandler.chances.get(type_id).randomResponse();
		String reply = response.getReply();
		
		if(reply.contains("{PLAYER_NAME}")){
			reply = reply.replaceAll("\\{PLAYER_NAME\\}", user);
		}

		
		
		chanceBot.sendMessage(reply);
	}
}
