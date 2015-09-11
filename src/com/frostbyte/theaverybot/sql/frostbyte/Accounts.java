package com.frostbyte.theaverybot.sql.frostbyte;

import com.frostbyte.javasqlapi.Table;

public class Accounts extends Table{
	public Accounts() {
		super("frostbyt_frostbyte", "Accounts");
		variables.put("account_id", "INT NOT NULL AUTO_INCREMENT");
		variables.put("username", "VARCHAR(25)");
		variables.put("display_name", "VARCHAR(35)");
		variables.put("date_created", "VARCHAR(75)");
		variables.put("last_log", "VARCHAR(75)");
		variables.put("rank", "VARCHAR(25)");
		variables.put("online", "TINYINT");
		variables.put("reputation", "INT");
		addPrimaryKey("account_id");
	}
	
}
