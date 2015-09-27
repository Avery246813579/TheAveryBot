package com.frostbyte.theaverybot.sql.theaverybot.trivia;

import com.frostbyte.javasqlapi.Table;

public class Trivia_Settings extends Table{
	public Trivia_Settings() {
		super("frostbyt_theaverybot", "Trivia_Settings");
		variables.put("settings_id", "INT NOT NULL AUTO_INCREMENT");
		variables.put("account_id", "INT");
		variables.put("active", "BOOL");
		variables.put("type", "INT");
		variables.put("delay", "INT");
		addPrimaryKey("settings_id");
	}
}
