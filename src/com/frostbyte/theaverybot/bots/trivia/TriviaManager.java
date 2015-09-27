package com.frostbyte.theaverybot.bots.trivia;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

import com.frostbyte.theaverybot.bots.BotManager;
import com.frostbyte.theaverybot.games.trivia.Question;
import com.frostbyte.theaverybot.games.trivia.TriviaHandler;
import com.frostbyte.theaverybot.sql.SqlHandler;
import com.frostbyte.theaverybot.util.ObjectUtil;
import com.frostbyte.theaverybot.util.PlayerUtil;

public class TriviaManager {
	private boolean enabled;
	private int delay = 30, SECOND = -delay, type_id = 1;
	public Map<String, Integer> trivia = new LinkedHashMap<String, Integer>();
	private Question question = null;
	private TriviaBot triviaBot;
	private BotManager botManager;

	public TriviaManager(BotManager botManager) {
		this.triviaBot = new TriviaBot(this, botManager.channel, botManager.trivia_username, botManager.trivia_oauth);
		this.botManager = botManager;

		checkStatus();
		resetTrivia();

		/* Sorts Rank Stuff **/
		Map<String, Object> where = new HashMap<String, Object>();
		where.put("channel", botManager.channel);
		for (Map<String, Object> users : SqlHandler.currencies.get(where)) {
			List<Map<String, Object>> tables = SqlHandler.twitch_Accounts.get("twitch_id",
					ObjectUtil.objectToInt(users.get("twitch_id")));

			if (tables.size() <= 0) {
				Map<String, Object> table = new HashMap<String, Object>();
				table.put("twitch_id", ObjectUtil.objectToInt(users.get("twitch_id")));
				SqlHandler.twitch_Accounts.create(table);

				tables = SqlHandler.twitch_Accounts.get("twitch_id", ObjectUtil.objectToInt(users.get("twitch_id")));
			}

			trivia.put(ObjectUtil.objectToString(tables.get(0).get("username")),
					ObjectUtil.objectToInt(users.get("points")));
		}

	}

	public void checkStatus() {
		try {
			/** Declares Player Finder **/
			Map<String, Object> playerFinders = new HashMap<String, Object>();
			playerFinders.put("account_id", botManager.getAccount_id());

			/** Checks Trivia Information **/
			List<Map<String, Object>> triviaInfo = SqlHandler.trivia_Settings.get(playerFinders);
			if (triviaInfo.isEmpty()) {
				Map<String, Object> triviaCreate = new HashMap<String, Object>();
				triviaCreate.put("account_id", botManager.getAccount_id());
				triviaCreate.put("active", 1);
				triviaCreate.put("type", 1);
				triviaCreate.put("delay", 30);
				SqlHandler.trivia_Settings.create(triviaCreate);

				triviaInfo = SqlHandler.trivia_Settings.get(playerFinders);
			}

			/** Sets Trivia Information **/
			if(enabled  != (boolean) triviaInfo.get(0).get("active")){
				enabled = (boolean) triviaInfo.get(0).get("active");
				resetTrivia();
			
				if(enabled){
					triviaBot.sendMessage(TriviaHandler.trivias.get(type_id).getMessages().get("JOIN"));
				}else{
					triviaBot.sendMessage(TriviaHandler.trivias.get(type_id).getMessages().get("LEAVE"));
				}
			}

			
			delay = ObjectUtil.objectToInt(triviaInfo.get(0).get("delay"));
		} catch (Exception ex) {
			triviaBot.sendMessage("Error enabling Trivia! Setting to default!");
			ex.printStackTrace();
		}
	}

	public void stop() {
		triviaBot.running = false;
	}

	public void onMessage(String channel, String sender, String login, String hostname, String message) {
		if (enabled) {
			if (question != null) {
				if (question.isAnswer(message)) {
					try {
						correctAnswer(sender, message);
					} catch (Exception ex) {
						System.out.println("Rabbits! Something happened!");
					}
				}
			}
		}
	}

	public void update() {
		if (enabled) {
			SECOND++;

			switch (SECOND) {
			case 0:
				giveQuestion();
				break;
			case 15:
				giveHint(1);
				break;
			case 30:
				giveHint(2);
				break;
			case 45:
				giveHint(3);
				break;
			case 60:
				giveAnswer();
				break;
			}
		}
	}

	public void resetTrivia() {
		SECOND = -delay;
		question = null;
	}

	public void correctAnswer(String name, String answer) {
		/** Give Player Points **/
		Map<String, Object> playerInfo = PlayerUtil.getCurrency(name, triviaBot.getChannel());
		int amount = ObjectUtil.objectToInt(playerInfo.get("points"));

		Map<String, Object> info = new HashMap<String, Object>();
		info.put("points", amount + 1);
		Map<String, Object> where = new HashMap<String, Object>();
		where.put("twitch_id", playerInfo.get("twitch_id"));
		where.put("channel", triviaBot.getChannel());
		SqlHandler.currencies.update(info, where);

		if (!trivia.containsKey(name)) {
			trivia.put(name, ObjectUtil.objectToInt(playerInfo.get("points")) + 1);
		}

		List<Map.Entry<String, Integer>> entries = new LinkedList<Map.Entry<String, Integer>>(trivia.entrySet());

		Collections.sort(entries, new Comparator<Map.Entry<String, Integer>>() {
			public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
				return o2.getValue().compareTo(o1.getValue());
			}
		});

		trivia.clear();

		for (Map.Entry<String, Integer> entry : entries) {
			trivia.put(entry.getKey(), entry.getValue());
		}

		String last = getLast(name);
		int last_points;
		if (!last.equalsIgnoreCase("NULL")) {
			last_points = trivia.get(last.toLowerCase());
		} else {
			last_points = 0;
		}

		int needed = last_points - (ObjectUtil.objectToInt(playerInfo.get("points")) + 1);

		/** Send Message **/
		String message = TriviaHandler.trivias.get(type_id).getMessages().get("CORRECT_ANSWER");
		message = message.replaceAll("\\{PLAYER_NAME\\}", name);
		message = message.replaceAll("\\{PLAYER_AMOUNT\\}",
				"" + (ObjectUtil.objectToInt(playerInfo.get("points")) + 1));
		message = message.replaceAll("\\{ANSWER\\}", question.getAnswer());
		message = message.replaceAll("\\{QUESTION\\}", question.getQuestion());
		message = message.replaceAll("\\{RANK\\}", Integer.toString(getValue(name)));
		message = message.replaceAll("\\{NEEDED\\}", Integer.toString(needed));
		message = message.replaceAll("\\{MAX\\}", Integer.toString(trivia.size()));

		triviaBot.sendMessage(message);

		resetTrivia();
	}

	public void giveAnswer() {
		String message = TriviaHandler.trivias.get(type_id).getMessages().get("TIMES_UP");
		message = message.replaceAll("\\{ANSWER\\}", question.getAnswer());
		message = message.replaceAll("\\{QUESTION\\}", question.getQuestion());

		triviaBot.sendMessage(message);

		resetTrivia();
	}

	public void giveQuestion() {
		Question question = TriviaHandler.trivias.get(type_id).randomQuestion();
		triviaBot.sendMessage("#" + triviaBot.getChannel(), question.getQuestion());
		this.question = question;
	}

	public void giveHint(int level) {
		String answer = question.getAnswer();
		String hint = "";
		int i = 0;

		for (char c : answer.toCharArray()) {
			if (c == ' ') {
				hint = hint + " ";
			} else {
				if (i > 3) {
					i = 0;
				} else {
					i++;
				}

				if (i <= level) {
					hint = hint + c;
				} else {
					hint = hint + "_";
				}
			}
		}

		String message = TriviaHandler.trivias.get(type_id).getMessages().get("HINT");
		message = message.replaceAll("\\{HINT\\}", hint);

		triviaBot.sendMessage(message);
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public int getType_id() {
		return type_id;
	}

	public void setType_id(int type_id) {
		this.type_id = type_id;
	}

	public TriviaBot getTriviaBot() {
		return triviaBot;
	}

	public void setTriviaBot(TriviaBot triviaBot) {
		this.triviaBot = triviaBot;
	}

	public int getValue(String sender) {
		int i = 1;

		for (String name : trivia.keySet()) {
			if (name.equalsIgnoreCase(sender)) {
				return i;
			}

			i++;
		}

		return trivia.size() + 1;
	}

	public String getLast(String sender) {
		String last = "NULL";

		for (String name : trivia.keySet()) {
			if (name.equalsIgnoreCase(sender)) {
				return last;
			}

			last = name;
		}

		return "NULL";
	}
}
