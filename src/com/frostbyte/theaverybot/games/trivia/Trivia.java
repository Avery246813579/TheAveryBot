package com.frostbyte.theaverybot.games.trivia;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.frostbyte.theaverybot.sql.SqlHandler;
import com.frostbyte.theaverybot.util.ObjectUtil;

public class Trivia {
	private Map<String, String> messages = new HashMap<String, String>();
	private List<Question> questions = new ArrayList<Question>();
	private int type_id;
	private String name, version;
	
	public Trivia(int type_id, String name, String version) {
		this.type_id = type_id;
		this.name = name;
		this.version = version;

		decodeMessages();
		loadQuestions();
	}

	public void loadQuestions() {
		List<Map<String, Object>> tables = SqlHandler.trivia_Questions.get("type_id", type_id);

		for (Map<String, Object> table : tables) {
			questions.add(new Question(ObjectUtil.objectToInt(table.get("question_id")), ObjectUtil.objectToString(table.get("question")), ObjectUtil.objectToString(table.get("answer"))));
		}
	}
	
	public void decodeMessages(){
		String message = ObjectUtil.objectToString(SqlHandler.trivia_Type.get("type_id", type_id).get(0).get("messages"));
		String[] messageSplit = message.split("~");
		for(String littleSplit : messageSplit){
			String[] mSplit = littleSplit.split("\\|");
			messages.put(mSplit[0].toUpperCase(), mSplit[1]);
		}
	}
	
	public Question randomQuestion(){
		return questions.get(new Random().nextInt(questions.size()));
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<String, String> getMessages() {
		return messages;
	}

	public void setMessages(Map<String, String> messages) {
		this.messages = messages;
	}

	public int getType_id() {
		return type_id;
	}

	public void setType_id(int type_id) {
		this.type_id = type_id;
	}
}
