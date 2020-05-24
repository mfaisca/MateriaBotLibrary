package com.materiabot.IO.JSON;
import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.google.common.base.CharMatcher;
import com.materiabot.GameElements.Ability;
import com.materiabot.GameElements.Artifact;
import com.materiabot.GameElements.Equipment;
import com.materiabot.GameElements.Passive;
import com.materiabot.GameElements.Sphere;
import com.materiabot.GameElements.Sphere.SphereType;
import com.materiabot.GameElements.Unit;
import com.materiabot.GameElements.Datamining.Ailment;
import com.materiabot.IO.JSON.JSONParser.MyJSONObject;
import com.materiabot.IO.JSON.Unit.PassiveParser;
import Shared.Methods;

public class UnitParser {
	public static interface OverrideManager{		
		public Unit getUnit(String name);
		public Unit getRandomUnit();
	}
	public static List<OverrideManager> overrideManagerCollection = new LinkedList<OverrideManager>();
	private String region;
	
	public UnitParser(String r) { region = r.toLowerCase(); }
	
	public Unit parseUnit(String name) {
		try {
			return createUnit(name);
		} catch(Exception e) {
			
		}
		return null;
	}
	private static Unit getUnit(String name) {
		for(OverrideManager m : overrideManagerCollection) {
			Unit u = m.getUnit(name);
			if(u != null) 
				return u;
		}
		return new Unit(name);
	}
	
	private Unit createUnit(String name) {
		try{
			Unit u = getUnit(name.replace("_", " "));
			File f = new File("./resources/" + region.toLowerCase() + "/tl_" + Methods.urlizeDB(u.getName()).toLowerCase() + ".json");
			if(!f.exists()) return null;
			MyJSONObject obj = JSONParser.loadContent(f.getAbsolutePath(), false);		
			//int[] baseSkillIds = ArrayUtils.toPrimitive(obj.getIntArray("defaultAbilities"));
			parseBaseAbilities(u, obj);
			parseCompleteListAbilities(u, obj);
			parseOptionalAbilities(u, obj);
			parsePassives(u, obj);
			parseCharaBoards(u, obj);
			parseArtifacts(u, obj);
			parseGear(u, obj);
			parseSpheres(u, obj);
			return u;
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	private void parseBaseAbilities(Unit u, MyJSONObject obj) {
		u.setBaseAbilities(obj.getIntArray("defaultAbilities"));
	}
	private void parseCompleteListAbilities(Unit u, MyJSONObject obj) {
		for(MyJSONObject ab : obj.getObjectArray("completeListOfAbilities")) {
			if(ab.getInt("error") != null) continue;
			Ability a = new Ability();
			a.setId(ab.getInt("id"));
			a.setName(ab.getObject("name").getString(region));
			if(region.equals("gl") && !CharMatcher.ascii().matchesAllOf(a.getName()))
				continue;
			a.setDescription(ab.getObject("desc").getString(region).replace("\\n", System.lineSeparator()));
			a.setUnit(u);
			u.getAbilities().put(a.getId(), a);
			//TODO Parse HitData Here
			for(MyJSONObject ailment : ab.getObjectArray("ailments")) {
				Ailment ail = new Ailment();
				ail.setId(ailment.getInt("id"));
				ail.setCastId(ailment.getInt("cast_id"));
				ail.setName(ailment.getObject("name").getString(region));
				ail.setDescription(ailment.getObject("desc").getString(region));
				ail.setRate(ailment.getObject("meta_data").getInt("rate"));
				ail.setRank(ailment.getObject("meta_data").getInt("rank"));
				ail.setTarget(Ailment.Target.get(ailment.getObject("meta_data").getInt("target")));
				ail.setDuration(ailment.getObject("meta_data").getInt("duration"));
				ail.setArgs(Arrays.asList(ailment.getObject("meta_data").getIntArray("arguments")).stream().mapToInt(i->i).toArray());
				//TODO Parse Ailments here
				//TODO Associate ailments to abilities so you can find what ailments an ability applies
				u.getAilments().put(ail.getId(), ail);
			}
		}
	}
	private void parseOptionalAbilities(Unit u, MyJSONObject obj) {
		int typeIdx = -1;
		for(MyJSONObject[] skill : obj.getArrayArray("optionalAbilities")) {
			typeIdx++;
			for(MyJSONObject skillLevel : skill) {
				try {
					Ability.UpgradedAbility ua = new Ability.UpgradedAbility();
					ua.id = skillLevel.getInt("id");
					ua.original = u.getSpecificAbility(skillLevel.getInt("originalAbility"));
					ua.upgrade = u.getSpecificAbility(skillLevel.getInt("upgradedAbility"));
					ua.type = Ability.Type.values()[typeIdx];
					ua.reqExtendPassives.addAll(Arrays.asList(skillLevel.getIntArray("reqExtendPassives")));
					ua.reqWeaponPassives.addAll(Arrays.asList(skillLevel.getIntArray("reqWeaponPassives")));
					ua.original.setType(ua.type);
					ua.upgrade.setType(ua.type);
					u.getUpgradedAbilities().add(ua);
				} catch(Exception e) {
					System.out.println("Error loading \"" + u.getName() + "\" OptionalAbility ID: " + skillLevel.getInt("id") + " - This is probably fine, just JP abilities being filtered");
					continue;
				}
			}
		}
	}
	private void parsePassives(Unit u, MyJSONObject obj) {
		for(Passive p : new PassiveParser(region).parsePassives(obj, "awakeningPassives")) {
			p.setUnit(u);
			u.getPassives().put(p.getLevel(), p);
		}
	}
	private void parseCharaBoards(Unit u, MyJSONObject obj) {
		if(obj.getObject("enhancementBoard") == null)
			return;
		for(Passive p : new PassiveParser(region).parsePassives(obj.getObject("enhancementBoard"), "passives")) {
			p.setUnit(u);
			u.getCharaBoards().add(p);
		}
	}
	private void parseArtifacts(Unit u, MyJSONObject obj) {
		for(MyJSONObject pass : obj.getObjectArray("artifactList")) {
			Artifact a = new Artifact();
			a.setId(pass.getInt("id"));
			a.setName(pass.getObject("name").getString(region));
			a.setDescription(pass.getObject("desc").getString(region).replace("\\n", System.lineSeparator()));
			a.setShortDescription(pass.getObject("short_desc").getString(region).replace("\\n", System.lineSeparator()));
			u.getArtifacts().add(a);
		}
	}
	private void parseGear(Unit u, MyJSONObject obj) {
		for(String gearType : Arrays.asList("silverWeapon", "baseWeapon", 
				"uniqueWeapon", "summonWeapon", "ntWeapon", "manikinWeapon", "exWeapon", "realizedWeapon", 
				"limitedWeapon", "burstWeapon", "silverArmor", "uniqueArmor", "exArmor", "realizedArmor", "highArmor")) {
			MyJSONObject gear = obj.getObject("gearList").getObject(gearType);
			if(gear.getInt("id") == null) continue;
			Equipment equip = new Equipment();
			equip.setId(gear.getInt("id"));
			equip.setName(gear.getObject("name").getString(region).replace("\\bQp", "+"));
			if(region.equals("gl") && !CharMatcher.ascii().matchesAllOf(equip.getName()))
				continue;
			equip.setType(gearType.contains("Armor") ? Equipment.Type.Armor : u.getEquipmentType());
			equip.setRarity(Equipment.Rarity.getByName(gearType));
			equip.setUnit(u);
			PassiveParser pp = new PassiveParser(region);
			{
//				MyJSONObject gearPassive = gear.getObject("passive");
				Passive p = pp.parsePassive(gear.getObject("passive"));
//				Passive p = new Passive();
//				p.setId(gearPassive.getInt("id"));
//				p.setName(gearPassive.getObject("name").getString(region));
//				p.setDescription(gearPassive.getObject("desc").getString(region).replace("\\n", System.lineSeparator()));
//				p.setShortDescription(gearPassive.getObject("short_desc").getString(region).replace("\\n", System.lineSeparator()));
//				p.setCpCost(gearPassive.getObject("meta_data").getInt("cp"));
				p.setUnit(u);
				//p.generateDescription();
				equip.getPassives().add(p);
			}
			if(gear.getObjectArray("passives") != null) {
				for(Passive p : pp.parsePassives(gear, "passives")) {
					p.setUnit(u);
					p.generateDescription();
					equip.getPassives().add(p);
				}
			}
//				for(MyJSONObject gearPassive : gear.getObjectArray("passives")) {
//					Passive p = new Passive(_Library.get(region));
//					p.setId(gearPassive.getInt("id"));
//					p.setName(gearPassive.getObject("name").getString(region));
//					p.setDescription(gearPassive.getObject("desc").getString(region).replace("\\n", System.lineSeparator()));
//					p.setShortDescription(gearPassive.getObject("short_desc").getString(region).replace("\\n", System.lineSeparator()));
//					p.setCpCost(gearPassive.getObject("meta_data").getInt("cp"));
//					equip.getPassives().add(p);
//					//TODO Missing Effects (Mainly for passive stat increases on EX+ and LD)
//				}
			u.getEquipment().add(equip);
		}
		MyJSONObject gear = obj.getObject(Equipment.Rarity.BS.getName());
		Equipment equip = new Equipment();
		equip.setId(gear.getInt("id"));
		equip.setName("Bloom Stone");
		equip.setType(Equipment.Type.BloomStone);
		equip.setRarity(Equipment.Rarity.BS);
		equip.setUnit(u);
		MyJSONObject gearPassive = gear.getObject("passive");
		Passive p = new Passive();
		p.setId(gearPassive.getInt("id"));
		p.setName(gearPassive.getObject("name").getString(region));
		p.setDescription(gearPassive.getObject("desc").getString(region).replace("\\n", System.lineSeparator()));
		p.setShortDescription(gearPassive.getObject("short_desc").getString(region).replace("\\n", System.lineSeparator()));
		p.setCpCost(gearPassive.getObject("meta_data").getInt("cp"));
		equip.getPassives().add(p);
		u.getEquipment().add(equip);
	}
	private void parseSpheres(Unit u, MyJSONObject obj) {
		Sphere s1 = null, s2 = null;
		for(String s : Arrays.asList("bonusSphere", "craftedSphere")) {
			MyJSONObject gear = obj.getObject(s);
			if(gear.getInt("id") == null) continue;
			Sphere sphere = new Sphere();
			sphere.setId(gear.getInt("id"));
			sphere.setType(SphereType.valueOf(gear.getString("category")));
			MyJSONObject gearPassive = gear.getObject("passive");
			sphere.setPassive(new PassiveParser(region).parsePassive(gearPassive));
			s2 = s1 == null ? null : sphere;
			s1 = s1 == null ? sphere : s1;
		}
		u.setSpheres(s1, s2);
	}
}