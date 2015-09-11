package com.frostbyte.theaverybot.sql.theaverybot.trivia;

import com.frostbyte.javasqlapi.Table;

public class Trivia_Questions extends Table{

	public Trivia_Questions() {
		super("frostbyt_theaverybot", "Trivia_Question");
		variables.put("question_id", "INT NOT NULL AUTO_INCREMENT");
		variables.put("question", "TEXT");
		variables.put("answer", "VARCHAR(150)");
		variables.put("type_id", "int");
		addPrimaryKey("question_id");
	}

}
