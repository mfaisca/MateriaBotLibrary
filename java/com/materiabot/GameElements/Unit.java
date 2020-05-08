package com.materiabot.GameElements;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import com.materiabot.GameElements.Sphere.SphereType;
import com.materiabot.GameElements.Datamining.Ailment;

public class Unit {	
	private String name;
	private List<String> nicknames = new LinkedList<String>();
	private Crystal crystal;
	private Equipment.Type equipmentType;
	private Integer[] baseAbilities = new Integer[8];
	private List<Ability.UpgradedAbility> upgradedAbilities = new LinkedList<Ability.UpgradedAbility>();
	private HashMap<Integer, Ability> abilities = new HashMap<Integer, Ability>();
	private HashMap<Integer, Passive> passives = new HashMap<Integer, Passive>();
	private HashMap<Integer, Ailment> ailments = new HashMap<Integer, Ailment>();
	private List<Equipment> equipment = new LinkedList<Equipment>();
	private List<Artifact> artifacts = new LinkedList<Artifact>();
	private SphereType[] sphereSlots = new SphereType[3];
	private Sphere weaponSphere, basicSphere;

	public Unit(String name, String... nicknames) {
		this.name = name;
		this.nicknames.add(name.toLowerCase());
		this.nicknames.addAll(Arrays.asList(nicknames).stream().map(s -> s.toLowerCase()).collect(Collectors.toList()));
	}
	
	public String getName() { return name; }
	public List<String> getNicknames() { return nicknames; }
	public Crystal getCrystal() { return crystal; }
	public void setCrystal(Crystal c) { crystal = c; }
	public Equipment.Type getEquipmentType() { return equipmentType; }
	public void setEquipmentType(Equipment.Type t) { equipmentType = t; }
	public List<Ability.UpgradedAbility> getUpgradedAbilities() { return upgradedAbilities; }
	public HashMap<Integer, Ability> getAbilities() { return abilities; }
	public HashMap<Integer, Passive> getPassives() { return passives; }
	public HashMap<Integer, Ailment> getAilments() { return ailments; }
	public List<Equipment> getEquipment() { return equipment; }
	public List<Artifact> getArtifacts() { return artifacts; }
	public SphereType[] getSphereSlots() { return sphereSlots; }
	public Sphere getWeaponSphere() { return weaponSphere; }
	public Sphere getBasicSphere() { return basicSphere; }
	public void setSpheres(Sphere weapon, Sphere basic) { this.basicSphere = basic; this.weaponSphere = weapon; }
	public void setBaseAbilities(Integer[] baseAbls) { baseAbilities = baseAbls; }
	
	public List<Ability> getAbility(Ability.Type type) {
		Iterator<Ability.UpgradedAbility> iter = upgradedAbilities.stream()
				.filter(ua -> ua.type.equals(type))
				.collect(Collectors.toCollection(LinkedList::new))
					.descendingIterator();
		if(!iter.hasNext()) 
			return baseAbilities.length > type.ordinal() ? Arrays.asList(abilities.get(baseAbilities[type.ordinal()])) : new LinkedList<Ability>();
		List<Ability.UpgradedAbility> ret = new LinkedList<Ability.UpgradedAbility>();
		Ability.UpgradedAbility last = iter.next();
		while(iter.hasNext()) {
			Ability.UpgradedAbility cur = iter.next();
			if(cur.reqExtendPassives.equals(last.reqExtendPassives) && 
				cur.reqWeaponPassives.equals(last.reqWeaponPassives))
					ret.add(0, cur);
		}
		ret.add(last);
		return ret.stream().map(ua -> ua.upgrade).collect(Collectors.toList());
	}
	public Passive getPassive(int level) {
		return passives.entrySet().stream()
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
	public Passive findPassive(String requestedInfo) {
		if(StringUtils.isNumeric(requestedInfo))
			return getPassive(Integer.parseInt(requestedInfo));
		Equipment w = null;
		if(requestedInfo.toUpperCase().contains("LD"))
			w = getEquipment(Equipment.Rarity.W_LD);
		if(requestedInfo.toUpperCase().contains("EX+"))
			w = getEquipment(Equipment.Rarity.W_EXP);
		if(requestedInfo.toUpperCase().contains("EX"))
			w = getEquipment(Equipment.Rarity.W_EX);
		if(w != null) {
			int idx = Integer.parseInt(requestedInfo.substring(w.getRarity().equals(Equipment.Rarity.W_EXP) ? 2 : 1).trim());
			idx -= (w.getRarity().equals(Equipment.Rarity.W_EXP) ? 0 : 1);
			return w.getPassives().get(idx);
		}
		return null;
	}

	public String toString() {
		return this.getName();
	}
	public Unit clone() { //Shallow copy since this is all its required for the Managers
		Unit clone = new Unit(name, nicknames.toArray(new String[0]));
		clone.setCrystal(this.getCrystal());
		clone.setEquipmentType(this.getEquipmentType());
		clone.sphereSlots[0] = this.sphereSlots[0];
		clone.sphereSlots[1] = this.sphereSlots[1];
		clone.sphereSlots[2] = this.sphereSlots[2];
		return clone;
	}
}