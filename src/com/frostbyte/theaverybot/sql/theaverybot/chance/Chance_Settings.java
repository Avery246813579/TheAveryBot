package com.frostbyte.theaverybot.sql.theaverybot.chance;

import com.frostbyte.javasqlapi.Table;

public class Chance_Settings extends Table{
	public Chance_Settings() {
		super("frostbyt_theaverybot", "Chance_Settings");
		variables.put("chance_id", "INT NOT NULL AUTO_INCREMENT");
		variables.put("account_id", "INT");
		variables.put("active", "BOOL");
		variables.put("type", "INT");
		variables.put("delay", "INT");
		addPrimaryKey("chance_id");
	}
}
