package com.frostbyte.theaverybot.sql.thirdparty;

import com.frostbyte.javasqlapi.Table;

public class Twitch_Accounts extends Table{
	public Twitch_Accounts() {
		super("frostbyt_thirdparty", "Twitch_Accounts");
		variables.put("twitch_id", "INT NOT NULL AUTO_INCREMENT");
		variables.put("account_id", "INT");
		variables.put("username", "VARCHAR(25)");
		variables.put("access_token", "VARCHAR(50)");
		
		addPrimaryKey("twitch_id");
	}
}
