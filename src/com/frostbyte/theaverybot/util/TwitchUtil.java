package com.frostbyte.theaverybot.util;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class TwitchUtil {
	public static List<String> getChatters(String channel) {
		List<String> users = new ArrayList<String>();
		String sURL = "http://tmi.twitch.tv/group/user/" + channel + "/chatters";

		URL url;
		try {
			url = new URL(sURL);
			HttpURLConnection request = (HttpURLConnection) url.openConnection();
			request.connect();

			JSONParser jp = new JSONParser(); // from gson
			JSONObject jsonObject = (JSONObject) jp.parse(new InputStreamReader((InputStream) request.getInputStream()));

			JSONObject chatters = (JSONObject) jsonObject.get("chatters");
			JSONArray mods = (JSONArray) chatters.get("moderators");
			JSONArray staff = (JSONArray) chatters.get("staff");
			JSONArray admins = (JSONArray) chatters.get("admins");
			JSONArray global_mods = (JSONArray) chatters.get("global_mods");
			JSONArray viewers = (JSONArray) chatters.get("viewers");

			for (Object string : mods.toArray()) {
				users.add((String) string);
			}

			for (Object string : staff.toArray()) {
				users.add((String) string);
			}

			for (Object string : admins.toArray()) {
				users.add((String) string);
			}

			for (Object string : global_mods.toArray()) {
				users.add((String) string);
			}

			for (Object string : viewers.toArray()) {
				users.add((String) string);
			}
		} catch (Exception ex) {
		}

		return users;
	}
}
