package com.frostbyte.theaverybot.games.chance;

public class ChanceResponse {
	private String reply;
	private int gain;

	public ChanceResponse(String reply, int gain) {
		this.reply = reply;
		this.gain = gain;
	}

	public String getReply() {
		return reply;
	}

	public void setReply(String reply) {
		this.reply = reply;
	}

	public int getGain() {
		return gain;
	}

	public void setGain(int gain) {
		this.gain = gain;
	}
}
