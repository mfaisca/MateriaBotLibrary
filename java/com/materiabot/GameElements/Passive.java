package com.materiabot.GameElements;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import com.materiabot.IO.JSON.JSONParser;

import Shared.BotException;
import Shared.Dual;
import Shared.Methods;

public class Passive{
	public static enum Target {
		T2(2, "Self", ""),
		T3(3, "Party", "party ");
		
		private int id;
		private String name, description;
		
		private Target(int id, String name, String description) {
			this.id = id;
			this.name = name;
			this.description = description;
		}

		public int getId() {
			return id;
		}
		public String getName() {
			return name;
		}
		public String getDescription() {
			return description;
		}
		
		public static Target get(int id) {
			for(Target t : values())
				if(t.getId() == id)
					return t;
			return null;
		}
	}
	public static enum Type{
		BRV(1, "BRV Attack"),
		HP(2, "HP Attack"),
		None(6, "None"),
		Unknown1(7, "Unknown1"),
		Unknown2(11, "Unknown2"),
		Unknown3(14, "Unknown3");

		private int id;
		private String description;
		
		private Type(int id, String desc) {
			this.id = id;
			this.description = desc;
		}
		
		public int getId() {
			return id;
		}
		public String getDescription() {
			return description;
		}
		
		public static Type get(int id) {
			return Arrays.asList(values()).stream().filter(t -> t.getId() == id).findFirst().orElse(null);
		}
	}
	public static enum MonsterType {
		Wolf(4, "Wolf"),
		Skeleton(7, "Skeleton");
		
		private String name;
		private int id;
		
		private MonsterType(int id, String name) {
			this.id = id;
			this.name = name;
		}
		
		public int getId() { return id; }
		public String getName() { return name; }
		
		public static MonsterType getById(int id) {
			for(MonsterType mt : values())
				if(mt.getId() == id)
					return mt;
			return null;
		}
	}
	public static enum Effect{
		E11(11, "Deals +{0}% BRV damage when using group attacks on 1 target"),
		E17(17, "Raises {3}ATK by {0}% up to {1} times"),
		E19(19, ""), //Basch Only, no parameters, Passive effect is covered fully by other effect ID
		E22(22, "「**{0}**」use +{1}"),
		E28(28, "Raises {3}ATK{4} by {0}%"),
		E29(29, "Raises {3}ATK{4} by {0}%{1}"),
		E30(30, "Raises {3}DEF{4} by {0}%"),
		E31(31, "Raises {3}DEF{4} by {0}%{1}"),
		E32(32, "Raises {3}Speed{4} by {0}%"),
		E33(33, "Raises {3}Speed{4} by {0}%{1}"),
		E34(34, "Raises {3}Int BRV{4} by {0}%"),
		E35(35, "Raises {3}Int BRV{4} by {0}%{1}"), //Doesnt Exist, just a guess
		E36(36, "Raises {3}Max BRV{4} by {0}%"),
		E37(37, "Raises {3}Max BRV{4} by {0}%{1}"),
		E39(39, "Raises BRV Damage by {0}%"),
		E40(40, "Raises turn rate"), //Only receives 1 parameter, but I don't know how to have it make sense for the end user
		E43(43, "Raises BRV by {0}% of Int BRV"), //Receives a 2nd Parameter "1", which probably indicates what stat its based on, but all examples refer to 1, so I'm removing that variable for ease of coding
		E47(47, "Raises {3}ATK, DEF, Int BRV, Max BRV by {0}%{1}"), //The second parameter is usually -1 to represent infinite turns, but I'll leave it to be changeable through "fix"
		E48(48, "Raises turn rate of {0}"), //Receives 2 parameters, but I don't know how to have 2nd make sense for the end user (Its usually 4)
		E49(49, "Break is avoided and BRV drops to 1"),
		E53(53, "Guarantees hit"),
		E54(54, "Extend {0} buffs on the party by {1} turn(s)"),
		E56(56, "Raises {3}DEF by {0}%"),
		E60(60, "Raises BRV Damage dealt to {0} by {1}%"),
		E61(61, "BRV hits become critical hits"),
		E62(62, "KO is avoided and HP drops to 1"),
		E63(63, "Raises evasion by {0}%"),
		E64(64, "Grants {0}% Break Bonus"),
		E66(66, "{0}% chance to apply {1} for {2} turns"),
		E67(67, "Lowers chance to get poisoned by {1}%"),
		E68(68, "Lowers chance to get debuffed for {1}% by {2} turns"),
		E72(72, "Raises ATK by {0}% per female party member"),
		E73(73, "Raises party ATK, DEF, Int BRV, Max BRV by {0}%"), //The second parameter is usually -1 to represent infinite turns, but I'll leave it to be changeable through "fix"
		E76(76, "Raises HP recovered by {0}%"),
		E77(77, "HP recovered in excess of Max HP added to BRV up to {1}% Int BRV"), //Has a first parameter with 100, but I'm not sure what it means
		E78(78, "Raises BRV Damage dealt to humanoids by {0}%"), //Maneater, unique to Steiner
		E79(79, "Counters with a BRV Attack"), //
		E82(82, "{0}% of HP recovered in excess of MAX HP shared with allies up to {1}% of the ally's Max HP"), 
		E89(89, "Raises ATK, DEF, Int BRV, Max BRV by {0}%"), //Another one of these
		E90(90, "Raises {3}ATK, DEF, Int BRV, Max BRV by {0}%{1}"), //The second parameter is usually -1 to represent infinite turns, but I'll leave it to be changeable through "fix"
		E102(102, "Ignores resistances against ghost-type enemies"), //Unique to Sabin
		E115(115, "Raises Max HP by {0}%"),
		;
		
		private int id;
		private String baseDescription;
		
		private Effect(int id, String desc) {
			this.id = id;
			this.baseDescription = desc;
		}
		
		public int getId() { return id; }
		public String getBaseDescription() { return baseDescription; }
		public String getDescription(Unit u, String target, Object... values) {
			Object[] oa = new Object[3];
			for(int i = 0; i < oa.length; i++)
				oa[i] = ((Integer[])values[0])[i];
			values = fix(u, oa);
			String r = baseDescription;
			for(int i = 0; i < values.length; i++)
				r = r.replace("{" + i + "}", values[i].toString());
			r = r.replace("{3}", target);
			return r;
		}
		public String getShort() {
			return getBaseDescription().substring(getBaseDescription().indexOf("}")+1, getBaseDescription().indexOf("{", getBaseDescription().indexOf("}")));
		}
		
		private Object[] fix(Unit u, Object[] v) {
			if(id == 17) v[1] = "" + (Integer.parseInt(v[1].toString())+1);
			if(id == 22 || id == 48) {
				Ability a = null; //_Library.JP.getSkillById(Integer.parseInt(v[0].toString()));
				v[0] = a != null ? a.getName() : "Unknown Skill ID: " + v[0];
			}
			if(id == 29 || id == 31 || id == 33 || id == 35 || id == 37 || id == 47 || id == 90) 
				v[1] = v[1].toString().equalsIgnoreCase("-1") ? "" : " for " + v[1] + " turns";
			if(id == 54) {
				String a = u.getSpecificAilment(Integer.parseInt(v[0].toString())).getName();
				v[0] = v[0].toString().equalsIgnoreCase("-1") ? "all" : ("ã€Œ" + (a != null ? a : "Unknown Ailment ID: " + v[0]) + "ï¿½?");
				v[1] = v[1].toString() + (v[1].toString().equalsIgnoreCase("1") ? " turn" : " turns");
			}
			if(id == 60) { Object o = MonsterType.getById((int)v[0]); if(o != null) v[0] = o; }
			if(id == 66) { 
				switch((int)v[1]) {
				case 2: v[1] = "20% IBrv Poison";
				case 10: v[1] = "10% Max Brv Down";
				case 11: v[1] = "10% Atk Down";
				case 13: v[1] = "10% Def Down";
				case 15: v[1] = "10% Speed Down";
				case 57: v[1] = "10% Max Brv Up";
				case 403: v[1] = "Lock";
				}
			}
			return v;
		}
		
		public static Effect get(int id) {
			for(Effect e : values())
				if(e.getId() == id)
					return e;
			return null;
		}
	}
	public static enum Required{
		RN1(-1, ""), //When there's no condition
		R1(1, ""),   //When the condition is the same as the effect before it
		R2(2, ""),   //When there's no condition
		R14(14, "When using 「**{0}**」:", false), //When using {0}: ?????????
		R26(26, "while own BRV > {0}% Max BRV"),
		R29(29, "while own BRV < {0}% Int BRV"),
		R30(30, "while own HP > {0}% Max HP"),
		R31(31, "while own HP < {0}% Max HP"),
		R32(32, "while 「**{0}**」 is active"), //While buff is active
		R33(33, "when breaking a target"), //When breaking
		R35(35, "if HP is MAX at start of last wave"),
		R36(36, "when taking HP damage"),
		R37(37, "when killing a target"), //When killing
		R38(38, "when dealing critical damage"), //When crit-ing
		R39(39, "when evading an attack"), //When evading
		R40(40, "while 「**{0}**」 is active"), //While buff is active
		R41(41, "when BRV = 0"),
		R42(42, "when initiating a launch"),
		R43(43, "if BRV >= Int BRV and enemy has twice as much BRV", true), //Spite weird stupid condition
		R44(44, "when granting 「**{0}**」 buff"),
		R45(45, "when dealing BRV damage"), //This condition is used on Tifa and Zidane on their Slayer passives, but there's no actual condition to it
		R46(46, "when attacking during a launch"),
		R47(47, "when attacking an enemy not targeting self"), //When enemy not targetting self
		R49(49, "if first to act in each wave"),
		R50(50, "when using HP Attack(or HP+)"),
		R51(51, "when dealing fire damage"),
		R52(52, "after mastering {0} {1}"),  //TODO Used by [Mastery chars]... and Setzer
		R52_2(52, "after using Freeze Joker 2 times"),  //TODO Used by Mastery chars... and [Setzer]     IF SKILL_ID = 1742 and char = "Setzer"
		R53(53, "when using BRV Attack(or BRV+)"),
		R54(54, "when ally inflicts Break"),
		R55(55, "when ally kills an enemy"),
		R58(58, "when HP is restored"),
		R59(59, "when using group attacks on 1 target"),
		R59_2(59, "when using group attacks"), //IF SKILL_ID = 296 and char = "Laguna"
		R61(61, "while buffed"), //Has 1 argument, but it doesn't seem to be used for anything, maybe # of buffs required?
		R63(63, "while an enemy is debuffed"),
		R64(64, "during ability chain (includes BRV+/HP+)"), //Has 1 argument, but it doesn't seem to be used for anything, maybe # of buffs required?
		R65(65, "at start of each wave"),
		R68(68, "with full card stock"), //Ace CL50 Exclusive
		R69(69, "when using ability with 1 use left"), //Sabin CL50 Exclusive
		R74(74, "when an ally's BRV is below their Int Brv"), //Krile CL50 Exclusive (3 parameters, but its an exclusive passive, so leaving it hardcoded)
		R75(75, "After using Darkness:", false), //Cecil Exclusive                 
		R76(76, "when inflicting a debuff"),
		R77(77, "when using an ability"), //Has 1 argument, but it doesn't seem to be used for anything //TODO RECHECK THIS ONE
		R78(78, "while 「**{0}**」 is active"), //While buff is active
		R79(79, "while an enemy is poisoned"), //Thancred Exclusive
		R89(89, "while 「**{0}**」 is active"), //If required[0] = 6, "Chelinka's Prayer", else if required[0] = 7, "Eblan's Teachings", else 
		;
		
		private int id;
		private String baseDescription;
		private boolean postEffect;

		private Required(int id, String desc) {
			this(id, desc, true);
		}
		private Required(int id, String desc, boolean post) {
			this.id = id;
			this.baseDescription = desc;
			this.postEffect = post;
		}
		
		public int getId() { return id; }
		public String getBaseDescription() { return baseDescription; }
		public boolean isPostEffect() { return postEffect; }
		
		public String getDescription(Unit u, Integer... values) {
			return getDescription(u, Arrays.stream(values).map(i -> i.toString()).collect(Collectors.toList()).toArray(new String[0]));
		}
		public String getDescription(Unit u, String... values) {
			values = fix(u, values);
			String r = baseDescription;
			for(int i = 0; i < values.length; i++)
				r = r.replace("{" + i + "}", values[i]);
			return r;
		}
		
		private String[] fix(Unit u, String[] v) {
			if(id == 14) {
				String a = u.getSpecificAbility(Integer.parseInt(v[0])).getName();
				v[0] = a != null ? a : "Unknown Skill ID: " + v[0];
			}
			if(id == 32 || id == 40) {
				String a = u.getSpecificAilment(Integer.parseInt(v[0])).getName();
				v[0] = a != null ? a : "Unknown Ailment ID: " + v[0];
			}
			if(id == 44) {
				String a = u.getSpecificAilment(Integer.parseInt(v[0])).getName();
				v[0] = v[0].equals("-1") ? "any" : "「" + (a != null ? a : "Unknown Ailment ID: " + v[0]) + "」";
			}
			if(id == 52) v[1] = v[0].equals("1") ? "mastery" : "masteries";
			if(id == 89) v[0] = v[0].equals("6") ? "Chelinka's Prayer" : (v[0].equals("7") ? "Eblan's Teachings" : ("Unknown Ailment ID: " + v[0]));
			
			return v;
		}
		
		public static Required get(int id) {
			for(Required r : values())
				if(r.getId() == id)
					return r;
			return null;
		}
	}
	
	private int id;
	private String name;
	private Unit unit;
	private String desc, shortDesc;
	private int cp, level;
	private Target target;
	private List<Dual<JSONParser.ValueGrouping<Effect>, JSONParser.ValueGrouping<Required>>> effects = new LinkedList<Dual<JSONParser.ValueGrouping<Effect>, JSONParser.ValueGrouping<Required>>>();
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Unit getUnit() {
		return unit;
	}
	public void setUnit(Unit unit) {
		this.unit = unit;
	}
	public String getDescription() {
		return desc;
	}
	public void setDescription(String desc) {
		this.desc = desc;
	}
	public String getShortDesc() {
		return shortDesc;
	}
	public void setShortDescription(String shortDesc) {
		this.shortDesc = shortDesc;
	}
	public int getCp() {
		return cp;
	}
	public void setCpCost(int cp) {
		this.cp = cp;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public Target getTarget() {
		return target;
	}
	public void setTarget(Target target) {
		this.target = target;
	}
	public List<Dual<JSONParser.ValueGrouping<Effect>, JSONParser.ValueGrouping<Required>>> getEffects() {
		return effects;
	}
	
	public String generateDescription() throws BotException {
		List<String> results = new LinkedList<String>();
		JSONParser.ValueGrouping<Effect> previousEff = null;
		JSONParser.ValueGrouping<Required> previous = null;
		for(Dual<JSONParser.ValueGrouping<Effect>, JSONParser.ValueGrouping<Required>> effReq : getEffects()) {
			JSONParser.ValueGrouping<Effect> eff = effReq.getValue1();
			JSONParser.ValueGrouping<Required> req = effReq.getValue2();
			if(req.type == null || eff.type == null) {
				results.add("E" + eff.id + "(" + eff.values.toString() + ") when R" + req.id + "(" + req.values.toString() + ")");
				continue;
			}
			if(req.type.getId() == 1 && previous != null) {
				if(eff.type.getId() >= 28 && eff.type.getId() <= 37 && previousEff.type.getId() >= 28 && previousEff.type.getId() <= 37) {
					String r = results.get(results.size() - 1);
					if(eff.type.getId() % 2 == 0) { //Perma
						if(eff.values[0] == previousEff.values[0])
							r = r.replace("{4}", ", " + eff.type.getShort() + "{4}");
						else 
							r = Methods.replaceLast(r, "%", "%, " + eff.type.getShort() + " by " + eff.values[0] + "%");
					}
					else { //For X turns
						if(eff.values[1] == previousEff.values[1])
							r = r.replace("{4}", ", " + eff.type.getShort() + "{4}");
						else
							r = r + System.lineSeparator() + " -" + eff.type.getDescription(this.unit, this.getTarget().getDescription(), (Object)eff.values);
					}
					results.remove(results.size() - 1);
					results.add(r);
				}
				else if(!previous.type.isPostEffect()) {
					String r = results.get(results.size() - 1);
					r = r + System.lineSeparator() + " -" + eff.type.getDescription(this.unit, this.getTarget().getDescription(), (Object)eff.values);
					results.remove(results.size() - 1);
					results.add(r);
				}else {
					results.add(eff.type.getDescription(this.unit, this.getTarget().getDescription(), (Object)eff.values) + " " + previous.type.getDescription(this.unit, previous.values));
				}
				continue;
			} else if(req.type.getId() == -1 || req.type.getId() == 2 || req.type.getId() == 1) {
				results.add(eff.type.getDescription(this.unit, this.getTarget().getDescription(), (Object)eff.values));
			} else {
				if(req.type.isPostEffect())
					results.add(eff.type.getDescription(this.unit, this.getTarget().getDescription(), (Object)eff.values) + " " + req.type.getDescription(this.unit, req.values));
				else
					results.add(req.type.getDescription(this.unit, req.values) + System.lineSeparator() + " -" + eff.type.getDescription(this.unit, this.getTarget().getDescription(), (Object)eff.values));
			}
			previous = req;
			previousEff = eff;
		}
		return results.stream().map(s -> s.replace("{4}", "")).reduce("", (o1, o2) -> o1 + System.lineSeparator() + o2).trim();
	}
}