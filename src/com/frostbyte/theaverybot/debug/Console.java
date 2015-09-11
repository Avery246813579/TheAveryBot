package com.frostbyte.theaverybot.debug;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.frostbyte.theaverybot.Main;
import com.frostbyte.theaverybot.debug.commands.TriviaCommand;

public class Console {
	public static List<ConsoleCommand> commands = new ArrayList<ConsoleCommand>();
	public static boolean log = true, debug = true;
	Scanner scanner = new Scanner(System.in);

	public Console() {
		new TriviaCommand();
		
		checkCommand();
	}

	public void checkCommand(){
		String nextLine= scanner.nextLine();
		
		System.out.println("Kitten");
		for(ConsoleCommand command : commands){
			String lowerCase = command.getCommand().toLowerCase();
			
			if(nextLine.startsWith("/" + lowerCase)){
				command.onCommand(nextLine.split(" "));
			}
		}
		
		checkCommand();
	}

	public static void sendLog(String message) {
		if (log) {
			System.out.println("TheAveryBot " + Main.version + " << LOG >> " + message);
		}
	}

	public static void sendError(String message) {
		if (debug) {
			System.out.println("TheAveryBot " + Main.version + " << ERROR >> " + message);
		}
	}

	public static void sendConsole(String message) {
		System.out.println("TheAveryBot " + Main.version + " << CONSOLE >> " + message);
	}
}
