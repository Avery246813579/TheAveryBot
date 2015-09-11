package com.frostbyte.theaverybot.games.trivia;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.frostbyte.theaverybot.sql.SqlHandler;
import com.frostbyte.theaverybot.util.ObjectUtil;

public class TriviaHandler {
	public static Map<Integer, Trivia> trivias = new HashMap<Integer, Trivia>();
	
	public TriviaHandler(){
		loadTrivias();
	}
	
	public void loadTrivias(){
		List<Map<String, Object>> tables = SqlHandler.trivia_Type.get();
		
		for(Map<String, Object> table : tables){
			trivias.put(ObjectUtil.objectToInt(table.get("type_id")), new Trivia(ObjectUtil.objectToInt(table.get("type_id")), ObjectUtil.objectToString(table.get("name")), ObjectUtil.objectToString(table.get("version"))));
		}
	}
	
	public static void loadBot(int type_id){
		List<Map<String, Object>> tables = SqlHandler.trivia_Type.get("type_id", type_id);

		for(Map<String, Object> table : tables){
			trivias.put(ObjectUtil.objectToInt(table.get("type_id")), new Trivia(ObjectUtil.objectToInt(table.get("type_id")), ObjectUtil.objectToString(table.get("name")), ObjectUtil.objectToString(table.get("version"))));
		}
	}
}
