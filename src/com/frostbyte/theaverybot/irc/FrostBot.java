package com.frostbyte.theaverybot.irc;

public abstract class FrostBot extends Irc {
	private String channel, oauth;

	public FrostBot(String channel, String username, String oauth) {
		this.channel = channel;
		this.oauth = oauth;
		
		setUsername(username);
		connect();
	}

	@Override
	protected abstract void onMessage(String channel, String sender, String login, String hostname, String message);

	protected abstract void onBotConnect();
	
	public void connect() {
		try {
			connect("irc.twitch.tv", 6667, "oauth:" + oauth);
		} catch (Exception e) {
			e.printStackTrace();
		}

		joinChannel("#" + channel);
		sendRawLine("TWITCHCLIENT 2");
		sendMessage("#" + channel, "/mods");
		
		onBotConnect();
	}
	
	public void sendMessage(String message){
		sendMessage("#" + channel, message);
	}
	
	public String getChannel(){
		return channel;
	}
}
