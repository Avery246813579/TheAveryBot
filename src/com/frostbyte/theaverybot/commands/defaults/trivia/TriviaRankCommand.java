package com.frostbyte.theaverybot.commands.defaults.trivia;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.frostbyte.theaverybot.bots.BotManager;
import com.frostbyte.theaverybot.commands.Command;
import com.frostbyte.theaverybot.games.trivia.TriviaHandler;
import com.frostbyte.theaverybot.sql.SqlHandler;

public class TriviaRankCommand extends Command {
	@Override
	public void onCommand(String sender, String[] args) {
		if (args.length == 0) {
			String message = TriviaHandler.trivias.get(getBotManager().getTriviaManager().getType_id()).getMessages().get("TRIVIA_RANK_COMMAND");
			getBotManager().getTriviaManager().sortTrivia();
			
			if (message.contains("{PLAYER_NAME}")) {
				message = message.replaceAll("\\{PLAYER_NAME\\}", sender);
			}

			if (message.contains("{RANK}")) {
				message = message.replaceAll("\\{RANK\\}", Integer.toString(getBotManager().getTriviaManager().getValue(sender)));
			}

			if (message.contains("{PLAYING}")) {
				message = message.replaceAll("\\{PLAYING\\}", Integer.toString(getBotManager().getTriviaManager().trivia.size()));
			}

			getBotManager().getTriviaManager().getTriviaBot().sendMessage(message);
		} else {
			Map<String, Object> twitch = new HashMap<String, Object>();
			twitch.put("username", args[0]);

			List<Map<String, Object>> twitchTables = SqlHandler.twitch_Accounts.get(twitch);
			if (!twitchTables.isEmpty()) {
				Map<String, Object> currency = new HashMap<String, Object>();
				currency.put("twitch_id", twitchTables.get(0).get("twitch_id"));
				currency.put("channel", getBotManager().channel);

				if (!SqlHandler.currencies.get(currency).isEmpty()) {
					String message = TriviaHandler.trivias.get(getBotManager().getTriviaManager().getType_id()).getMessages().get("TRIVIA_RANK_COMMAND");
					getBotManager().getTriviaManager().sortTrivia();

					if (message.contains("{PLAYER_NAME}")) {
						message = message.replaceAll("\\{PLAYER_NAME\\}", sender);
					}

					if (message.contains("{RANK}")) {
						message = message.replaceAll("\\{RANK\\}", Integer.toString(getBotManager().getTriviaManager().getValue(args[0])));
					}

					if (message.contains("{PLAYING}")) {
						message = message.replaceAll("\\{PLAYING\\}", Integer.toString(getBotManager().getTriviaManager().trivia.size()));
					}

					getBotManager().getTriviaManager().getTriviaBot().sendMessage(message);
				} else {
					getBotManager().getTriviaManager().getTriviaBot().sendMessage("Could not find player!");
				}
			} else {
				getBotManager().getTriviaManager().getTriviaBot().sendMessage("Could not find player!");
			}
		}
	}

	public TriviaRankCommand(BotManager botManager) {
		super(botManager, new String[] { "$trank", "$triviarank" });
	}
}
