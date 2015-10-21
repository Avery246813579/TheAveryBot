package com.frostbyte.theaverybot.sql.bytesite;

import com.frostbyte.javasqlapi.Table;

public class Sites extends Table{
	public Sites() {
		super("frostbyt_bytesite", "Sites");
		variables.put("site_id", "INT NOT NULL AUTO_INCREMENT");
		variables.put("account_id", "INT");
		variables.put("name", "VARCHAR(35)");
		variables.put("prefix", "VARCHAR(16)");
		addPrimaryKey("site_id");
	}
}
