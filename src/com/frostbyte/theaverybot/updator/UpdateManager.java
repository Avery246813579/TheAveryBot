package com.frostbyte.theaverybot.updator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.frostbyte.theaverybot.debug.Console;
import com.frostbyte.theaverybot.sql.SqlHandler;
import com.frostbyte.theaverybot.updator.types.TriviaUpdator;
import com.frostbyte.theaverybot.util.ObjectUtil;

public class UpdateManager implements Runnable {
	public List<AutoUpdator> updators = new ArrayList<AutoUpdator>();
	private int updateTime = 20;

	public UpdateManager() {
		ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
		executor.scheduleAtFixedRate(this, 0, updateTime, TimeUnit.SECONDS);
	}

	public void run() {
		triviaChecker();

		for (AutoUpdator autoUpdator : updators) {
			autoUpdator.checkUpdate();
		}
	}

	public void triviaChecker() {
		List<Map<String, Object>> tables = SqlHandler.trivia_Type.get();

		for (Map<String, Object> table : tables) {
			int type_id = ObjectUtil.objectToInt(table.get("type_id"));

			if (!containsTrivia(type_id)) {
				Console.sendLog("Auto Updator added Trivia with the type id of " + type_id);
				updators.add(new TriviaUpdator(type_id));
			}
		}
	}

	public boolean containsTrivia(int type_id) {
		for (AutoUpdator autoUpdator : updators) {
			if (autoUpdator instanceof TriviaUpdator) {
				TriviaUpdator triviaUpdator = (TriviaUpdator) autoUpdator;

				if (triviaUpdator.type_id == type_id) {
					return true;
				}
			}
		}

		return false;
	}
}
