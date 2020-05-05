package com.materiabot.GameElements;

public class Sphere{
	public enum SphereType {
		A("Attack"), B("Defense"), C("Recovery"), D("Support"), E("Jamming");	
		
		private SphereType(String name) { this.name = name; }
		
		private String name;
		
		public String getName() { return name; }
		public String getEmoteLetter() { return "sphereLetter" + this.name(); }
		public String getEmoteSphere() { return "sphere" + this.name(); }
		public String getEmoteSphereLetter() { return "sphere" + this.name() + "Letter"; }
		public String getEmoteShardLetter() { return "shard" + this.name() + "Letter"; }
		
		public static SphereType get(String c) {
			switch(c.toUpperCase()) {
				case "A": return A; case "B": return B;
				case "C": return C; case "D": return D;
				case "E": return E; }
			return null;
		}
	}
	private int id;
	private SphereType type;
	private Passive passive;
	
	public int getId() { return id; }
	public void setId(int id) { this.id = id; }
	public SphereType getType() { return type; }
	public void setType(SphereType type) { this.type = type; }
	public Passive getPassive() { return passive; }
	public void setPassive(Passive passive) { this.passive = passive; }
}