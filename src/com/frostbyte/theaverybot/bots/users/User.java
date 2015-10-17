package com.frostbyte.theaverybot.bots.users;

import java.util.HashMap;
import java.util.Map;

import com.frostbyte.theaverybot.sql.SqlHandler;

public class User {
	private String name, channel;
	private int twitch_id, credits, points, custom;
	
	public User(String name, int twitch_id) {
		this.setName(name);
		this.twitch_id = twitch_id;
	}
	
	public void loadCurrency(String channel, int credits, int points, int custom){
		this.channel = channel;
		this.credits = credits;
		this.points = points;
		this.custom = custom;
	}

	public void addCredits(int credits){
		this.credits += credits;
	}
	
	public void removeCredits(int credits){
		this.credits -= credits;
	}
	
	public void setCredits(int credits){
		this.credits = credits;
	}
	
	public void saveCurrency(){
		Map<String, Object> inputTable = new HashMap<String, Object>();
		inputTable.put("credits", credits);
		inputTable.put("points", points);
		inputTable.put("custom", custom);
		
		Map<String, Object> whereTable = new HashMap<String, Object>();
		whereTable.put("twitch_id", twitch_id);
		whereTable.put("channel", channel);
		
		SqlHandler.currencies.update(inputTable, whereTable);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
