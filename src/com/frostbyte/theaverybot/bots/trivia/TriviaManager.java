package com.frostbyte.theaverybot.bots.trivia;

import java.util.HashMap;
import java.util.Map;

import com.frostbyte.theaverybot.bots.BotManager;
import com.frostbyte.theaverybot.games.trivia.Question;
import com.frostbyte.theaverybot.games.trivia.TriviaHandler;
import com.frostbyte.theaverybot.sql.SqlHandler;
import com.frostbyte.theaverybot.util.ObjectUtil;
import com.frostbyte.theaverybot.util.PlayerUtil;

public class TriviaManager {
	private boolean enabled = true;
	private int delay = 30, SECOND = -delay, type_id = 1;
	private Question question = null;
	private TriviaBot triviaBot;

	public TriviaManager(BotManager botManager) {
		this.triviaBot = new TriviaBot(this, botManager.channel, botManager.trivia_username, botManager.trivia_oauth);
		resetTrivia();
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

		/** Send Message **/
		String message = TriviaHandler.trivias.get(type_id).getMessages().get("CORRECT_ANSWER");
		message = message.replaceAll("\\{PLAYER_NAME\\}", name);
		message = message.replaceAll("\\{PLAYER_AMOUNT\\}", "" + (ObjectUtil.objectToInt(playerInfo.get("points")) + 1));
		message = message.replaceAll("\\{ANSWER\\}", question.getAnswer());
		message = message.replaceAll("\\{QUESTION\\}", question.getQuestion());

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
		if (enabled) {
			resetTrivia();
			triviaBot.sendMessage(TriviaHandler.trivias.get(type_id).getMessages().get("JOIN"));
		} else {
			triviaBot.sendMessage(TriviaHandler.trivias.get(type_id).getMessages().get("LEAVE"));
		}

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
}
