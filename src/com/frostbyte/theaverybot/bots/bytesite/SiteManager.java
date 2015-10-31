package com.frostbyte.theaverybot.bots.bytesite;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.frostbyte.theaverybot.bots.BotManager;
import com.frostbyte.theaverybot.sql.SqlHandler;
import com.frostbyte.theaverybot.util.PlayerUtil;
import com.frostbyte.theaverybot.util.TwitchUtil;

public class SiteManager {
	private BotManager botManager;
	private String name;
	private int second, income;

	public SiteManager(BotManager botManager) {
		this.botManager = botManager;
	}

	public void tick() {
		second++;
		if (second > 60) {
			givePoints();
			second = 0;
		}
	}

	public void givePoints() {
		for (String user : TwitchUtil.getChatters(botManager.channel)) {
			Map<String, Object> currency = PlayerUtil.getCurrency(user, botManager.channel);

			Map<String, Object> saveWhere = new HashMap<String, Object>();
			saveWhere.put("twitch_id", currency.get("twitch_id"));
			saveWhere.put("channel", currency.get("channel"));

			Map<String, Object> saveInput = new HashMap<String, Object>();
			saveInput.put("custom", (int) (currency.get("custom")) + income);

			SqlHandler.currencies.update(saveInput, saveWhere);
		}
	}

	public void update(Map<String, Object> sites) {
		Map<String, Object> whereSetting = new HashMap<String, Object>();
		whereSetting.put("site_id", sites.get("site_id"));

		List<Map<String, Object>> settingList = SqlHandler.site_Settings.get(whereSetting);
		if (settingList.isEmpty()) {
			return;
		}

		Map<String, Object> table = settingList.get(0);
		name = (String) table.get("currency_name");
		income = (int) table.get("currency_income");
	}

	public void onMessage(String channel, String sender, String message) {
		if (botManager.isSite()) {
			if (message.split(" ")[0].equalsIgnoreCase("$" + name) || message.split(" ")[0].equalsIgnoreCase("!" + name)) {
				Map<String, Object> currency = PlayerUtil.getCurrency(sender, botManager.channel);

				if (currency != null) {
					botManager.getAveryBot().sendMessage(sender + " has " + currency.get("custom") + " " + name + "!");
				} else {
					botManager.getAveryBot().sendMessage("Account not setup yet! Wait a minute!");
				}
			}
		}
	}
}
