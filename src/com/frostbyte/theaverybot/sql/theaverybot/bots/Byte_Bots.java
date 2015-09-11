package com.frostbyte.theaverybot.sql.theaverybot.bots;

import com.frostbyte.javasqlapi.Table;

public class Byte_Bots extends Table{
	public Byte_Bots() {
		super("frostbyt_theaverybot", "Byte_Bots");
		variables.put("byte_id", "INT NOT NULL AUTO_INCREMENT");
		variables.put("account_id", "INT");
		variables.put("bot_id", "INT");
		addPrimaryKey("byte_id");
	}
}
