package com.materiabot.GameElements;
import java.util.LinkedList;
import java.util.List;

public class Ability {
	public static enum Type{
		BRV, HP, S1, S2, EX, AA, LD, BT;
	}
	public static class UpgradedAbility{
		public int id;
		public Ability original;
		public Ability upgrade;
		public Type type;
		public List<Integer> reqExtendPassives = new LinkedList<Integer>();
		public List<Integer> reqWeaponPassives = new LinkedList<Integer>();
	}
	
	private int id;
	private String name;
	private String description;
	private int useCount;
	private Type type;
	private Unit unit;
	
	public int getId() { return id; }
	public void setId(int id) { this.id = id; }
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	public String getDescription() { return description; }
	public void setDescription(String description) { this.description = description; }
	public int getUseCount() { return useCount; }
	public void setUseCount(int useCount) { this.useCount = useCount; }
	public Type getType() { return type; }
	public void setType(Type type) { this.type = type; }
	public Unit getUnit() { return unit; }
	public void setUnit(Unit unit) { this.unit = unit; }
}