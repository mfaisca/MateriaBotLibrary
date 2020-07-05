package com.materiabot.IO.JSON;
import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import com.materiabot.GameElements.Ability;
import com.materiabot.GameElements.Ability.Type;
import com.materiabot.GameElements.Ailment;
import com.materiabot.GameElements.Artifact;
import com.materiabot.GameElements.Crystal;
import com.materiabot.GameElements.Equipment;
import com.materiabot.GameElements.Passive;
import com.materiabot.GameElements.Sphere;
import com.materiabot.GameElements.Sphere.SphereType;
import com.materiabot.GameElements.Unit;
import com.materiabot.GameElements.Ailment.Target;
import com.materiabot.IO.JSON.JSONParser.MyJSONObject;
import com.materiabot.IO.JSON.Unit.AbilityParser;
import com.materiabot.IO.JSON.Unit.PassiveParser;
import Shared.Methods;

public class UnitParser {
	public static interface OverrideManager{
		public static class _Default extends Unit{
			public _Default(String name, String... nicknames) {
				super(name, nicknames);
			}
		}

		public Unit getUnit(String name);
		public List<Unit> getAllUnits();
	}
	public static List<OverrideManager> overrideManagerCollection = new LinkedList<OverrideManager>();
	public static List<Unit> UNITS = new LinkedList<Unit>();
		
	public Unit parseUnit(String name) {
		return createUnit(name);
	}
	private Unit createUnit(String name) {
		try{
			Unit u = UNITS.stream()
						.filter(uu -> uu.getNicknames().contains(name.replace("_", " ").toLowerCase()))
						//.map(uu -> uu.clone())
						.findFirst().orElse(new Unit(name));
			File f = new File("./resources/units/db_" + Methods.urlizeDB(u.getName()).toLowerCase() + ".json");
			if(!f.exists()) return null;
			MyJSONObject obj = JSONParser.loadContent(f.getAbsolutePath(), false);
			parseProfile(u, obj);
			parseCompleteListAbilities(u, obj);
			parseBaseAbilities(u, obj);
			parseDefaultAilments(u, obj);
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
	private void parseProfile(Unit u, MyJSONObject obj) {
		u.setCrystal(Crystal.find(obj.getObject("profile").getInt("crystal")));
		u.setEquipmentType(Equipment.Type.find(obj.getObject("profile").getInt("weaponType")));
		u.setSeries(obj.getObject("profile").getInt("world"));
		for(int i = 0; i < 3; i++)
			u.getSphereSlots()[i] = SphereType.get(obj.getObject("profile").getObject("traits").getStringArray("spheres")[i]);
	}
	private void parseBaseAbilities(Unit u, MyJSONObject obj) {
		u.setBaseAbilities(obj.getIntArray("defaultAbilities"));
		int typeIdx = -1;
		for(int id : u.getBaseAbilities())
			u.getSpecificAbility(id).setType(Type.values()[++typeIdx]);
	}
	private void parseCompleteListAbilities(Unit u, MyJSONObject obj) {
		new AbilityParser(u).parseAbilities(obj, "completeListOfAbilities");
	}
	private void parseDefaultAilments(Unit u, MyJSONObject obj) {
		for(MyJSONObject ailment : obj.getObjectArray("defaultAilments")) {
			Ailment ail = new Ailment();
			ail.setId(ailment.getInt("id"));
			ail.setCastId(ailment.getInt("cast_id"));
			if(ailment.getObject("name") == null) continue; //FIXME Temporary Fix for a Prishe bugged DefaultAilment
			ail.setName(Methods.getBestText(ailment.getStringArray(ailment.getObject("name"))));
			ail.setDescription(Methods.getBestText(ailment.getStringArray(ailment.getObject("desc"))));
			ail.setRate(ailment.getObject("meta_data").getInt("rate"));
			ail.setRank(ailment.getObject("meta_data").getInt("rank"));
			ail.setTarget(Target.get(ailment.getObject("meta_data").getInt("target")));
			ail.setDuration(ailment.getObject("meta_data").getInt("duration"));
			ail.setArgs(Arrays.asList(ailment.getObject("meta_data").getIntArray("arguments")).stream().mapToInt(i->i).toArray());
			//TODO Parse Ailments here
			u.getAilments().put(ail.getId(), ail);
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
		for(Passive p : new PassiveParser().parsePassives(obj, "awakeningPassives")) {
			p.setUnit(u);
			u.getJPPassives().put(p.getLevel(), p);
			u.getPassives().put(p.getLevel(), p);
		}
		for(Passive p : new PassiveParser().parsePassives(obj, "glAwakeningPassives")) {
			p.setUnit(u);
			u.getPassives().put(p.getLevel(), p);
		}
	}
	private void parseCharaBoards(Unit u, MyJSONObject obj) {
		if(obj.getObject("enhancementBoard") == null)
			return;
		for(Passive p : new PassiveParser().parsePassives(obj.getObject("enhancementBoard"), "passives")) {
			p.setUnit(u);
			u.getCharaBoards().add(p);
		}
	}
	private void parseArtifacts(Unit u, MyJSONObject obj) {
		for(MyJSONObject pass : obj.getObjectArray("artifactList")) {
			Artifact a = new Artifact();
			a.setId(pass.getInt("id"));
			a.setName(Methods.getBestText(pass.getStringArray(pass.getObject("name"))));
			a.setDescription(Methods.getBestText(pass.getStringArray(pass.getObject("desc"))).replace("\\n", System.lineSeparator()));
			a.setShortDescription(Methods.getBestText(pass.getStringArray(pass.getObject("short_desc"))).replace("\\n", System.lineSeparator()));
			u.getArtifacts().add(a);
		}
	}
	private void parseGear(Unit u, MyJSONObject obj) {
		PassiveParser pp = new PassiveParser();
		for(String gearType : Arrays.asList("silverWeapon", "baseWeapon", 
				"uniqueWeapon", "summonWeapon", "ntWeapon", "manikinWeapon", "exWeapon", "realizedWeapon", 
				"limitedWeapon", "burstWeapon", "silverArmor", "uniqueArmor", "exArmor", "realizedArmor", "highArmor")) {
			MyJSONObject gear = obj.getObject("gearList").getObject(gearType);
			if(gear.getInt("id") == null) continue;
			Equipment equip = new Equipment();
			equip.setId(gear.getInt("id"));
			equip.setName(Methods.getBestText(gear.getStringArray(gear.getObject("name"))).replace("\\bQp", "+"));
			equip.setType(gearType.contains("Armor") ? Equipment.Type.Armor : u.getEquipmentType());
			equip.setRarity(Equipment.Rarity.getByName(gearType));
			equip.setUnit(u);
			Passive p = pp.parsePassive(gear.getObject("passive"));
			if(p != null) {
				p.setUnit(u);
				equip.getPassives().add(p);
			}
			if(gear.getObjectArray("passives") != null)
				for(Passive ppp : pp.parsePassives(gear, "passives")) {
					ppp.setUnit(u);
					equip.getPassives().add(ppp);
				}
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
		equip.getPassives().add(new PassiveParser().parsePassive(gearPassive));
//		Passive p = new Passive();
//		p.setId(gearPassive.getInt("id"));
//		p.setName(Methods.getBestText(gearPassive.getStringArray(gearPassive.getObject("name"))));
//		p.setDescription(Methods.getBestText(gearPassive.getStringArray(gearPassive.getObject("desc"))).replace("\\n", System.lineSeparator()));
//		p.setShortDescription(Methods.getBestText(gearPassive.getStringArray(gearPassive.getObject("short_desc"))).replace("\\n", System.lineSeparator()));
//		p.setCpCost(gearPassive.getObject("meta_data").getInt("cp"));
//		equip.getPassives().add(p);
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
			sphere.setPassive(new PassiveParser().parsePassive(gearPassive));
			s2 = s1 == null ? null : sphere;
			s1 = s1 == null ? sphere : s1;
		}
		u.setSpheres(s1, s2);
	}
}