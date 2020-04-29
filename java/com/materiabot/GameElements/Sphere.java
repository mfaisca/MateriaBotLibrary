package com.materiabot.GameElements;

public class Sphere{
	public enum SphereType {
		A("Attack"), B("Defense"), C("Recovery"), D("Support"), E("Jamming");	
		
		private SphereType(String name) { this.name = name; }
		
		private String name;
		
		public String getName() { return name; }
		public String getEmoteLetter() { return "letter_" + this.name(); }
		public String getEmoteSphere() { return "sphere_" + this.name(); }
		public String getEmoteSphereLetter() { return "sphereLetter_" + this.name(); }
		public String getEmoteSlot(int slot) { return "slot_" + this.name() + slot; }
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