package com.frostbyte.theaverybot.economics;

public class UserThread extends Thread {
	private UserManager userManager;

	public UserThread(UserManager userManager) {
		this.userManager = userManager;

		save();
	}

	public void save() {
		while (userManager.getBotManager().isEnabled()) {
			while (userManager.needsSave()) {
				for (User user : userManager.getSaving()){
					userManager.saveUser(user);
				}
			}
		}
	}
}
