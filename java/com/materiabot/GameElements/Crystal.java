package com.materiabot.GameElements;
import java.awt.Color;

public enum Crystal{
	Red(235, 70, 22), 
	Blue(0, 177, 220), 
	Green(0, 221, 0), 
	Yellow(224, 165, 0), 
	Black(94, 49, 187), 
	White(199, 199, 199);
	private Color c;
	
	private Crystal(int r, int g, int b) { c = new Color(r, g, b); }
	public Color getColor() { return c; }
	public String getEmote() { return getEmote(1); }
	public String getEmote(int tier) { return name() + "T" + tier; }
	
	public static Crystal find(String text) {
		if(text != null)
			for(Crystal crystal : values())
				if(crystal.name().equalsIgnoreCase(text))
					return crystal;
		return null;
	}
	public static Crystal find(int id) {
		return values()[id-1];
	}
}