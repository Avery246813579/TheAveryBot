package com.frostbyte.theaverybot.irc;

public class User {
	private String prefix, nick;
	private boolean mod, sub;

	public User(String prefix, String nick) {
		this.prefix = prefix;
		this.nick = nick;
	}

	public boolean isOp() {
		return prefix.indexOf('+') >= 0;
	}

	public boolean hasVoice() {
		return prefix.indexOf('@') >= 0;
	}

	public String toString() {
		return prefix + nick;
	}

	public boolean equals(String username) {
		return username.toLowerCase().equalsIgnoreCase(nick.toLowerCase());
	}

	public boolean equals(Object object) {
		if (object instanceof User) {
			User user = (User) object;
			return user.nick.toLowerCase().equals(nick.toLowerCase());
		}

		return false;
	}

	public int hashCode() {
		return nick.toLowerCase().hashCode();
	}

	public int compateTo(Object object) {
		if ((object instanceof User)) {
			User user = (User) object;
			return user.nick.toLowerCase().compareTo(nick.toLowerCase());
		}

		return -1;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public boolean isMod() {
		return mod;
	}

	public void setMod(boolean mod) {
		this.mod = mod;
	}

	public boolean isSub() {
		return sub;
	}

	public void setSub(boolean sub) {
		this.sub = sub;
	}
}
