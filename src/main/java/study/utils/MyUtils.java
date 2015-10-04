package study.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyUtils {
	static String linkRegex = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
	public static boolean isLink(String maybeLink){
		return isMatch(maybeLink,linkRegex);
	}
	private static boolean isMatch(String s, String pattern) {
		try {
			Pattern patt = Pattern.compile(pattern);
			Matcher matcher = patt.matcher(s);
			return matcher.matches();
		} catch (RuntimeException e) {
			return false;
		} 

	}
}
