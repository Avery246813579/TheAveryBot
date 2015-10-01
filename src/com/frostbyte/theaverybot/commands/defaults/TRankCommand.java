package com.frostbyte.theaverybot.commands.defaults;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.frostbyte.theaverybot.bots.BotManager;
import com.frostbyte.theaverybot.commands.Command;
import com.frostbyte.theaverybot.games.trivia.TriviaHandler;
import com.frostbyte.theaverybot.sql.SqlHandler;

public class TRankCommand extends Command {
	@Override
	public void onCommand(String sender, String[] args) {
		System.out.println("Hi");
		
		if(args.length == 0){
			String message = TriviaHandler.trivias.get(getBotManager().getTriviaManager().getType_id()).
					getMessages().get("TRIVIA_RANK_COMMAND");

			message = message.replaceAll("{PLAYER_NAME}", sender);
			message = message.replaceAll("{RANK}", getBotManager().getTriviaManager().getValue(sender) + "");
			getBotManager().getTriviaManager().getTriviaBot().sendMessage(message);
		}else{
			Map<String, Object> twitch = new HashMap<String, Object>();
			twitch.put("username", args[0]);
			
			List<Map<String, Object>> twitchTables = SqlHandler.twitch_Accounts.get(twitch);
			if(!twitchTables.isEmpty()){
				Map<String, Object> currency = new HashMap<String, Object>();
				currency.put("twitch_id", twitchTables.get(0).get("twitch_id"));
				currency.put("channel", getBotManager().channel);
				
				if(!SqlHandler.currencies.get(currency).isEmpty()){
					String message = TriviaHandler.trivias.get(getBotManager().getTriviaManager().getType_id()).
							getMessages().get("TRIVIA_RANK_COMMAND");

					message = message.replaceAll("{PLAYER_NAME}", args[0]);
					message = message.replaceAll("{RANK}", getBotManager().getTriviaManager().getValue(args[0]) + "");
					getBotManager().getTriviaManager().getTriviaBot().sendMessage(message);
				}else{
					
				}
			}else{
				
			}
		}
	}

	public TRankCommand(BotManager botManager) {
		super(botManager, new String[] { "$trank", "$triviarank" });
	}
}
