package com.materiabot.GameElements;
import com.materiabot.Utils.ImageUtils;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;

public class Ailment {
	public static enum Emotes{
		BUFF_INVISIBLE("buffInvisible"),
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
		E1(1, "{3}ATK{4} {0}%"),
		E2(2, "{3}DEF{4} {0}%"),
		E3(3, "{3}Speed{4} {0}%"),
		E4(4, "{3}Int BRV{4} {0}%"),
		E5(5, "{3}Max BRV{4} {0}%"),
		E6(6, "{3}Max HP{4} {0}%"),
		E7(7, "{3}HP Regen({0}% HP)"),
		E8(8, "{3}BRV Regen({0}% IBRV)"),
		E9(9, "{3}BRV Regen({0}% MBRV)"),
		E10(10, "{0} {3}Fire Resist"), //0 = Lowers/Increases
		E11(11, "{0} {3}Ice Resist"),  //3 = Party
		E12(12, "{0} {3}Thunder Resist"),
		E13(13, "{0} {3}Wind Resist"),
		E14(14, "{0} {3}Water Resist"),
		E15(15, "{0} {3}Earth Resist"),
		E16(16, "{0} {3}Holy Resist"),
		E17(17, "{0} {3}Dark Resist"),
		E18(18, "{0} {3}ALL Resistances"),
		E19(19, "{0} {3}Magic Resist"),
		E20(20, "{0} {3}Melee Resist"),
		E21(21, "{0} {3}Ranged Resist"),
		E22(22, "{0}% {3}Debuff Evasion"),
		E23(23, "{3}Fire Enchant"),
		E24(24, "{3}Ice Enchant"),
		E25(25, "{3}Thunder Enchant"),
		E26(26, "{3}Wind Enchant"),
		E27(27, "{3}Water Enchant"),
		E28(28, "{3}Earth Enchant"),
		E29(29, "{3}Holy Enchant"),
		E30(30, "{3}Dark Enchant"),
		E31(31, "Unable to act"),
		E40(40, "Unable to switch target"),
		E44(44, "Free ability use next turn(except LD)"),
		E45(45, "Turn Rate {0}%"),
		E46(46, "Critical Hit Rate {0}%"),
		E50(50, "Magic Damage {0}%"),
		E51(51, "Melee Damage {0}%"),
		E52(52, "Ranged Damage {0}%"),
		E60(60, "Aura (Separate Parsing)"),
		E67(67, "{0}% Stolen BRV Overflow"),
		E103(103, "Unable to act"),
		E114(114, "Unable to act"),
		E199(199, "Raises critical BRV damage dealt by {0}%"),
		E216(216, "Nulls BRV damage under {0}% Int BRV"),
		E257(257, null), //Unknown Cloud BT Buff Effect
		;

		private int id;
		private String baseDescription;

		private EffectType(int id, String desc) { this.id = id; baseDescription = desc; }

		public int getId() { return id; }
		public String getBaseDescription() { return baseDescription; }

		public String getDescription(Integer h, int rankDataIndex, String...extra) {
			if(h == null)
				return getDescription(new String[0], extra);
			return getDescription(Arrays.asList(splitRankData(h, rankDataIndex)).stream()
					.map(i -> i.toString())
					.collect(Collectors.toList()).toArray(new String[0]), extra);
		}
		
		private String getDescription(String[] values, String... extra) {
			values = fix(values, extra);
			String r = baseDescription;
			if(values.length > 0)
				for(int i = 0; i < values.length; i++)
					r = r.replace("{" + i + "}", values[i]);
			r = r.replace("{3}", "");
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
		public Integer[] rankData;

		public EffectGrouping() {}
		public EffectGrouping(int eid) { effectId = eid; }
	}
	public static class Aura{
		public int id;
		public Integer[] requiredConditions, requiredValues;
		
		public int effect, ailmentEffect, target, valType, typeId;
		public Integer[] rankData;
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
		return (isBuff() ? Ailment.Emotes.BUFF_GENERIC.get() : Ailment.Emotes.DEBUFF_GENERIC.get()) + (isFramed() ? "Framed" : "");
	}
	
	private static Integer splitRankData(int i, int idxx) {
		int oI = i;
		try {
			Integer[] result = new Integer[(int)Math.ceil((""+i).toString().length()/3f)];
			int idx = result.length-1;
			while(i > 0) {
				result[idx--] = i % 1000;
				i /= 1000;
			}
			if(result.length == 1 && result[0] == null)
				return 0;
			return result[idxx];
		} catch(Exception e) {
			if (idxx == -1) throw e;
			return splitRankData(oI, idxx-1);
		}
	}
	
	public String generateDescription() {
		if(fakeDesc != null) return fakeDesc;
		if(effects.size() == 0) return null;
		List<String> ret = new LinkedList<String>();
		if(this.rate < 100)
			ret.add(rate + "% chance");
		String str = //(isBuff() ? "Grants " : "Applies ") + 
				(getMaxStacks() > 1 && getArgs().length > 0 ? getArgs()[0] + (getArgs()[0] == 1 ? " stack to " : " stacks to ") : "")
				+ getTarget().getDesc();
		if(getDuration() > 0)
			str += " for " + getDuration() + (getDuration() > 1 ? " turns" : " turn");
		ret.add(str);
		int rankDataIndex = 0;
		EffectGrouping last = null;
		for(EffectGrouping eff : effects) {
			EffectType e = EffectType.get(eff.effectId);
			if(e == null) {
				ret.add("Unknown Effect " + eff.effectId); continue; }
			if(e.getBaseDescription() == null) return "";
			String desc = null;
			Integer data = eff.rankData == null ? null : eff.rankData[this.rank];
			if(last != null) {
				if(eff.effectId <= 6 && last.effectId <= 6){
					if(splitRankData(data, rankDataIndex).intValue() == splitRankData(data, rankDataIndex-1).intValue()) {
						String r = ret.get(ret.size()-1);
						ret.remove(ret.size()-1);
						desc = e.getDescription(data, rankDataIndex, ""+(this.isBuff() ? 1 : 0));
						rankDataIndex = (data != null && ((1000^(rankDataIndex+1)) >= data)) ? rankDataIndex + 1 : rankDataIndex;
						desc = r.replace("{4}", ", " + desc.substring(0, desc.lastIndexOf("}")+1));
						ret.add(desc);
						last = eff;
						continue;
					}
				}else if(eff.effectId == 60){
					desc = "";
					for(Aura a : getAuras().values()) {
						EffectType ae = EffectType.get(a.effect);
						desc += System.lineSeparator() + ae.getDescription(data, 0, ""+(this.isBuff() ? 1 : 0));
					}
					desc = desc.trim();
				}else {
					desc = e.getDescription(data, rankDataIndex, ""+(this.isBuff() ? 1 : 0));
					rankDataIndex = (data != null && ((1000^(rankDataIndex+1)) >= data)) ? rankDataIndex + 1 : rankDataIndex;
				}
			}else {
				desc = e.getDescription(data, rankDataIndex, ""+(this.isBuff() ? 1 : 0));
				rankDataIndex = (data != null && ((1000^(rankDataIndex+1)) >= data)) ? rankDataIndex + 1 : rankDataIndex;
			}
			ret.add(desc);
			last = eff;
		}
		return StringUtils.capitalize(ret.stream().distinct()
				.map(s -> s == null ? s : s.replace("{4}", ""))
				.reduce((s1, s2) -> s1 + System.lineSeparator() + s2).orElse(""));
	}
}