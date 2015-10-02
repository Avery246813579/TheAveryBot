package com.frostbyte.theaverybot.commands.defaults.trivia;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.frostbyte.theaverybot.bots.BotManager;
import com.frostbyte.theaverybot.commands.Command;
import com.frostbyte.theaverybot.games.trivia.TriviaHandler;
import com.frostbyte.theaverybot.util.Numbers;

public class TriviaBoardCommand extends Command {
	@Override
	public void onCommand(String sender, String[] args) {
		List<Map.Entry<String, Integer>> entries = new LinkedList<Map.Entry<String, Integer>>(getBotManager().getTriviaManager().trivia.entrySet());

		Collections.sort(entries, new Comparator<Map.Entry<String, Integer>>() {
			public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
				return o2.getValue().compareTo(o1.getValue());
			}
		});

		getBotManager().getTriviaManager().trivia.clear();

		for (Map.Entry<String, Integer> entry : entries) {
			getBotManager().getTriviaManager().trivia.put(entry.getKey(), entry.getValue());
		}

		String message = TriviaHandler.trivias.get(getBotManager().getTriviaManager().getType_id()).getMessages().get("TRIVIA_BOARD");

		int slot = 1;
		for (Map.Entry<String, Integer> entry : getBotManager().getTriviaManager().trivia.entrySet()) {
			String key = entry.getKey();
			Integer value = entry.getValue();

			message = message.replaceAll("\\{" + Numbers.getWord(slot) + "_USER\\}", key);
			message = message.replaceAll("\\{" + Numbers.getWord(slot) + "_CREDITS\\}", Integer.toString(value));

			if (slot >= 5) {
				break;
			}

			slot++;
		}

		if (slot >= 4) {
			getBotManager().getTriviaManager().getTriviaBot().sendMessage(message);
		}else{
			getBotManager().getTriviaManager().getTriviaBot().sendMessage("Not enough users to display trivia leaderboard!");
		}
	}

	public TriviaBoardCommand(BotManager botManager) {
		super(botManager, new String[] { "$tboard", "$triviaboard" });
	}
}
