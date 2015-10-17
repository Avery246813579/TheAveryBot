package com.frostbyte.theaverybot.games.chance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.frostbyte.theaverybot.sql.SqlHandler;
import com.frostbyte.theaverybot.util.ObjectUtil;

public class Chance {
	private List<ChanceResponse> responses = new ArrayList<ChanceResponse>();
	private Map<String, String> messages = new HashMap<String, String>();
	private String name, version, username, oauth;
	private int type_id;
	private char start;
	
	public Chance(int type_id, String name, char start, String version, String username, String oauth){
		this.type_id = type_id;
		this.name = name;
		this.start = start;
		this.version = version;
		this.username = username;
		this.oauth = oauth;
		
		decodeMessages();
		loadResponse();
	}

	public void loadResponse() {
		List<Map<String, Object>> tables = SqlHandler.chance_Response.get("type_id", type_id);

		for (Map<String, Object> table : tables) {
			responses.add(new ChanceResponse((String) table.get("response"), (int)table.get("gain"))); 
		}
	}
	
	public void decodeMessages(){
		String message = ObjectUtil.objectToString(SqlHandler.chance_Type.get("type_id", type_id).get(0).get("messages"));
		String[] messageSplit = message.split("~");
		for(String littleSplit : messageSplit){
			String[] mSplit = littleSplit.split("\\|");
			messages.put(mSplit[0].toUpperCase(), mSplit[1]);
		}
	}
	
	public ChanceResponse randomResponse(){
		return responses.get(new Random().nextInt(responses.size()));
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public char getStart() {
		return start;
	}

	public void setStart(char start) {
		this.start = start;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public int getType_id() {
		return type_id;
	}

	public void setType_id(int type_id) {
		this.type_id = type_id;
	}

	public List<ChanceResponse> getResponses() {
		return responses;
	}

	public void setResponses(List<ChanceResponse> responses) {
		this.responses = responses;
	}

	public Map<String, String> getMessages() {
		return messages;
	}

	public void setMessages(Map<String, String> messages) {
		this.messages = messages;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getOauth() {
		return oauth;
	}

	public void setOauth(String oauth) {
		this.oauth = oauth;
	}
}
