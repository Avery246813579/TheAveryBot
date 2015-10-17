package com.frostbyte.theaverybot.economics;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.frostbyte.theaverybot.sql.SqlHandler;

public class User {
	private UserManager manager;
	private int twitch_id, saveTime, points, credits, custom;
	private String name;

	public User(UserManager manager, String name) {
		this.manager = manager;
		this.name = name;
	}

	public void load() {
		Map<String, Object> twitchWhere = new HashMap<String, Object>();
		twitchWhere.put("username", name);

		List<Map<String, Object>> twitchList = SqlHandler.twitch_Accounts.get(twitchWhere);
		if (twitchList.isEmpty()) {
			return;
		}

		Map<String, Object> twitchTable = twitchList.get(0);
		twitch_id = (int) twitchTable.get("twitch_id");

		Map<String, Object> currencyWhere = new HashMap<String, Object>();
		currencyWhere.put("twitch_id", twitch_id);
		currencyWhere.put("channel", manager.getBotManager().channel);

		List<Map<String, Object>> currencyList = SqlHandler.currencies.get(currencyWhere);
		if (currencyList.isEmpty()) {
			return;
		}

		Map<String, Object> currencTable = currencyList.get(0);
		points = (int) currencTable.get("points");
		credits = (int) currencTable.get("credits");
		custom = (int) currencTable.get();

	}

	public void save() {
		Map<String, Object> saveTable = new HashMap<String, Object>();
		saveTable.put("points", points);
		saveTable.put("credits", credits);
		saveTable.put("custom", custom);

		Map<String, Object> whereTable = new HashMap<String, Object>();
		whereTable.put("twitch_id", twitch_id);
		whereTable.put("channel", manager.getBotManager().channel);

		SqlHandler.currencies.update(saveTable, whereTable);
	}

	public void addCustom(int custom) {
		this.custom += custom;
	}

	public void removeCustom(int custom) {
		this.custom -= custom;
	}

	public void setCustom(int custom) {
		this.custom = custom;
	}

	public int getCustom() {
		return custom;
	}

	public void addCredits(int credits) {
		this.credits += credits;
	}

	public void removeCredits(int credits) {
		this.credits -= credits;
	}

	public void setCredits(int credits) {
		this.credits = credits;
	}

	public int getCredits() {
		return credits;
	}

	public void addPoints(int points) {
		this.points += points;
	}

	public void removePoints(int points) {
		this.points -= points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public int getPoints() {
		return points;
	}

	public void addTime() {
		saveTime++;
	}

	public boolean timeExpired() {
		return saveTime >= 300;
	}

	public String getName() {
		return name;
	}
}
