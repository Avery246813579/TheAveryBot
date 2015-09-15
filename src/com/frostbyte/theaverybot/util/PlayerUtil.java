package com.frostbyte.theaverybot.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.frostbyte.theaverybot.sql.SqlHandler;

public class PlayerUtil {
	public static Map<String, Object> getUser(String username) {
		List<Map<String, Object>> tables = SqlHandler.twitch_Accounts.get("username", username.toLowerCase());

		if (tables.size() <= 0) {
			Map<String, Object> table = new HashMap<String, Object>();
			table.put("username", username.toLowerCase());
			SqlHandler.twitch_Accounts.create(table);

			tables = SqlHandler.twitch_Accounts.get("username", username.toLowerCase());
		}

		for (Map<String, Object> table : tables) {
			return table;
		}

		return null;
	}

	public static Map<String, Object> getCurrency(String username, String channel) {
		Map<String, Object> user = getUser(username.toLowerCase());

		Map<String, Object> playerInput = new HashMap<String, Object>();
		playerInput.put("twitch_id", ObjectUtil.objectToInt(user.get("twitch_id")));
		playerInput.put("channel", channel.toLowerCase());
		List<Map<String, Object>> tables = SqlHandler.currencies.get(playerInput);

		if (tables.size() <= 0) {
			Map<String, Object> table = new HashMap<String, Object>();
			table.put("twitch_id", user.get("twitch_id"));
			table.put("channel", channel.toLowerCase());
			SqlHandler.currencies.create(table);

			tables = SqlHandler.currencies.get(playerInput);
		}

		for (Map<String, Object> table : tables) {
			return table;
		}

		return null;
	}
}


