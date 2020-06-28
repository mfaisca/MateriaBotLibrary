package com.materiabot.GameElements;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import com.materiabot.GameElements.Sphere.SphereType;

public class Unit {	
	private String name;
	private int series = -1;
	private List<String> nicknames = new LinkedList<String>();
	private Crystal crystal;
	private Equipment.Type equipmentType;
	private Integer[] baseAbilities = new Integer[8];
	private List<Ability.UpgradedAbility> upgradedAbilities = new LinkedList<Ability.UpgradedAbility>();
	private HashMap<Integer, Ability> abilities = new HashMap<Integer, Ability>();
	private HashMap<Integer, Passive> passives = new HashMap<Integer, Passive>();
	private HashMap<Integer, Passive> glPassives = new HashMap<Integer, Passive>();
	private List<Passive> charaBoards = new LinkedList<Passive>();
	private HashMap<Integer, Ailment> ailments = new HashMap<Integer, Ailment>();
	private List<Equipment> equipment = new LinkedList<Equipment>();
	private List<Artifact> artifacts = new LinkedList<Artifact>();
	private SphereType[] sphereSlots = new SphereType[3];
	private Sphere weaponSphere, basicSphere;

	public Unit(String name, String... nicknames) {
		this.name = name;
		this.nicknames.add(name.toLowerCase());
		if(nicknames != null)
			this.nicknames.addAll(Arrays.asList(nicknames).stream().map(s -> s.toLowerCase()).collect(Collectors.toList()));
	}
	
	public String getName() { return name; }
	public int getSeries() { return series; }
	public void setSeries(int series) { this.series = series; }
	public List<String> getNicknames() { return nicknames; }
	public Crystal getCrystal() { return crystal; }
	public void setCrystal(Crystal c) { crystal = c; }
	public Equipment.Type getEquipmentType() { return equipmentType; }
	public void setEquipmentType(Equipment.Type t) { equipmentType = t; }
	public List<Ability.UpgradedAbility> getUpgradedAbilities() { return upgradedAbilities; }
	public HashMap<Integer, Ability> getAbilities() { return abilities; }
	public HashMap<Integer, Passive> getJPPassives() { return passives; }
	public HashMap<Integer, Passive> getPassives() { return glPassives; }
	public List<Passive> getCharaBoards() { return charaBoards; }
	public HashMap<Integer, Ailment> getAilments() { return ailments; }
	public List<Equipment> getEquipment() { return equipment; }
	public List<Artifact> getArtifacts() { return artifacts; }
	public SphereType[] getSphereSlots() { return sphereSlots; }
	public Sphere getWeaponSphere() { return weaponSphere; }
	public Sphere getBasicSphere() { return basicSphere; }
	public void setSpheres(Sphere weapon, Sphere basic) { this.basicSphere = basic; this.weaponSphere = weapon; }
	public void setBaseAbilities(Integer[] baseAbls) { baseAbilities = baseAbls; }

	public List<Ability> getBaseAbility(Ability.Type type) {
		return Arrays.asList(abilities.get(baseAbilities[type.ordinal()]));
	}
	public List<Ability> getAbility(Ability.Type type) {
		return getAbility(type, null);
	}
	public List<Ability> getAbility(Ability.Type type, String region) {
		Passive awakening = type.equals(Ability.Type.S1) ? this.getPassive(55, region) :
							type.equals(Ability.Type.S2) ? this.getPassive(60, region) :
							type.equals(Ability.Type.AA) ? this.getPassive(70, region) : null;
		Iterator<Ability.UpgradedAbility> iter = upgradedAbilities.stream()
				.filter(ua -> ua.type.equals(type))
				.filter(ua -> awakening == null || ua.reqExtendPassives.stream().anyMatch(rp -> rp.intValue() == awakening.getId()))
				.sorted((u1, u2) -> u1.upgrade.getDescription().length() - u2.upgrade.getDescription().length())
				.collect(Collectors.toCollection(LinkedList::new))
					.descendingIterator();
		if(!iter.hasNext()) 
			return baseAbilities.length > type.ordinal() ? Arrays.asList(abilities.get(baseAbilities[type.ordinal()])) : new LinkedList<Ability>();
		List<Ability.UpgradedAbility> ret = new LinkedList<Ability.UpgradedAbility>();
		Ability.UpgradedAbility last = iter.next();
		ret.add(last);
		while(iter.hasNext()) {
			Ability.UpgradedAbility cur = iter.next();
			if(cur.reqExtendPassives.equals(last.reqExtendPassives) && 
				cur.reqWeaponPassives.equals(last.reqWeaponPassives) && 
				ret.stream().map(a -> a.upgrade.getName()).noneMatch(a -> a.equals(cur.upgrade.getName())))
					ret.add(0, cur);
		}
		return ret.stream().map(ua -> ua.upgrade).collect(Collectors.toList());
	}
	public Passive getPassive(int level) {
		return getPassive(level, null);
	}
	public Passive getPassive(int level, String region) {
		return ((region == null ? "GL" : region).equals("GL") ? getPassives() : getJPPassives())
				.entrySet().stream()
				.filter(e -> e.getValue().getLevel() == level)
				.map(e -> e.getValue())
				.findFirst().orElse(null);
	}

	public Passive getEquipmentPassive(Equipment.Rarity rarity) {
		return getEquipmentPassive(rarity, 0);
	}
	public Equipment getEquipment(Equipment.Rarity rarity) {
		return equipment.stream().filter(e -> e.getRarity().equals(rarity)).findFirst().orElse(null);
	}
	public Passive getEquipmentPassive(Equipment.Rarity rarity, int idx) {
		return equipment.stream().filter(e -> e.getRarity().equals(rarity)).findFirst().orElse(null).getPassives().get(idx);
	}
	
	public Ability getSpecificAbility(int id) {
		return abilities.get(id);
	}
	public Passive getSpecificPassive(int id) {
		return passives.get(id);
	}
	public Ailment getSpecificAilment(int id) {
		return ailments.get(id);
	}

	public String toString() {
		return this.getName();
	}
	public Unit clone() { //Shallow copy since this is all its required for the Managers
		Unit clone = new Unit(name);
		clone.nicknames = this.nicknames;
		clone.setCrystal(this.getCrystal());
		clone.setEquipmentType(this.getEquipmentType());
		clone.setSeries(this.getSeries());
		clone.sphereSlots = this.sphereSlots;
		return clone;
	}
}