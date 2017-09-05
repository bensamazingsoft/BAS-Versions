package com.bas.versions.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageIncrementer {

	private static String regex = "\\[?\\(?\\d+\\)?\\]?\\.?$";
	private static Pattern pattern = Pattern.compile(regex);
	private static Matcher matcher;

	private static String regexForInt = "\\d+";
	private static Pattern patternForInt = Pattern.compile(regexForInt);
	private static Matcher matcherForInt;

	/**
	 * if string ends with number ex. 32 or 32. or (32). or [32]
	 * 
	 * @param msg
	 * @return
	 */
	public static String increment(String msg) {

		matcher = pattern.matcher(msg);
		// if msg ends with a number, maybe between parenthesis or brackets with
		// or without a period
		if (matcher.find()) {
			// find the int in the matched end of msg
			matcherForInt = patternForInt.matcher(matcher.group());
			if (matcherForInt.find()) {
				int id = Integer.parseInt(matcherForInt.group());
				id++;
				String result = msg.replace(matcherForInt.group(), String.valueOf(id));
				return result;
			}
		}
		// if msg does not end with a number
		return msg;
	}
}
