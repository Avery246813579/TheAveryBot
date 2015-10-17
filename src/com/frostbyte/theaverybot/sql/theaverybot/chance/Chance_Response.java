package com.frostbyte.theaverybot.sql.theaverybot.chance;

import com.frostbyte.javasqlapi.Table;

public class Chance_Response extends Table{

	public Chance_Response() {
		super("frostbyt_theaverybot", "Chance_Response");
		variables.put("response_id", "INT NOT NULL AUTO_INCREMENT");
		variables.put("response", "TEXT");
		variables.put("gain", "INT");
		variables.put("type_id", "INT");
		addPrimaryKey("response_id");
	}

}
