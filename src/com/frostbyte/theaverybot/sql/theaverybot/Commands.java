package com.frostbyte.theaverybot.sql.theaverybot;

import com.frostbyte.javasqlapi.Table;

public class Commands extends Table{
	public Commands() {
		super("frostbyt_theaverybot", "Commands");
		variables.put("command_id", "INT NOT NULL AUTO_INCREMENT");
		variables.put("account_id", "INT");
		variables.put("command", "VARCHAR(100)");
		variables.put("content", "TEXT");
		variables.put("group_id", "INT");
		
		addPrimaryKey("command_id");
	}
}
