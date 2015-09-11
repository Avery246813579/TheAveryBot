package com.frostbyte.theaverybot.debug.commands;

import com.frostbyte.theaverybot.debug.Console;
import com.frostbyte.theaverybot.debug.ConsoleCommand;
import com.frostbyte.theaverybot.loading.bots.BotLoading;

public class TriviaCommand extends ConsoleCommand{
	public TriviaCommand() {
		super("trivia");
	}

	@Override
	public void onCommand(String[] args) {
		if(args.length <= 2){
			Console.sendConsole("/Trivia (Toggle) (Status)");
		}else{
			switch(args[1].toLowerCase()){
			case "toggle":
				try{
					boolean status = Boolean.parseBoolean(args[2]);
					BotLoading.bots.get(0).getBotManager().getTriviaManager().setEnabled(status);
					Console.sendConsole("Status of trivia set to: " + status);
				}catch(Exception e){
					Console.sendError("Could not parse boolean!");
				}
				break;
			}
		}
	}
}
