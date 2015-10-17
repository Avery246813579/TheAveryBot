package com.frostbyte.theaverybot.updator.types;

import com.frostbyte.theaverybot.bots.BotManager;
import com.frostbyte.theaverybot.debug.Console;
import com.frostbyte.theaverybot.games.chance.ChanceHandler;
import com.frostbyte.theaverybot.games.trivia.TriviaHandler;
import com.frostbyte.theaverybot.loading.bots.ActiveBot;
import com.frostbyte.theaverybot.loading.bots.BotLoading;
import com.frostbyte.theaverybot.sql.SqlHandler;
import com.frostbyte.theaverybot.updator.AutoUpdator;
import com.frostbyte.theaverybot.util.ObjectUtil;

public class ChanceUpdator extends AutoUpdator {
	public int type_id;

	public ChanceUpdator(int type_id) {
		super("Chance", ChanceHandler.chances.get(type_id).getVersion());

		this.type_id = type_id;
	}

	@Override
	public boolean canUpdate() {
		int currentVersion = 0;
		String tVersion = ObjectUtil.objectToString(SqlHandler.chance_Type.get("type_id", type_id).get(0).get("version"));
		String sVersion = "";
		for (String string : tVersion.split("\\.")) {
			sVersion = sVersion + string;
		}

		try {
			currentVersion = Integer.parseInt(sVersion);
		} catch (Exception ex) {
			Console.sendError("Could not parse " + name + " version in auto updator!");
		}

		return currentVersion > version;
	}

	@Override
	public void update() {
		for (ActiveBot activeBot : BotLoading.bots) {
			BotManager manager = activeBot.getBotManager();

			if (manager.getTriviaManager().getType_id() == type_id) {
				String oldVersion = TriviaHandler.trivias.get(type_id).getVersion();

				manager.getTriviaManager().getTriviaBot().sendMessage("TheAveryBot << Auto Updator >> " + TriviaHandler.trivias.get(type_id).getName() + " Trivia is currently auto updating. Sorry for the inconvenience!");

				manager.getTriviaManager().resetTrivia();
				TriviaHandler.trivias.remove(type_id);
				TriviaHandler.loadBot(type_id);

				manager.getTriviaManager().getTriviaBot().sendMessage(
						"TheAveryBot << Auto Updator >> " + TriviaHandler.trivias.get(type_id).getName() + " Trivia has been updated from v" + oldVersion + " to v" + TriviaHandler.trivias.get(type_id).getVersion() + "! Thanks for you patience!");

				String tVersion = TriviaHandler.trivias.get(type_id).getVersion();
				String sVersion = "";
				for (String string : tVersion.split("\\.")) {
					sVersion = sVersion + string;
				}

				try {
					this.version = Integer.parseInt(sVersion);
				} catch (Exception ex) {
					Console.sendError("Could not parse " + name + " version in auto updator!");
				}
			}
		}
	}
}
