package com.frostbyte.theaverybot.util;

public enum Numbers {
	FIRST(1), SECOND(2), THIRD(3), FOURTH(4), FIFTH(5);

	private int number;

	private Numbers(int number) {
		this.number = number;
	}

	public static String getWord(int number) {
		for (Numbers numbers : values()) {
			if(numbers.number == number){
				return numbers.toString();
			}
		}
		
		return null;
	}
}
