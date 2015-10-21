package com.frostbyte.theaverybot.sql.bytesite;

import com.frostbyte.javasqlapi.Table;

public class Site_Settings extends Table{
	public Site_Settings() {
		super("frostbyt_bytesite", "Settings");
		variables.put("setting_id", "INT NOT NULL AUTO_INCREMENT");
		variables.put("site_id", "INT");
		variables.put("currency_name", "VARCHAR(35)");
		variables.put("currency_income", "INT");
		addPrimaryKey("setting_id");
	}
}
