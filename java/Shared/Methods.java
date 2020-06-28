package Shared;
import java.util.LinkedList;
import java.util.Random;

public class Methods {
	public static final Random RNG = new Random();

	public static final String urlizeDB(String s) {
		return s.substring(0, s.contains("(") ? s.indexOf("(") : s.length()).replace("'", "").trim().replace(" ", "_");
	}
	public static final String urlizeTT(String s) {
		s = s.toLowerCase();
		if(s.equalsIgnoreCase("cecil")) s = "cecil-dark-knight";
		if(s.equalsIgnoreCase("lann&reynn")) s = "lann-reynn";
		return s.substring(0, s.contains("(") ? s.indexOf("(") : s.length()).replace("'", "").trim().replace(" ", "-");
	}
	
	public static final LinkedList<String> splitString(String s, int size) {
		LinkedList<String> split = new LinkedList<String>();
		int spaceIndex = s.indexOf(" ");
		if(spaceIndex != -1)
			while(spaceIndex < s.length()) {
				int nextLineIndex = s.indexOf("\n", 0);
				if(nextLineIndex != -1 && nextLineIndex < size) {
					split.add(s.substring(0, nextLineIndex));
					split.addAll(splitString(s.substring(nextLineIndex + 1), size));
					return split;
				}
				int nextSpaceIndex = s.indexOf(" ", spaceIndex + 1);
				if(nextSpaceIndex == -1){
					if(s.length() < size)
						split.addFirst(s);
					else {
						split.add(s.substring(0, spaceIndex));
						split.add(s.substring(spaceIndex+1));
					}
					return split;
				}
				if(nextSpaceIndex < size)
					spaceIndex = nextSpaceIndex;
				else {
					split.add(s.substring(0, spaceIndex));
					split.addAll(splitString(s.substring(spaceIndex + 1), size));
					return split;
				}
			}
		split.add(s);
		return split;
	}

	public static final String replaceLast(String string, String toReplace, String replacement) {
	    int pos = string.lastIndexOf(toReplace);
	    if (pos > -1)
	        return string.substring(0, pos)
	             + replacement
	             + string.substring(pos + toReplace.length(), string.length());
	    else
	        return string;
	}

	@Deprecated
	/**
	 * To be used only in names and descriptions to make it easier to find a valid text, following the order: GL > EN > JP
	 */
	public static final String getBestText(String[] texts) {
		return (texts[1].length() > 0 ? texts[1] : texts[0].length() > 0 ? texts[0] : texts[2].length() > 0 ? texts[2] : "WTF NoName").replace("''", "'");
	}
}