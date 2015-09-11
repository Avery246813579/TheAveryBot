package com.frostbyte.theaverybot.debug;

public abstract class ConsoleCommand {
	private String command;
	
	public ConsoleCommand(String command){
		this.command = command;

		Console.commands.add(this);
	}
	
	public abstract void onCommand(String[] args);
	
	public String getCommand(){
		return command;
	}
}
