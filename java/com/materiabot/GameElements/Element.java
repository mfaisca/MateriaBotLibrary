package com.materiabot.GameElements;
import java.awt.Color;
import java.util.LinkedList;
import java.util.List;

public enum Element{
	Null(0, 0, 0), Fire(221, 47, 41), Ice(4, 223, 238), 
	Thunder(240, 233, 76), Wind(131, 213, 116), 
	Earth(155, 97, 34), Water(27, 78, 242), 
	Dark(102, 49, 143), Holy(255, 254, 242);
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
	
	public static List<Element> get2(String text) {
		LinkedList<Element> ret = new LinkedList<Element>();
		for(int i = 0; i < text.length(); i++)
			if(text.charAt(text.length() - 1 - i) == '1')
				ret.add(values()[i]);
		return ret;
	}
}