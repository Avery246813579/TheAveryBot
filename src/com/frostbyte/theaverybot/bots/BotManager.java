package com.frostbyte.theaverybot.bots;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.frostbyte.theaverybot.bots.trivia.TriviaBot;
import com.frostbyte.theaverybot.bots.trivia.TriviaManager;
import com.frostbyte.theaverybot.commands.CommandHandler;
import com.frostbyte.theaverybot.loading.bots.ActiveBot;
import com.frostbyte.theaverybot.sql.SqlHandler;
import com.frostbyte.theaverybot.util.ObjectUtil;

public class BotManager {
	private BotThread botThread;
	public String channel, botName, regular_username, regular_oauth, trivia_username, trivia_oauth;
	private boolean enabled = false;
	private TriviaManager triviaManager;
	private CommandHandler commandHandler;
	private TheAveryBot averyBot;
	private int account_id;
	private Map<String, String> messages = new HashMap<String, String>();
	private String stringSettings = "NULL";
	private int botType = -1;

	public BotManager(ActiveBot activeBot) {
		channel = ObjectUtil.objectToString(SqlHandler.twitch_Accounts.get("account_id", ObjectUtil.intToObject(activeBot.getAccount_id())).get(0).get("username"));
		account_id = activeBot.getAccount_id();
		triviaManager = new TriviaManager(this);

		loadBotSettings();

		triviaManager.init();

		this.enabled = true;
		commandHandler = new CommandHandler(this);

		botThread = new BotThread(this);
		botThread.start();
	}

	public void loadBotSettings() {
		Map<String, Object> settingsWhere = new HashMap<String, Object>();
		settingsWhere.put("account_id", account_id);

		List<Map<String, Object>> settingsTable = SqlHandler.bot_Settings.get(settingsWhere);
		if (settingsTable.isEmpty()) {
			Map<String, Object> createTable = new HashMap<String, Object>();
			createTable.put("account_id", account_id);
			createTable.put("bot_type", 1);
			createTable.put("messages", "SUB|{PLAYER_NAME} has subbed!~RE_SUB|{PLAYER_NAME} has subbed for {SUB_TIME} months!");

			SqlHandler.bot_Settings.create(createTable);
			settingsTable = SqlHandler.bot_Settings.get(settingsWhere);
		}

		if (botType != (int) settingsTable.get(0).get("bot_type")) {
			botType = (int) settingsTable.get(0).get("bot_type");

			List<Map<String, Object>> botTables = SqlHandler.bot_Type.get("bot_id", botType);
			Map<String, Object> table;
			if (!botTables.isEmpty()) {
				table = botTables.get(0);
			} else {
				botType = 1;
				table = SqlHandler.bot_Type.get("bot_id", botType).get(0);
			}

			botName = (String) table.get("name");
			trivia_username = (String) table.get("trivia_username");
			trivia_oauth = (String) table.get("trivia_oauth");
			regular_username = (String) table.get("regular_username");
			regular_oauth = (String) table.get("regular_oauth");

			if (averyBot != null) {
				averyBot.running = false;
			}

			averyBot = new TheAveryBot(this, channel, regular_username, regular_oauth);

			if (triviaManager.getTriviaBot() != null) {
				triviaManager.getTriviaBot().running = false;
			}

			triviaManager.setTriviaBot(new TriviaBot(triviaManager, channel, trivia_username, trivia_oauth));

			String joinMessage = "";
			if (botType == 1) {
				joinMessage = "{BOT_NAME} has joined the chat!" + " {BOT_NAME} is developed by Avery246813579! TheAveryBot is public and can be found on the FrostbyteDev website!";
			} else {
				joinMessage = "{BOT_NAME} has joined the chat!" + " {BOT_NAME} is powered by TheAveryBot and developed by Avery246813579! TheAveryBot is public and can be found on the FrostbyteDev website!";
			}

			if (joinMessage.contains("{BOT_NAME}")) {
				joinMessage = joinMessage.replaceAll("\\{BOT_NAME\\}", botName);
			}

			averyBot.sendMessage(joinMessage);
		}

		if (!stringSettings.equalsIgnoreCase((String) settingsTable.get(0).get("messages"))) {
			stringSettings = (String) settingsTable.get(0).get("messages");
			decodeMessages();
		}
	}

	public void onSubscribe(String channel, String user) {
		String subMessage = messages.get("SUB");

		if (subMessage.contains("{PLAYER_NAME}")) {
			subMessage = subMessage.replaceAll("\\{PLAYER_NAME\\}", user);
		}

		averyBot.sendMessage(subMessage);
	}

	public void decodeMessages() {
		String[] messageSplit = stringSettings.split("~");
		for (String littleSplit : messageSplit) {
			String[] mSplit = littleSplit.split("\\|");
			messages.put(mSplit[0].toUpperCase(), mSplit[1]);
		}
	}

	public void shutdown() {
		enabled = false;
		triviaManager.stop();
		averyBot.running = false;
	}

	public void update() {
		if (triviaManager != null) {
			triviaManager.update();
		}
	}

	public void onMessage(String channel, String sender, String message) {
		if (commandHandler != null) {
			commandHandler.onMessage(sender, message);
		}
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public TriviaManager getTriviaManager() {
		return triviaManager;
	}

	public void setTriviaManager(TriviaManager triviaManager) {
		this.triviaManager = triviaManager;
	}

	public CommandHandler getCommandHandler() {
		return commandHandler;
	}

	public void setCommandHandler(CommandHandler commandHandler) {
		this.commandHandler = commandHandler;
	}

	public TheAveryBot getAveryBot() {
		return averyBot;
	}

	public void setAveryBot(TheAveryBot averyBot) {
		this.averyBot = averyBot;
	}

	public int getAccount_id() {
		return account_id;
	}

	public void setAccount_id(int account_id) {
		this.account_id = account_id;
	}
}
