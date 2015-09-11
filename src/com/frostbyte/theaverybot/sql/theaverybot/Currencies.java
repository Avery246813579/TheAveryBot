package com.frostbyte.theaverybot.sql.theaverybot;

import com.frostbyte.javasqlapi.Table;

public class Currencies extends Table{
	public Currencies() {
		super("frostbyt_theaverybot", "Currencies");
		variables.put("currency_id", "INT NOT NULL AUTO_INCREMENT");
		variables.put("twitch_id", "INT");
		variables.put("channel", "VARCHAR(30)");
		variables.put("points", "INT DEFAULT 0");
		variables.put("credits", "INT DEFAULT 0");
		variables.put("custom", "INT DEFAULT 0");
		
		addPrimaryKey("currency_id");
	}
}
