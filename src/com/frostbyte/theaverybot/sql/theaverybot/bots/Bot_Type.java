package com.frostbyte.theaverybot.sql.theaverybot.bots;

import com.frostbyte.javasqlapi.Table;

public class Bot_Type extends Table{
	public Bot_Type() {
		super("frostbyt_theaverybot", "Bot_Type");
		variables.put("bot_id", "INT NOT NULL AUTO_INCREMENT");
		variables.put("name", "VARCHAR(30)");
		variables.put("regular_username", "VARCHAR(30)");
		variables.put("regular_oauth", "VARCHAR(100)");
		variables.put("trivia_username", "VARCHAR(30)");
		variables.put("trivia_oauth", "VARCHAR(30)");
		variables.put("public", "BOOL");
		variables.put("can_use", "TEXT");
		addPrimaryKey("bot_id");
	}
}
