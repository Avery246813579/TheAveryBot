package com.frostbyte.theaverybot.games.trivia;

import java.util.ArrayList;
import java.util.List;

public class Question {
	private int question_id;
	private String question;
	private List<String> answers = new ArrayList<String>();

	public Question(int question_id, String question, String answers) {
		this.question_id = question_id;
		this.question = question;

		if (answers.contains("~")) {
			String[] split = answers.split("~");
			for (String string : split) {
				this.answers.add(string);
			}
		} else {
			this.answers.add(answers);
		}
	}
	
	public String getAnswer(){
		return answers.get(0);
	}

	public boolean isAnswer(String answer) {
		for (String string : answers) {
			if (answer.equalsIgnoreCase(string)) {
				return true;
			}
		}

		return false;
	}

	public int getQuestion_id() {
		return question_id;
	}

	public void setQuestion_id(int question_id) {
		this.question_id = question_id;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}
}
