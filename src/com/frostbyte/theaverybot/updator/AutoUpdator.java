package com.frostbyte.theaverybot.updator;

import com.frostbyte.theaverybot.debug.Console;

public abstract class AutoUpdator {
	public int version;
	public String name;

	public AutoUpdator(String name, int version) {
		this.version = version;
		this.name = name;
	}

	public AutoUpdator(String name, String version) {
		String sVersion = "";
		for (String string : version.split("\\.")) {
			sVersion = sVersion + string;
		}

		try {
			this.version = Integer.parseInt(sVersion);
		} catch (Exception ex) {
			Console.sendError("Could not parse " + name + " version in auto updator!");
		}

		this.name = name;
	}

	public void checkUpdate() {
		if (canUpdate()) {
			Console.sendLog(name + " has been updated!");
			update();
		}
	}

	public abstract boolean canUpdate();

	public abstract void update();
}
