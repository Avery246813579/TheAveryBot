package com.frostbyte.theaverybot.sql.theaverybot.bots;

import com.frostbyte.javasqlapi.Table;

public class Bot_Settings extends Table{
	public Bot_Settings() {
		super("frostbyt_theaverybot", "Bot_Settings");
		variables.put("settings_id", "INT NOT NULL AUTO_INCREMENT");
		variables.put("account_id", "INT");
		variables.put("bot_type", "INT");
		variables.put("messages", "TEXT");
		addPrimaryKey("settings_id");
	}
}
