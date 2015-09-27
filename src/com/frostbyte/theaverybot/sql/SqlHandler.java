package com.frostbyte.theaverybot.sql;

import com.frostbyte.theaverybot.sql.frostbyte.Accounts;
import com.frostbyte.theaverybot.sql.theaverybot.Commands;
import com.frostbyte.theaverybot.sql.theaverybot.Currencies;
import com.frostbyte.theaverybot.sql.theaverybot.bots.Bot_Settings;
import com.frostbyte.theaverybot.sql.theaverybot.bots.Bot_Type;
import com.frostbyte.theaverybot.sql.theaverybot.bots.Byte_Bots;
import com.frostbyte.theaverybot.sql.theaverybot.trivia.Trivia_Questions;
import com.frostbyte.theaverybot.sql.theaverybot.trivia.Trivia_Settings;
import com.frostbyte.theaverybot.sql.theaverybot.trivia.Trivia_Type;
import com.frostbyte.theaverybot.sql.thirdparty.Twitch_Accounts;

public class SqlHandler extends com.frostbyte.javasqlapi.SqlHandler{
	public static Currencies currencies = new Currencies();
	public static Twitch_Accounts twitch_Accounts = new Twitch_Accounts();
	public static Trivia_Questions trivia_Questions = new Trivia_Questions();
	public static Trivia_Type trivia_Type = new Trivia_Type();
	public static Accounts accounts = new Accounts();
	public static Byte_Bots byte_Bots = new Byte_Bots();
	public static Bot_Type bot_Type = new Bot_Type();
	public static Commands commands = new Commands();
	public static Trivia_Settings trivia_Settings = new Trivia_Settings();
	public static Bot_Settings bot_Settings = new Bot_Settings();
	
	public SqlHandler() {
		SQL_HOST = "127.0.0.1";
		//SQL_PASS = "t]T}1a!@()90";
		//SQL_USER = "frostbyt_site";
		SQL_PASS = "";
		SQL_USER = "root";

		log = false;
		console_errors = true;

		currencies.createTable();
		twitch_Accounts.createTable();
		trivia_Questions.createTable();
		trivia_Type.createTable();
		accounts.createTable();
		byte_Bots.createTable();
		bot_Type.createTable();
		commands.createTable();
		trivia_Settings.createTable();
		bot_Settings.createTable();
	}
}
