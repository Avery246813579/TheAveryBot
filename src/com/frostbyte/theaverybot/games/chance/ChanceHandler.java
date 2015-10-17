package com.frostbyte.theaverybot.games.chance;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.frostbyte.theaverybot.sql.SqlHandler;

public class ChanceHandler {
	public static Map<Integer, Chance> chances = new HashMap<Integer, Chance>();
	
	public ChanceHandler(){
		loadChance();
	}
	
	public void loadChance(){
		List<Map<String, Object>> tables = SqlHandler.chance_Type.get();
		
		for(Map<String, Object> table : tables){
			chances.put((int) table.get("type_id"), new Chance((int) table.get("type_id"), 
					(String) table.get("name"), (char) table.get("start"), (String) table.get("version"), 
					(String) table.get("username"), (String) table.get("oauth")));
		}
	}
	
	public static void loadBot(int type_id){
		List<Map<String, Object>> tables = SqlHandler.chance_Type.get("type_id", type_id);

		for(Map<String, Object> table : tables){
			chances.put((int) table.get("type_id"), new Chance((int) table.get("type_id"), 
					(String) table.get("name"), (char) table.get("start"), (String) table.get("version"), 
					(String) table.get("username"), (String) table.get("oauth")));
		}
	}
}
