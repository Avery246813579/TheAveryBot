package com.frostbyte.theaverybot.economics;

import java.util.ArrayList;
import java.util.List;

import com.frostbyte.theaverybot.bots.BotManager;

public class UserManager {
	private List<User> users = new ArrayList<User>();
	private UserThread userThread = new UserThread(this);
	private BotManager botManager;

	public UserManager(BotManager botManager) {
		this.botManager = botManager;
		
		userThread.start();
	}
	
	public void saveAll(){
		for(User user : users){
			saveUser(user);
		}
	}
	
	public void saveUser(User user){
		user.save();
		
		users.remove(user);
	}
	
	public User findUser(String name){
		User user = getUser(name);
		
		if(user != null){
			user.resetTime();
			return user;
		}
		
		user = new User(this, name);
		if(!user.isActive()){
			return null;
		}
		
		users.add(user);
		return user;
	}
	
	public User getUser(String name){
		for(User user : users){
			if(user.getName().equalsIgnoreCase(name)){
				return user;
			}
		}
		
		return null;
	}

	public void incrementLimit() {
		for (User user : users) {
			user.addTime();
		}
	}

	public List<User> getSaving() {
		List<User> saveList = new ArrayList<User>();

		for (User user : users) {
			if (user.timeExpired()) {
				saveList.add(user);
			}
		}

		return saveList;
	}

	public boolean needsSave() {
		return !getSaving().isEmpty();
	}

	public BotManager getBotManager() {
		return botManager;
	}
}
