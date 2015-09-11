package com.frostbyte.theaverybot.util;

public class ObjectUtil {
	public static int objectToInt(Object object){
		int integer = (Integer) object;
		return integer;
	}
	
	public static Object intToObject(int integer){
		Object object = (Object) integer;
		return object;
	}
	
	public static String objectToString(Object object){
		String string = (String) object;
		return string;
	}
}
