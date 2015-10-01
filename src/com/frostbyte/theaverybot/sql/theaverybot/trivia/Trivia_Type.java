package com.frostbyte.theaverybot.sql.theaverybot.trivia;

import com.frostbyte.javasqlapi.Table;

public class Trivia_Type extends Table{
	/*
	 * Message Types: 
	 * - Win
	 * - Enabled
	 * - Disabled
	 * - No Answer
	 * - Hint
	 * 
	 * {Player}
	 * {Player_Amount}
	 * {Player_Rank}
	 * {Hint}
	 * {Answer}
	 * {Question}
	 * {Rank}
	 * {Needed}
	 * {Max}
	 * 
	 * For Answers:
	 * First once is most correct,
	 * Alises, 
	 * more
	 */
	
	public Trivia_Type(){
		super("frostbyt_theaverybot", "trivia_type");
		variables.put("type_id", "INT NOT NULL AUTO_INCREMENT");
		variables.put("name", "VARCHAR(75)");
		variables.put("version", "VARCHAR(20)");
		variables.put("description", "VARCHAR(250)");
		variables.put("messages", "TEXT");
		variables.put("public", "TINYINT");
		addPrimaryKey("type_id");
	}
}
