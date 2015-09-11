package com.frostbyte.theaverybot.loading.bots;

import com.frostbyte.theaverybot.bots.BotManager;

public class ActiveBot {
	private int byte_id, account_id, bot_id;
	private BotManager botManager;

	public ActiveBot(int byte_id, int account_id, int bot_id) {
		this.byte_id = byte_id;
		this.account_id = account_id;
		this.bot_id = bot_id;
			
		this.botManager = new BotManager(this);
	}
	
	public int getByte_id() {
		return byte_id;
	}

	public void setByte_id(int byte_id) {
		this.byte_id = byte_id;
	}

	public int getAccount_id() {
		return account_id;
	}

	public void setAccount_id(int account_id) {
		this.account_id = account_id;
	}

	public int getBot_id() {
		return bot_id;
	}

	public void setBot_id(int bot_id) {
		this.bot_id = bot_id;
	}

	public BotManager getBotManager() {
		return botManager;
	}

	public void setBotManager(BotManager botManager) {
		this.botManager = botManager;
	}
}
