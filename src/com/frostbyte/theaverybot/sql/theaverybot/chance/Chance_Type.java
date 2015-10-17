package com.frostbyte.theaverybot.sql.theaverybot.chance;

import com.frostbyte.javasqlapi.Table;

public class Chance_Type extends Table{
	public Chance_Type(){
		super("frostbyt_theaverybot", "chance_type");
		variables.put("type_id", "INT NOT NULL AUTO_INCREMENT");
		variables.put("username", "VARCHAR(25)");
		variables.put("oauth", "VARCHAR(75)");
		variables.put("name", "VARCHAR(75)");
		variables.put("start", "VARCHAR(1)");
		variables.put("version", "VARCHAR(20)");
		variables.put("description", "VARCHAR(250)");
		variables.put("messages", "TEXT");
		variables.put("public", "TINYINT");
		variables.put("users", "TEXT");
		addPrimaryKey("type_id");
	}
}
