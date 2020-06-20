package com.materiabot.GameElements;
import java.awt.Color;

public enum Element{
	Fire(221, 47, 41), Ice(4, 223, 238), Lightning(240, 233, 76), Thunder(240, 233, 76), Earth(155, 97, 34), 
	Water(27, 78, 242), Wind(131, 213, 116), Holy(255, 255, 255), Dark(102, 49, 143);
	private Color c;
	
	private Element(int r, int g, int b) { c = new Color(r, g, b); }
	
	public Color getColor() { return c; }
	public String getEmote() { return name() + "Element"; }

	public static Element get(String text) {
		for(Element element : values())
			if(element.name().equalsIgnoreCase(text))
				return element;
		return null;
	}
	public static Element get(int val) {
		return Element.values()[val-1];
	}
}