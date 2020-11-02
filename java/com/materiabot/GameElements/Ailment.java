package com.materiabot.GameElements;
import Shared.Methods;
import com.materiabot.Utils.Constants;
import com.materiabot.Utils.ImageUtils;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;

public class Ailment {
	public static enum Emotes{
		BUFF_INVISIBLE("ailmentInvisible"),
		BUFF_BT("ailmentBT"),
		BUFF_AA("ailmentAA"),
		BUFF_CA("ailmentCA"),
		BUFF_CALD("ailmentCALD"),
		BUFF_GENERIC("buffGeneric"),
		DEBUFF_GENERIC("debuffGeneric"),
		;
		private String emote;
		
		private Emotes(String emote) { this.emote = emote; }
		
		public String get() { return emote; }
	}
	public static enum ValType {
		V1(1, "All Attacks"),
		V2(2, "Evaded Attacks"), //Made Up (Shadow)
		V3(3, "BRV Attacks"), //Made Up (Machina)
		V6(6, "HP Damage"),
		;
		
		private int id;
		private String desc;
		
		private ValType(int id, String desc) {this.id = id; this.desc = desc; }

		public int getId() {
			return id;
		}
		public String getDesc() {
			return desc;
		}
		
		public static ValType get(int id) {
			for(ValType t : values())
				if(t.getId() == id)
					return t;
			return null;
		}
	}
	public static enum Target {
		Same(0, ""),
		ST(1, "target"),
		Self(2, "self"),
		AoE(5, "all enemies"),
		Party(6, "party"),
		Allies(7, "allies"),
		Ally(13, "ally"),
		Random(14, "random");
		
		private int id;
		private String desc;
		
		private Target(int id, String desc) {this.id = id; this.desc = desc; }

		public int getId() {
			return id;
		}
		public String getDesc() {
			return desc;
		}
		
		public static Target get(int id) {
			for(Target t : values())
				if(t.getId() == id)
					return t;
			return null;
		}
	}

	public static enum EffectType{
		E0(0, null),
		E1(1, "{0}% {t}ATK"),
		E2(2, "{0}% {t}DEF"),
		E3(3, "{0}% {t}Speed"),
		E4(4, "{0}% {t}Int BRV"),
		E5(5, "{0}% {t}Max BRV"),
		E6(6, "{0}% {t}Max HP"),
		E7(7, "{t}HP Regen ({0}% Max HP)", null),
		E8(8, "{t}BRV Regen ({0}% IBRV)", null),
		E9(9, "{t}BRV Regen ({0}% MBRV)", null),
		E10(10, "{0} {t}Fire Resist", null), //0 = Lowers/Increases
		E11(11, "{0} {t}Ice Resist", null),  //3 = Party
		E12(12, "{0} {t}Thunder Resist", null),
		E13(13, "{0} {t}Wind Resist", null),
		E14(14, "{0} {t}Water Resist", null),
		E15(15, "{0} {t}Earth Resist", null),
		E16(16, "{0} {t}Holy Resist", null),
		E17(17, "{0} {t}Dark Resist", null),
		E18(18, "{0} {t}ALL Resistances", null),
		E19(19, "{0} {t}Magic Resist", null),
		E20(20, "{0} {t}Melee Resist", null),
		E21(21, "{0} {t}Ranged Resist", null),
		E22(22, "{0}% {t}Debuff Evasion"),
		E23(23, "{t}Fire Enchant"),
		E24(24, "{t}Ice Enchant"),
		E25(25, "{t}Thunder Enchant"),
		E26(26, "{t}Wind Enchant"),
		E27(27, "{t}Water Enchant"),
		E28(28, "{t}Earth Enchant"),
		E29(29, "{t}Holy Enchant"),
		E30(30, "{t}Dark Enchant"),
		E31(31, "Unable to act"),
		E32(32, "{t}Poison by {0}% of Int BRV", null),
		E40(40, "Unable to switch target"),
		E41(41, "{0}% {t}BRV Magic Damage dealt", true),
		E42(42, "{0}% {t}BRV Melee Damage dealt", true),
		E43(43, "{0}% {t}BRV Ranged Damage dealt", true),
		E44(44, "Free ability use next turn(S1/S2/AA only)"), //Set CastID to 463 to not merge with description
		E45(45, "{0}% Turn Rate"),
		E46(46, "{0}% {t}Critical Hit Rate"),
		E47(47, "{0}% {t} Evasion Rate"),
		E48(48, "{0}% {t} Hit Rate", null),
		E49(49, "{t}Sap by {0}% Max BRV", null),
		E50(50, "{0}% {t}Magic ATK"),
		E51(51, "{0}% {t}Melee ATK"),
		E52(52, "{0}% {t}Ranged ATK"),
		E53(53, "Raises {t}BRV by BRV damage prevented"),
		E54(54, "{0}% {t}Debuff Success Rate"),
		E55(55, "Halves current and max [CU](https://www.reddit.com/r/DissidiaFFOO/comments/7x7ffp/chase_mechanic/)"),
		E56(56, "{0}% {t}BRV Damage when hitting elemental weakness"),
		E57(57, "{0}% {t}HP Damage taken", true),
		E58(58, "{0}% {t}BRV Damage taken", true),
		E59(59, "Revive with {0}% Max HP if KO'd", null),
		E60(60, "Aura (Separate Parsing)"),
		E61(61, "{0}% BRV Damage per debuff on target"),
		E62(62, "Shields BRV for {0}% of caster Int BRV", null),
		E63(63, "{0}% {t}BRV Damage taken"), //Fake for Alphi
		E65(65, "{0}% BRV Damage on debuffed targets"),
		E67(67, "{0}% {t} Stolen BRV Overflow"),
		//E68 - Zell Duel - Effect not needed
		E69(69, "Counter {vt} with 「**{vs}**」"),
		E78(78, null), //Shadow EX
		E79(79, null), //Gladio LD Random Effect
		E80(80, null), //CoD S1 Random Effect
		E81(81, null), //Gabranth Main Buff Random Effect
		E82(82, null), //Gabranth Main Buff Random Effect
		E83(83, null), //Gladio EX Random Effect
		E84(84, null), //Gladio S1/EX Random Effect
		//E85 - Selphie Aura - Unknown effect
		E97(97, "{0}% {t}BRV Damage when hitting weakness"),
		E98(98, "Dispels if broken"),
		E101(101, null), //Unknown Poison second effect
		E103(103, "Unable to act"),
		E105(105, "{t} unable to battery"),
		E106(106, "{t} EX Recast {0}%"),
		E107(107, "Prevents from being broken"),
		E110(110, "Free turn after breaking a target"),
		E111(111, null), //Aphmau 2T dolls
		E112(112, "{0}% {t}Critical Hit Damage"),
		E114(114, "{0}% {t}BRV Damage dealt"),
		E115(115, "{0}% {t}HP Damage dealt"),
		E116(116, "Party BRV Regen({0}% HP Damage Dealt with 「**Quick Dualcast**」"),
		E118(118, "BRV Regen applies **every** turn. Duration goes down **every** turn."),
		E119(119, "Instant turn rate"),
		E120(120, "Doesn't increase turn count"),
		E122(122, "{0}% {t}Melee Damage taken", true),
		E123(123, "{0}% {t}Ranged Damage taken", true),
		E124(124, "Prevents use of HP attacks"),
		E125(125, "Prevents use of Magic attacks"),
		E139(139, "New debuffs duration {0} turns"),
		E140(140, "New buffs duration {0} turns"),
		E141(141, "+1 「**Chakra**」 whenever party uses an ability(non BRV/HP/AA)"),
		E142(142, null), //Lyse Chakra
		E146(146, "Take AoE HP attacks in place of allies"),
		E150(150, "{0}% {t} Gained BRV Overflow"),
		E151(151, "Triggers 「**{0}**」 at end of turn", null),
		E169(169, "{0}% {t}Ranged BRV Overflow"),
		E164(164, "{t} unable to gain buffs"),
		E165(165, "{t} unable to battery"),
		E180(180, "{0}% {t} HP damage taken", true),
		E181(181, "{vs} joins you in battle"),
		E183(183, null), //Both Eight Counterbuffs have this
		E184(184, "{0}% {t} BRV damage taken", true), //Manually Made
		E185(185, null), //Kurasame useless effect?
		E186(186, "{t} unable to gain BRV"),
		E190(190, "Last stand on {t} when {0}% Max HP or higher", null),
		E191(191, "Sets {t} HP Damage dealt to 0"),
		E193(193, "HP Damage equal to {0}% of Attack", null),
		E194(194, "Trigger 「**{vs}**」 on expiration"),
		E195(195, null), //WoL LD unknown HitData
		E197(197, "Raises stack by 1 every action you take"),
		E199(199, "{0}% {t} critical BRV damage dealt"),
		E200(200, null), //Amidatelion LD effect?
		E205(205, null), //Extra Garland LD effect?
		E208(208, "{0}% BRV Damage with ST attacks", null),
		E210(210, "Sets {t} BRV Damage dealt to 0", null),
		E211(211, "{0}% HP Damage to non-targets"),
		E214(214, "Shields BRV for {0}% of damage dealt", null),
		E215(215, "Raises {t}BRV back to Int BRV when BRV=0 on the turn after theirs"),
		E216(216, "Nulls BRV damage under {0}% Int BRV"),
		E217(217, "{0}% HP Damage during a chase"),
		E223(223, "Recover HP by {0}% of HP damage dealt up to {1}% Max HP", null),
		E224(224, "{0}% {t}HP Damage when attacking a broken target"),
		E226(226, "After an ally turn, raise BRV by {0}% of caster Max BRV", null),
		E227(227, "{0}% {t}Max HP overflow limit"),
		E228(228, "{0}% {t}BRV Damage taken if broken", true),
		E229(229, "{0}% HP damage taken from caster", true),
		E230(230, "Triggers 「**{0}**」 at end of every turn" + System.lineSeparator()
					+ "Lowers duration at the end of every turn", null),
		E232(232, "All BRV Attacks will inflict BREAK if not broken", true),
		E233(233, "Caster takes a free instant turn after target's turn"),
		E234(234, "Cannot act when targetting Eald'narche"),
		E235(235, "{0}% {t} Maximum BRV damage limit"),
		E236(236, "{0}% {t} Maximum obtainable BRV & HP damage limit"),
		E238(238, "Guaranteed Launch with next attack"),
		E239(239, "Trigger BREAK on expiration"),
		E244(244, "{0}% {t}BRV Damage taken if broken", true),
		E245(245, "+1 stack after attacking"),
		E246(246, "{t}BRV will not drop below {0}% of Max BRV", null),
		E252(252, "After HP attack, raises BRV by {0}% of HP Damage Dealt", null),
		E255(255, "When Current HP <= {0}% Max HP, recover HP back to {0}% Max HP", null),
		E257(257, null), //BT Buff Effect
		E265(265, "{0}% Critical BRV Damage Taken", true),
		E266(266, "All BRV hits are Critical Hits"),
		E268(268, "Cannot be killed"),
		E274(274, "Triggers 「**{0}**」 after ally attack or enemy attacks", null),
		E276(276, "Move your next turn to just before the target next turn"),
		E279(279, "-100% HP Damage taken"),
		E280(280, null), //Nine LD ???
		E282(282, "Sets HP to 1 if at 0 HP when buff expires"),
		E288(288, "{0} duration when hit"),
		E284(284, "Raises party member BRV by {0}% of remaining 「**Shield**」 at the start of their turn", null),
		E293(293, "Nulls {t} HP damage that is lower than each ally's 「**Shield**」 power"),
		E294(294, "Triggers 「**{0}**」 after party member turn", null),
		E295(295, "Caster takes the hit for ally"),
		E300(300, "{0}% {t}BRV Damage when attacking a broken target"),
		E309(309, null), //Kurasame useless effect?
		E311(311, "{0}% of {t}excess healing is converted to BRV"),
		E317(317, "After any turn, sets {t} BRV to {0}", null),
		E320(320, "Delay target by {0}T after a physical attack"),
		E325(325, "「**{0}**」 are not consumed", null),
		E331(331, "Cannot deal BRV damage"),
		E335(335, "Cannot deal HP damage"),
		E327(327, "Cannot inflict debuffs"),
		E329(329, "Cancelled after using BRV, HP or debuffing skill"),
		E334(334, "{0}% {t}HP Damage taken", true),
		E336(336, "Deletes target next turn with abilities"),
		E347(347, "{t}BRV Regen ({0}% Current HP)"),
		E348(348, "Raises party BRV by {0}% of Total Chase HP Damage Dealt", null),
		E355(355, "Raises BRV by {0}% of BRV Damage Dealt", null),
		E359(359, "Unbreaks target before attack"),
		E367(367, "+1 stack after every ally turn", null),
		E368(368, "Resets to 1 stack after enemy turn", null),
		E374(374, "Reduce BRV by {0}% at end of turn", null),
		E390(390, "Apply 「**{0}**」 after attacking", null),
		E403(403, "Raises allies BRV by {0}% of own Max BRV when dealing weakness damage", null),
		E418(418, "Triggers 「**{vs}**」 after own turn when at 1+ stacks"),
		
		E1001(1001, "{t}Attacks deal +3 [CU](https://www.reddit.com/r/DissidiaFFOO/comments/7x7ffp/chase_mechanic/)"), //Fake Ailment used by Layle EX Buff
		E1002(1002, "{t} cannot be broken by BRV damage"),
		;

		private int id;
		private String baseDescription;
		private Boolean defaultNegative = false;

		private EffectType(int id, String desc) { this.id = id; baseDescription = desc; }
		private EffectType(int id, String desc, Boolean dN) { this.id = id; baseDescription = desc; defaultNegative = dN; }

		public int getId() { return id; }
		public String getBaseDescription() { return baseDescription; }
		public Boolean isDefaultNegative() { return defaultNegative; }

		public String getDescription(Unit u, String h, int rankDataIndex, int val_type, int val_specify, Target target, String...extra) {
			if(h == null)
				return getDescription(u, new String[0], val_type, val_specify, target, extra);
			int val = splitRankData(h, rankDataIndex) * 
						(isDefaultNegative() == null ? 1 : 
							((extra.length > 0 && extra[0].equals("0") ? -1 : 1) * (isDefaultNegative() ? -1 : 1)));
			return getDescription(u, Arrays.asList(val).stream()
					.map(i -> i.toString())
					.collect(Collectors.toList()).toArray(new String[0]), val_type, val_specify, target, extra);
		}
		public String getDescription(Unit u, String stackingNumber, Target target, String... extra) {
			return getDescription(u, new String[] {stackingNumber}, -1, -1, target, extra);
		}		
		private String getDescription(Unit u, String[] values, int val_type, int val_specify, Target target, String... extra) {
			String r = baseDescription;
			//if(this == EffectType.E69 || this == EffectType.E194) {
			if(this.baseDescription.contains("{vs}")) {
				if(val_type == 1)
					val_specify = Integer.parseInt(extra[1]);
				r = r.replace("{vs}", val_specify > 0 ? u.getSpecificAbility(val_specify).getName() : "Unknown Ability ID: " + val_specify);
			}
			if(this == EffectType.E325) {
				if(values.length == 0)
					values = new String[1];
				if(val_specify == 23)
					values[0] = u.getSpecificAilment(1996).getName();
				else if(val_specify == 24)
					values[0] = u.getSpecificAilment(982).getName();
				else
					values[0] = "Unknown ID: " + extra[1];
			}
			values = fix(u, values, extra);
			if(values.length > 0)
				for(int i = 0; i < values.length; i++)
					r = r.replace("{" + i + "}", (isDefaultNegative() != null ? (values[i].contains("-") ? "" : "+") : "") + values[i]);
			else
				r = r.replace("{0}", (isDefaultNegative() != null ? (val_specify < 0 ? "" : "+") : "")+val_specify);
			r = r.replace("{t}", target != null ? target.getDesc() + " " : "");
			if(r.contains("{vt}"))
				r = r.replace("{vt}", val_type > 0 ? Ailment.ValType.get(val_type).getDesc() + " " : "");
			return r;
		}
		private String[] fix(Unit u, String[] v, String[] extra) {
			switch(id) {
				case 10:
				case 11:
				case 12:
				case 13:
				case 14:
				case 15:
				case 16:
				case 17:
				case 18:
				case 19:
				case 20:
				case 21:
					v = new String[1];
					v[0] = extra[0].equals("1") ? "Raises" : "Lowers";
					break;
				case 69:
					v = new String[1];
					v[0] = extra[0].equals("1") ? "Raises" : "Lowers";
					break;
				case 151:
				case 230:
				case 274:
				case 294:
					v = new String[1];
					v[0] = u.getSpecificAbility(Integer.parseInt(extra[1])).getName();
					break;
				case 223:
					v = new String[] {v[0], "35"};
					break;
				case 390:
					v = new String[1];
					v[0] = u.getSpecificAilment(Integer.parseInt(extra[1])).getName();
					break;
			}
			return v;
		}
		
		public static EffectType get(int id) {
			for(EffectType e : values())
				if(e.getId() == id)
					return e;
			return null;
		}
	}
	
	public static class EffectGrouping{
		public int effectId, val_type, val_specify;
		public String[] rankData;
		public int rank;
		public String fakeDesc;

		public EffectGrouping() {}
		public EffectGrouping(int eid) { effectId = eid; }
		public EffectGrouping(String fd) { fakeDesc = fd; }
	}
	public static class Aura{
		public int id;
		public Integer[] requiredConditions, requiredValues;
		
		public int effect, ailmentEffect, target, valType, typeId;
		public String[] rankData;
	}
	
	private int id, castId;
	private String name, desc, fakeName, fakeDesc, fakeEmote;
	private int rate, rank, duration, maxStacks, buffType, iconType;
	private int[] args;
	private Target target;
	private boolean stackable, extendable, framed;
	private List<EffectGrouping> effects = new LinkedList<EffectGrouping>();
	private HashMap<Integer, Aura> auras = new HashMap<Integer, Aura>();
	private Unit unit;
	
	public Ailment(Unit u) { unit = u; }
	public Ailment(String fE, String fN, String fD) { fakeEmote = fE; fakeName = fN; fakeDesc = fD; }
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getCastId() {
		return castId;
	}
	public void setCastId(int castId) {
		this.castId = castId;
	}
	public String getName() {
		if(fakeName != null) return fakeName;
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDesc() {
		if(fakeDesc != null) return fakeDesc;
		return desc;
	}
	public void setDescription(String desc) {
		this.desc = desc;
	}
	public int getRate() {
		return rate;
	}
	public void setRate(int rate) {
		this.rate = rate;
	}
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	public int[] getArgs() {
		return args;
	}
	public void setArgs(int... args) {
		this.args = args;
	}
	public Target getTarget() {
		return target;
	}
	public void setTarget(Target target) {
		this.target = target;
	}

	public int getMaxStacks() {
		return maxStacks;
	}
	public void setMaxStacks(int maxStacks) {
		this.maxStacks = maxStacks;
	}
	public int getBuffType() {
		return buffType;
	}
	public void setBuffType(int buffType) {
		this.buffType = buffType;
	}
	public int getIconType() {
		return iconType;
	}
	public void setIconType(int iconType) {
		this.iconType = iconType;
	}
	public boolean isStackable() {
		return stackable;
	}
	public void setStackable(boolean stackable) {
		this.stackable = stackable;
	}
	public boolean isExtendable() {
		return extendable;
	}
	public void setExtendable(boolean extendable) {
		this.extendable = extendable;
	}
	public boolean isFramed() {
		return framed;
	}
	public void setFramed(boolean framed) {
		this.framed = framed;
	}
	public List<EffectGrouping> getEffects() {
		return effects;
	}
	public HashMap<Integer, Aura> getAuras() {
		return auras;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public void setFake(String name, String desc, String emote) {
		if(name != null) fakeName = name;
		if(desc != null) fakeDesc = desc;
		if(emote != null) fakeEmote = emote;
	}

	public boolean isBuff() {
		return !isDebuff(); //Easier to do like this since there's more variations for buffs
	}
	public boolean isDebuff() {
		return target == Target.ST || target == Target.AoE;
	}
	public boolean isBTBuff() {
		return this.getEffects().stream().anyMatch(e -> e.effectId == Ailment.EffectType.E257.id);
	}
	public boolean isStackingBuff() {
		return getMaxStacks() > 1;
	}
	public boolean equals(Object o) {
		if(o == null || !o.getClass().equals(this.getClass()))
			return false;
		Ailment other = (Ailment)o;
		return this.getName() != null && this.getName().equals(other.getName()) 
			&& this.generateDescription() != null && this.generateDescription().equals(other.generateDescription());
	}
	public int hashCode() {
		return this.getName().hashCode();
	}
	
	public String getTitle() {
		return ImageUtils.getEmoteText(getIconEmote()) + " " + getName() + (Constants.DEBUG ? " (" + this.getId() + ")" : "") + (getMaxStacks() > 1 ? System.lineSeparator() + "(" + getMaxStacks() + " max stacks)" : "");
	}
	public String getIconEmote() {
		if(fakeEmote != null) return fakeEmote;
		return (isBTBuff() ? Ailment.Emotes.BUFF_BT.get() : 
				(isBuff() ? Ailment.Emotes.BUFF_GENERIC.get() : 
							Ailment.Emotes.DEBUFF_GENERIC.get()) + (isFramed() ? "Framed" : ""));
	}
	
	private static Integer splitRankData(String v, int idxx) {
		String[] r = Methods.splitRankData(v);
		return r != null ? Integer.parseInt(r[idxx]) : null;
	}
	
	public String generateDescription() {
		if(fakeDesc != null) return fakeDesc;
		if(effects.size() == 0) return null;
		List<String> ret = new LinkedList<String>();
		if(this.rate < 100)
			ret.add(rate + "% chance");
		String str = 
				(getMaxStacks() > 1 && getArgs().length > 0 && getArgs()[0] > 0? 
						"+" + (getArgs()[0] == 1 ? "1 stack to " : getArgs()[0] + " stacks to ") : 
						"")
				+ getTarget().getDesc();
		if(getDuration() > 0)
			str += " for " + getDuration() + (getDuration() > 1 ? " turns" : " turn");
		ret.add(str);
		int rankDataIndex = 0;
		for(EffectGrouping eff : effects) {
			if(eff.fakeDesc != null) {
				ret.add(eff.fakeDesc); continue; }
			EffectType e = EffectType.get(eff.effectId);
			if(e == null) {
				ret.add("Unknown Effect " + eff.effectId); continue; }
			if(e.getBaseDescription() == null) continue;
			String desc = null;
			String data;
			if(this.rank >= 0)
				data = eff.rankData == null ? null : eff.rankData[this.rank];
			else
				data = null;
			if(eff.effectId == 60){
				desc = "";
				rankDataIndex = 0;;
				for(Aura a : getAuras().values()) {
					EffectType ae = EffectType.get(a.ailmentEffect);
					List<String> aa = new LinkedList<String>(Arrays.asList(a.rankData));
					aa.add(0, ""+(this.isBuff() ? 1 : 0));
					if(ae == null)
						desc += System.lineSeparator() + ("Unknown Effect " + a.ailmentEffect);
					else if(isStackingBuff() && a.rankData != null) {
						String stackingNumber = null;
						for(int i = 0; i < getMaxStacks() && i < a.rankData.length; i++){
							Integer iv = splitRankData(a.rankData[i], rankDataIndex);
							if(iv != 0 || stackingNumber == null || stackingNumber.length() == 0)
								stackingNumber = (stackingNumber == null ? "" : stackingNumber + "/") + iv;
						}
						if(stackingNumber.contains("/")) {
							boolean same = true;
							String val = null;
							for(String v : stackingNumber.split("/")) {
								same &= val == null ? true : val.equals(v);
								val = v;
							}
							if(same) stackingNumber = val;
						}
						desc += System.lineSeparator() + StringUtils.capitalize(ae.getDescription(unit, stackingNumber, Target.get(a.target), aa.toArray(new String[0])));
					}else
						desc += System.lineSeparator() + StringUtils.capitalize(ae.getDescription(unit, a.rankData[this.rank], 0, -1, -1, Target.get(a.target), aa.toArray(new String[0])));
					rankDataIndex = (data != null && ((Math.pow(1000, (rankDataIndex+1))) <= Integer.parseInt(data.replace("-", "")))) ? rankDataIndex + 1 : rankDataIndex;
					if(Constants.DEBUG)
						desc = desc + " (" + a.id + ")";
				}
				desc = desc.trim();
			}else if(data == null && isStackingBuff() && eff.rankData != null) {
				String stackingNumber = null;
				for(int i = 0; i < getMaxStacks(); i++){
					Integer iv = i >= eff.rankData.length ? null : splitRankData(eff.rankData[i], rankDataIndex);
					stackingNumber = (stackingNumber == null ? "" : stackingNumber + "/") + iv;
				}
				if(stackingNumber.contains("/")) {
					boolean same = true;
					String val = null;
					for(String v : stackingNumber.split("/")) {
						same &= val == null ? true : val.equals(v);
						val = v;
					}
					if(same) stackingNumber = val;
				}
				desc = e.getDescription(unit, stackingNumber, Target.Same, ""+(this.isBuff() ? 1 : 0));
				rankDataIndex = eff.rankData[0].length() > 3 ? rankDataIndex+1 : rankDataIndex;
			}else {
				List<String> a = new LinkedList<String>(Arrays.asList(Arrays.stream(getArgs()).mapToObj(String::valueOf).toArray(String[]::new)));
				a.add(0, ""+(this.isBuff() ? 1 : 0));
				desc = e.getDescription(unit, data, rankDataIndex, eff.val_type, eff.val_specify, Target.Same, a.toArray(new String[0]));
				rankDataIndex = (data != null && ((Math.pow(1000, (rankDataIndex+1))) <= Integer.parseInt(data.replace("-", "")))) ? rankDataIndex + 1 : rankDataIndex;
			}
			if(desc.length() > 0)
				ret.add(desc);
		}
		return StringUtils.capitalize(ret.stream().distinct()
				.map(s -> s == null ? s : StringUtils.capitalize(s))
				.reduce((s1, s2) -> s1 + System.lineSeparator() + s2).orElse(""));
	}
	public void removeEffect(int... effectIds) {
		java.util.Iterator<EffectGrouping> iter = getEffects().iterator();
		while(iter.hasNext()) {
			EffectGrouping eg = iter.next();
			if(Arrays.stream(effectIds).anyMatch(a -> a == eg.effectId))
				iter.remove();
		}
	}
}