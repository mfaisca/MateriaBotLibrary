package com.materiabot.GameElements;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import Shared.Methods;

public class Equipment{	
	public static enum Type{
		Dagger(), Sword(), Greatsword(), Staff(), Gun(), Fist(), ThrowingWeapon(),
		Spear(), Bow(), Whip(), Other(), 
		Armor(), Artifact(), BloomStone();
		
		public String getEmote() { return name() + "Equip"; }
		public String getTrashEmote() { return "Trash" + name(); }
		

		public static Type random3Star() {
			int rng = Methods.RNG.nextInt(8);
			if(rng == 7) rng = 11;
			return values()[rng];
		}
		public static Type random4Star() {
			int rng = Methods.RNG.nextInt(8);
			if(rng == 7) rng = 11;
			return values()[rng];
		}
		public static Type random5Star() {
			int rng = Methods.RNG.nextInt(11);
			return values()[rng];
		}
	}
	public static enum Rarity{
		W_4S(4, "silverWeapon", "4w", "rarity4", "4", "4w", "4*"), 
		W_15(5, "baseWeapon", "15", "15cp", "15", "15cp"), 
		W_35(5, "uniqueWeapon", "35", "35cp", "35", "35cp"), 
		W_WoI(5, "summonWeapon", "woi", "15cp", "woi", "summon"), 
		W_NT(5, "ntWeapon", "NT", "35cp", "nt"), 
		W_MW(5, "manikinWeapon", "dark", "15cp", "manikin", "mw", "dark", "shadow"), 
		W_EX(5, "exWeapon", "ex", "70cpSquare", "ex", "70", "70cp", "ex3", "ex2", "ex1"), 
		W_EXP(7, "realizedWeapon", "ex+", "100cpSquare", "ex+", "exp", "100", "100cp", "ex+3", "ex+2", "ex+1", "ex+0"), 
		W_LD(5, "limitedWeapon", "ld", "90cpSquare", "ld", "limited", "90", "90cp", "ld3", "ld2", "ld1"), 
		W_BT(10, "burstWeapon", "bt", "140cpSquare", "bt", "burst", "130", "130cp"),
		A_4S(4, "silverArmor", "4a", "rarity4", "4a"), A_35(5, "uniqueArmor", "35a", "15cp", "35a"), 
		A_90(5, "exArmor", "hg", "35cp", "90a"), A_90P(7, "realizedArmor", "hg+", "35cp", "90a+", "130a"), 
		A_7S(7, "highArmor", "7a", "haToken", "ha", "210a"),
		BS(0, "bloomStone", "bloom", "AdditionalAttackType", "bloom");
		
		private int rarity;
		private String name, imageName, emojiName;
		private List<String> names = new LinkedList<String>();
		
		private Rarity(int rarity, String name, String imageName, String emojiName, String... gearNames) { 
			this.rarity = rarity; this.name = name; this.imageName = imageName; this.emojiName = emojiName;
			names = Arrays.asList(gearNames);
		}

		public int getRarity() { return rarity; }
		public String getName() { return name; }
		public String getImageName() { return imageName; }
		public String getEmojiName() { return emojiName; }
		public List<String> getNames() { return names; }

		public static Rarity getByName(String s) {
			for(Rarity r : values())
				if(r.getName().equals(s))
					return r;
			return null;
		}
		public static Rarity getByTags(String s) {
			for(Rarity r : values())
				if(r.getNames().contains(s.toLowerCase()))
					return r;
			return null;
		}
	}
	
	private int id;
	private String name;
	private List<Passive> passives = new ArrayList<Passive>(1);
	private Type type;
	private Rarity rarity;
	private Unit unit;
	
	public int getId() { return id; }
	public void setId(int id) { this.id = id; }
	public String getName() { return name; }
	public String getSlugName() { return name.substring(0, name.indexOf("(")).trim().replace("'", "").replace("�", "c").replace(" ", "_"); }
	public void setName(String name) { this.name = name; }
	public Unit getUnit() { return unit; }
	public void setUnit(Unit unit) { this.unit = unit; }
	public List<Passive> getPassives() { return passives; }
	public Type getType() { return type; }
	public void setType(Type type) { this.type = type; }
	public Rarity getRarity() { return rarity; }
	public void setRarity(Rarity rarity) { this.rarity = rarity; }
}