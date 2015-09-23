package com.frostbyte.theaverybot.bots.trivia;

public class TriviaUser {
	private String username;
	private int points, credits;
	
	public TriviaUser(String username, int points, int credits) {
		this.username = username;
		this.points = points;
		this.credits = credits;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public int getCredits() {
		return credits;
	}

	public void setCredits(int credits) {
		this.credits = credits;
	}
}
