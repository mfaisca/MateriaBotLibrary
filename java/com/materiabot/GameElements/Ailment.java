package com.materiabot.GameElements;
import Shared.Methods;
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
	public static enum Target {
		ST(1, "target"),
		Self(2, "self"),
		AoE(5, "all enemies"),
		Party(6, "party"),
		Party2(7, "party"),
		Ally(13, "ally");
		
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
		E1(1, "{t}ATK {0}%"),
		E2(2, "{t}DEF {0}%"),
		E3(3, "{t}Speed {0}%"),
		E4(4, "{t}Int BRV {0}%"),
		E5(5, "{t}Max BRV {0}%"),
		E6(6, "{t}Max HP {0}%"),
		E7(7, "{t}HP Regen({0}% Max HP)"),
		E8(8, "{t}BRV Regen({0}% IBRV)"),
		E9(9, "{t}BRV Regen({0}% MBRV)"),
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
		E22(22, "{t}Debuff Evasion {0}%"),
		E23(23, "{t}Fire Enchant"),
		E24(24, "{t}Ice Enchant"),
		E25(25, "{t}Thunder Enchant"),
		E26(26, "{t}Wind Enchant"),
		E27(27, "{t}Water Enchant"),
		E28(28, "{t}Earth Enchant"),
		E29(29, "{t}Holy Enchant"),
		E30(30, "{t}Dark Enchant"),
		E31(31, "Unable to act"),
		E40(40, "Unable to switch target"),
		E44(44, "Free ability use next turn(except LD)"),
		E45(45, "Turn Rate {0}%"),
		E46(46, "{t} Critical Hit Rate {0}%"),
		E49(49, "{t} Sap by {0}% Max BRV"),
		E50(50, "{t} Magic Damage dealt {0}%"),
		E51(51, "{t} Melee Damage dealt {0}%"),
		E52(52, "{t} Ranged Damage dealt {0}%"),
		E53(53, "Raises {t} BRV by BRV damage prevented"),
		E54(54, "{t} {0}% Debuff Success Rate"),
		E58(58, "{t} BRV Damage taken {0}%", true),
		E60(60, "Aura (Separate Parsing)"),
		E61(61, "BRV Damage {0}% per debuff on target"),
		E65(65, "BRV Damage on debuffed targets {0}%"),
		E67(67, "{t} Stolen BRV Overflow {0}%"),
		//E68 - Zell Duel - Effect not needed
		//E85 - Selphie Aura - Unknown effect
		E103(103, "Unable to act"),
		E106(106, "{t} EX Recast {0}%"),
		E111(111, null), //Aphmau 2T dolls
		E112(112, "Critical Hit Damage {0}%"),
		E114(114, "Party BRV Damage dealt by {0}%"),
		E115(115, "{t} HP Damage dealt on abilities with splash {0}%"),
		E119(119, "Instant turn rate"),
		E120(120, "Doesn't increase turn count"),
		E122(122, "{t} Melee Damage taken {0}%", true),
		E123(123, "{t} Ranged Damage taken {0}%", true),
		E139(139, "New debuffs duration {0} turns"),
		E140(140, "New buffs duration {0} turns"),
		E150(150, "{t} Gained BRV Overflow {0}%"),
		E151(151, "Triggers 「**Wind Slash**」 at end of turn"),
		E169(169, "{t} Ranged BRV Overflow {0}%"),
		E164(164, "{t} unable to gain buffs"),
		E165(165, "{t} unable to battery"),
		E180(180, "Sets {t} HP Damage taken to 0"),
		E190(190, "Last stand on {t} when {0}% Max HP or higher", null),
		E197(197, "Raises stack by 1 every action you take"),
		E199(199, "Party critical BRV damage dealt by {0}%"),
		E210(210, "Sets {t} BRV Damage dealt to 0", null),
		E216(216, "Nulls BRV damage under {0}% Int BRV"),
		E229(229, "{0}% HP damage taken from Eald'narche", true),
		E234(234, "Cannot act when targetting Eald'narche"),
		E235(235, "Party Maximum BRV damage limit {0}%"),
		E252(252, "After HP attack, raises BRV by {0}% of HP Damage Dealt"),
		E257(257, null), //BT Buff Effect
		E311(311, "{0}% of {t} excess healing is converted to BRV"),
		E317(317, "After any turn, sets {t} BRV to {0}", null),
		E320(320, "Delay target by {0}T after a physical attack"),
		E331(331, "Cannot deal BRV damage"),
		E335(335, "Cannot deal HP damage"),
		E327(327, "Cannot inflict debuffs"),
		E329(329, "Cancelled after using BRV, HP or debuffing skill"),
		E336(336, "Deletes target next turn with abilities"),
		;

		private int id;
		private String baseDescription;
		private Boolean defaultNegative = false;

		private EffectType(int id, String desc) { this.id = id; baseDescription = desc; }
		private EffectType(int id, String desc, Boolean dN) { this.id = id; baseDescription = desc; defaultNegative = dN; }

		public int getId() { return id; }
		public String getBaseDescription() { return baseDescription; }
		public Boolean isDefaultNegative() { return defaultNegative; }

		public String getDescription(String h, int rankDataIndex, int val_specify, Target target, String...extra) {
			if(h == null)
				return getDescription(new String[0], val_specify, target, extra);
			int val = splitRankData(h, rankDataIndex) * 
						(isDefaultNegative() == null ? 1 : 
							((extra.length > 0 && extra[0].equals("0") ? -1 : 1) * (isDefaultNegative() ? -1 : 1)));
			return getDescription(Arrays.asList(val).stream()
					.map(i -> i.toString())
					.collect(Collectors.toList()).toArray(new String[0]), val_specify, target, extra);
		}
		public String getDescription(String stackingNumber, Target target, String... extra) {
			return getDescription(new String[] {stackingNumber}, -1, target, extra);
		}		
		private String getDescription(String[] values, int val_specify, Target target, String... extra) {
			values = fix(values, extra);
			String r = baseDescription;
			if(values.length > 0)
				for(int i = 0; i < values.length; i++)
					r = r.replace("{" + i + "}", (isDefaultNegative() != null ? (values[i].contains("-") ? "" : "+") : "") + values[i]);
			else
				r = r.replace("{0}", ""+val_specify);
			r = r.replace("{t}", target != null ? target.getDesc() + " " : "");
			return r;
		}
		private String[] fix(String[] v, String[] extra) {
			//if(v.length > 0) return;
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
	
	public Ailment() {}
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
	public void setArgs(int[] args) {
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
		return ImageUtils.getEmoteText(getIconEmote()) + " " + getName() + (getMaxStacks() > 1 ? " (" + getMaxStacks() + " max stacks)" : "") + " (" + this.getId() + ")";
	}
	public String getIconEmote() {
		if(fakeEmote != null) return fakeEmote;
		return (isBTBuff() ? Ailment.Emotes.BUFF_BT.get() : 
				(isBuff() ? Ailment.Emotes.BUFF_GENERIC.get() : 
							Ailment.Emotes.DEBUFF_GENERIC.get()) + (isFramed() ? "Framed" : ""));
	}
	
//	private static Integer splitRankData(String v, int idxx) {
//		//int i = Integer.parseInt(v.replace("-", ""));
//		String oV = ""+v;
//		try {
//			Integer[] result = new Integer[(int)Math.ceil((v).toString().length()/3f)];
//			int idx = result.length-1;
//			while(v.length() > 0) {
//				int ii = Integer.parseInt(v.split("(?<=\\G...)")[v.split("(?<=\\G...)").length - 1]);
//				result[idx--] = ii % 1000;
//				v = v.substring(0, v.length() < 4 ? 0 : (v.length()-3));
//			}
//			if(result.length == 1 && result[0] == null)
//				return 0;;
//			return result[idxx];
//		} catch(Exception e) {
//			if (idxx == -1) throw e;
//			return splitRankData(oV, idxx-1);
//		}
//	}
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
				rankDataIndex = 0;
				for(Aura a : getAuras().values()) {
					EffectType ae = EffectType.get(a.ailmentEffect);
					if(ae == null)
						desc += System.lineSeparator() + ("Unknown Effect " + a.ailmentEffect);
					else if(isStackingBuff() && a.rankData != null) {
						String stackingNumber = null;
						for(int i = 0; i < getMaxStacks() && i < a.rankData.length; i++){
							Integer iv = splitRankData(a.rankData[i], rankDataIndex);
							if(iv != 0 || stackingNumber == null || stackingNumber.length() == 0)
								stackingNumber = (stackingNumber == null ? "" : stackingNumber + "/") + iv;
						}
						desc += System.lineSeparator() + StringUtils.capitalize(ae.getDescription(stackingNumber, Target.get(a.target), ""+(this.isBuff() ? 1 : 0)));
					}else
						desc += System.lineSeparator() + StringUtils.capitalize(ae.getDescription(a.rankData[this.rank], 0, -1, Target.get(a.target), ""+(this.isBuff() ? 1 : 0)));
					rankDataIndex = (data != null && ((Math.pow(1000, (rankDataIndex+1))) <= Integer.parseInt(data.replace("-", "")))) ? rankDataIndex + 1 : rankDataIndex;
					desc = desc + " (" + a.id + ")";
				}
				desc = desc.trim();
			}else if(data == null && isStackingBuff() && eff.rankData != null) {
				String stackingNumber = null;
				for(int i = 0; i < getMaxStacks(); i++){
					Integer iv = splitRankData(eff.rankData[i], rankDataIndex);
					if(iv != 0 || stackingNumber == null || stackingNumber.length() == 0)
						stackingNumber = (stackingNumber == null ? "" : stackingNumber + "/") + iv;
				}
				desc = e.getDescription(stackingNumber, this.target, ""+(this.isBuff() ? 1 : 0));
				rankDataIndex = eff.rankData[0].length() > 3 ? rankDataIndex+1 : rankDataIndex;
			}else {
				desc = e.getDescription(data, rankDataIndex, eff.val_specify, this.target, ""+(this.isBuff() ? 1 : 0));
				rankDataIndex = (data != null && ((Math.pow(1000, (rankDataIndex+1))) <= Integer.parseInt(data.replace("-", "")))) ? rankDataIndex + 1 : rankDataIndex;
			}
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