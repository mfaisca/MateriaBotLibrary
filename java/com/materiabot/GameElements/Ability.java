package com.materiabot.GameElements;
import Shared.Methods;
import com.google.common.collect.Streams;
import com.materiabot.GameElements.Ability.Details.Hit_Data;
import com.materiabot.GameElements.Ability.Details.Hit_Data.EffectType;
import com.materiabot.GameElements.Ability.Details.Hit_Data.Target;
import com.materiabot.Utils.ImageUtils;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
//import com.google.common.base.CharMatcher;
import java.util.stream.Collectors;

public class Ability {
	public static enum ChargeRate{
		crEFast(3000, "Fast+++", "Extremely Fast"),
		crVFast(6000, "Fast++", "Very Fast"),
		crFast(9000, "Fast+", "Fast"),
		crFast2(10350, "Fast+", "Fast"),
		crSFast(12000, "Fast", "Slightly Fast"),
		crNormal(14400, "Normal", "Normal"),
		crNormal2(15000, "Normal", "Normal"),
		crSSlow(18000, "Slow", "Slightly Slow"),
		crSlow(21000, "Slow-", "Slow"),
		crVSlow(24000, "Slow--", "Very Slow"),
		
		;private int chargeRate;
		@SuppressWarnings("unused")
		private String GL, JP;
		
		private ChargeRate(int cr, String g, String j) { GL = g; JP = j; chargeRate = cr; }

		public int getChargeRate() { return chargeRate; }
		public String getDesc() { return GL; }
		public static ChargeRate getBy(int chargeRate) {
			for(ChargeRate cr : values()) {
				if(chargeRate <= cr.chargeRate)
					return cr;
			}
			return null;
		}
	}
	public static class Details {
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
		public static enum Attack_Type{
			None(0, "None"),
			Melee(1, "Melee Attack"),
			Ranged(2, "Ranged Attack"),
			Magic(3, "Magic Attack"),
			Ranged_Melee(4, "Melee + Ranged Attack"),
			Magic_Melee(5, "Melee + Magic Attack"),
			Magic_Ranged(-1, "Ranged + Magic Attack"),
			Buff(-1, "Buff"),
			Debuff(-1, "Debuff"),
			Shield(-1, "Shield"),
			Heal(-1, "Heal");

			private int id;
			private String description;
			
			private Attack_Type(int id, String desc) {
				this.id = id;
				this.description = desc;
			}
			
			public int getId() {
				return id;
			}
			public String getDescription() {
				return description;
			}
			
			public static Attack_Type get(int id) {
				return Arrays.asList(values()).stream().filter(t -> t.getId() == id).findFirst().orElse(null);
			}
		}
		public static enum Command_Type{
			Normal(1, "Normal"),
			EX(2, "EX"),
			Unknown(3, "Unknown");

			private int id;
			private String description;
			
			private Command_Type(int id, String desc) {
				this.id = id;
				this.description = desc;
			}
			
			public int getId() {
				return id;
			}
			public String getDescription() {
				return description;
			}
			
			public static Command_Type get(int id) {
				return Arrays.asList(values()).stream().filter(t -> t.getId() == id).findFirst().orElse(null);
			}
		}
		public static enum Target_Type{
			Counter(1, "Counter"),
			Enemy(2, "Enemy"),
			Ally(3, "Ally"),
			OtherAlly(4, "Other Ally"),
			Self(5, "Self"),
			Party(7, "Party");

			private int id;
			private String description;
			
			private Target_Type(int id, String desc) {
				this.id = id;
				this.description = desc;
			}
			
			public int getId() {
				return id;
			}
			public String getDescription() {
				return description;
			}
			
			public static Target_Type get(int id) {
				return Arrays.asList(values()).stream().filter(t -> t.getId() == id).findFirst().orElse(null);
			}
		}
		
		public static class Hit_Data implements Cloneable{
			public static enum BasedOnStat{
				Stat1(1, "???"),
				Stat2(2, "Attack"),
				Stat3(3, "Initial BRV"),
				Stat4(4, "Max BRV"),
				Stat5(5, "Current BRV"),
				Stat6(6, "Max HP"),
				Stat7(7, "Current HP"),
				Stat8(8, "Initial BRV"),
				Stat9(9, "Max BRV"),
				Stat10(10, "Current BRV"),
				Stat11(11, "Max HP"),
				Stat12(12, "Target Current BRV"), //Locke unused skills?
				Stat13(13, "BRV Damage Dealt"),
				Stat14(14, "HP Damage Dealt"),
				Stat15(15, "Attack"),
				Stat16(16, "Attack"),
				Stat21(21, "highest current BRV"),
				Stat22(22, "Attack"),
				Stat29(29, "Unknown"), //Jecht unused skill?
				Stat36(36, "HP Damage Dealt"), //Serah EX only?
				Stat37(37, "total Current HP"), //Unique to Ignis?
				Stat41(41, "gravity damage dealt"), //Unique to Zidane Mug?
				Stat42(42, "HP Damage Dealt"),
				Stat46(46, "Attack at random"), //Wakka EX only, Random between 4 values on arguments
				Stat49(49, "Attack"),
				;
				
				private int id;
				private String stat;
				
				private BasedOnStat(int id, String stat) { this.id = id; this.stat = stat; }

				public int getId() { return id; }
				public String getStat() { return stat; }

				public static BasedOnStat get(int id) {
					return Arrays.asList(values()).stream().filter(t -> t.getId() == id).findFirst().orElse(null);
				}
			}
			
			public static enum Type{
				BRV(1), HP(2), Other(6), HPSplash(7), BRVIgnoreDEF(14), SketchSummon(15);
				
				private int id;
				
				private Type(int id) { this.id = id; }

				public int getId() {
					return id;
				}
				
				public static Type get(int id) {
					return Arrays.asList(values()).stream().filter(t -> t.getId() == id).findFirst().orElse(null);
				}
			}
			public static enum Attack_Type{
				None(0),
				Melee(1),
				Ranged(2),
				Magic(3);
				
				private int id;
				
				private Attack_Type(int id) { this.id = id; }
				
				public int getId() { return id; }
				public static Attack_Type get(int id) {
					return Arrays.asList(values()).stream().filter(t -> t.getId() == id).findFirst().orElse(null);
				}
			}
			public static enum Target{
				ST(1, "target"),
				Self(2, "own"),
				Random(3, "random targets"), //Kuja/Lenna only
				AoE(5, 21, "all enemies"),
				Party(6, 8, "party"),
				Allies(7, "allies"),
				Splash(10, "splash"),
				Alternating(11, 18, "split between enemies"),
				Ally(13, "ally"),
				Traps(18, "traps???"), //Emperor only(S2 / EX)
				Terra(29, "Terra"),
				AOE(23, "traps???"), //Emperor only
				Machina(28, "Machina"),
				Caller(29, "caller"),
				
				;private int id, id2;
				private String desc;

				private Target(int id, String desc) {this.id = id; this.desc = desc; }
				private Target(int id, int id2, String desc) {this.id = id; this.id2 = id2; this.desc = desc; }

				public int getId() {
					return id;
				}
				public int getId2() {
					return id2;
				}
				public String getDesc() {
					return desc;
				}
				public static Target get(int id) {
					return Arrays.asList(values()).stream().filter(t -> t.getId() == id || t.getId2() == id).findFirst().orElse(null);
				}
			}
			public static enum EffectType{
				EN1(-1, null, true),//Basic Hits - Exclusive to Yuri
				E1(1, null, true), //Basic Hits
				E7(7, "Removes {0} debuffs from {t}"),//(#ofRemovedDebuffs[, ?]) First can be -1 for all
				E8(8, "Removes {0} buffs from {t}"),//(#ofRemovedBuffs[, ?, ?])  First can be -1 for all
				E25(25, "BRV Hits are guaranteed hits", true, false),
				E33(33, "Give turn to selected ally"),//(-1) Argument unknown
				E34(34, "{0}"),//Angel Wing Unique Buff - ([1]) No value = remove, Value = give
				E37(37, "Dispels 「**{0}**」", false, false),//(ID of buff)
				E38(38, "Recover {0} of {2}"), //(1) or (-1, -1) - First is both, second is only skill1
				E41(41, "Raises {t} BRV by {0}% of {evt}"), //Battery - (Potency[[, ?], ?, ?]) - EffectValueType says what stat its based on
															//For effect_value_type = 46, there's 4 potencies and one is picked at random
				E42(42, "{0}% Gravity on {t}"),//(% of shave)
				E43(43, "Recover {t} HP by {0}% of {evt}"), //(Potency[, ?]) - EffectValueType says what stat its based on
				E44(44, null, false), //HP Splash Damage - (% of splash)
				E46(46, "Delays {t} by {0}"), //(# of turns)
				E48(48, "{1}% chance to steal {0} buffs from {t}"), //(# of buffs, ?(-1), success%, ?(-1)) OR (# of buffs, success%, ?(-1)) OR (# of buffs, success%, ?(-1))
				E48_2(48, "{1}% chance to steal {0} buffs from {t}" + System.lineSeparator() + 
						  "Extend stolen buff by 1 turn"),
				E50(50, null), //Yuri Only
				E51(51, "Transfer own debuffs to all enemies" + System.lineSeparator() + 
						"Extends transferred debuff duration by {0}"), //(Duration extended, ?, ?, ?) OR (Duration extended, ?, ?) - It always transfers to all, even though target is 1
				E52(52, "Reduce {t} Current HP by {0}%"), //(%, ?) - Cecil, Rinoa, Yuri, Vivi
				E54(54, "Revive {t} with {0}% of {evt}"), //(% of effectValueType, ?)
				E55(55, null), //(100) - Balthier Great Aim and Yuffie I dont need this, but unknown what it refers to
				E57(57, "{0}% chance to BREAK {t}", true), //(success%)
				E58(58, "BRV hits have a random potency between {0}% and {1}%", true, false), //(minPower, maxPower, ?(3), ?(-1)) - Shadow Exclusive - minPower and maxPower are in tens(4, 6 = 40%, 60%)
				E61(61, "Raises party BRV by {0}% of {t} {evt}"), //(copy%)
				E65(65, "Restores {t} HP by {0}% of {evt}, up to {1}% Max HP"), //(Potency, MaxHP%Healed) EffectValueType = What damage it is based on
				E69(62, null), //(X) || Unique to Ignis Regroup - Unknown Effect, others are accounted for
				E70(70, "Raises BRV by {0}% of {t} {evt}"), //EffectValueType 2 = Copy Target BRV || Otherwise table
				E72(72, "Raises turn rate by {0}% when breaking or hitting broken target", true, false), //(New Cost[, ?(-1)]))
				E73(73, "BRV Hits are guaranted hits & 50% Bonus BRV potency if target not targetting self", true, false), //([?]) - No params - Fucking Lion
				E78(78, "BRV Hits apply a stacking IBRV debuff", true, false), //Lenna Rapid Fire mechanic
				E80(80, "Copy {0} random buff and extend its duration by 1"), //(?, ?, ?, ?) - Yuffie Snatch
				E81(81, "Increase BRV potency by {0}% when targetted", true, false), //(Base Multiplier(100), Targetted Multipler(300), ?, ?, ?) - Zack
				E82(82, "BRV Hits are critical hits if target not targetting self", true, false), //([?]) - No params - Jack
				E84(84, null), //Old Vanille Data
				E89(89, "{2} 「**{1}**」 stacks by {0}"), //(# of stacks to increase, buffID)
				E90(90, "Moves own next turn to just before the target's next turn"),
				E91(91, "Raises {t} BRV by {0}% of {evt} when breaking"),
				E93(93, "Adds an extra hit with 30% ~ 120% BRV potency based on 「**{0}**」 stacks"), //([-1]) Noctis unique hit (30/60/80/100/120)
				E94(94, "Raises BRV potency by {0}% per 1750? {1} amount on the party", true, false), //(10, -1) Unknown how to formulate it
				E97(97, "Increases BRV hits potency up to {0}% based on how much HP you're missing", true, false), //(Potency, -1) Terra EX
		/**/	E99(99, "Restores {t} HP by {0}% of {evt}, up to {1}% Max HP"), 	//(Potency[, MaxHP%Healed, ?]) EffectValueType = What damage it is based on
				E100(100, "Restores {t} HP by {0}% of {evt}, up to {1}% Max HP" + System.lineSeparator() + 
						  "{2}% of excess healing is converted to BRV"), //(Potency, MaxHP%Healed, YYY100, ?, ?) YYY = 100(%) / 300(%)
				E102(102, null),		//Cait Sith Only
				E103(103, null),		//Cait Sith Only	//I have no fucking idea how the arguments work
				E104(104, null), //Cheating Andy 	//Cait Sith Only  //EffectValueType
				E105(105, "(25% potency + 5% per Total Dice Value)"),  	//Cait Sith Only  //EffectValueType
				E106(106, null, true, false), //(Overflow%) - Mentions overflow through an argument instead of the regular field, older model perhaps?
				E107(107, null, true), // 100% AoE HP Damage
				E110(110, "Doesn't increase turn count"),
				E111(111, null), //Old Data? Barret Counter
				E113(113, "Extends self-buffs by {0}"), //Prishe Only? (X, 1, -1)
				E114(114, "Initiates a chase sequence ({0} [CU](https://www.reddit.com/r/DissidiaFFOO/comments/7x7ffp/chase_mechanic/))) if the target is broken"),
				E115(115, "Raises BRV Damage by {0}% against ST", true, false), //(X, -1)
				E116(116, null),
				E117(117, "Raises BRV Damage by {0}% against Broken Targets", true, false), //(X)
				E120(120, "Raises BRV Damage by {1}% against target with 「**Turn Rate Down**」 or 「**SPD Down**」", true, false), //(1, X)
				E121(121, "{0}"), //(X, buffId) || (X, 2, -1)
				E122(122, "Raises {t} BRV by {0}% of {evt}"), //(X) || effectvaluetype = stat its based on
				E124(124, "Restores {t} HP by {0}% of {evt}, up to {2}% Max HP" + System.lineSeparator() + 
						  "{1}% of excess healing is added to allies HP, up to {3}% Max HP"),
				E125(125, "Cancels {t} BREAK status"),
				E126(126, "Restores {t} HP by {0}% of {evt}" + System.lineSeparator() + "Allows overhealing up to {1}%"), //Porom only (X, Y, ?)
				E128(128, null), //Alphinaud Only - Something related to his summon?
				E129(129, "Release pet when broken???"), //Alphinaud Only
				E131(131, "Raises {t} BRV by {0}% of {evt} (except the one with highest current BRV)"), //(X) || EffectValueType = 21 ||| Setzer Only
				E132(132, null), //(X) || Unique to Ignis Regroup - Unknown Effect, others are accounted for
				E135(135, "Raises Critical BRV Damage by {0}%", true, false),
				E136(136, "Recover {0} of 「**{2}**」"), //(#ofUses, 100, skillID)
				E137(137, "Deals HP damage to {t} equal to party current BRV"), //(100) / (1, 100) || Sherlotta Only
				E139(139, "Delays {t} by {0} delay if 「**{1}**」 is active"), //(X, Y) || Garland Only
				E140(140, null),
				E141(141, null),
				E142(142, "Reduce target's BRV by {0}% based on own {evt}"), //(X) - Y = effect_value_type = based on stat X
				E143(143, "{2} 「**{1}**」 stacks by {0} when breaking target", true, false), //(New Cost[, ?(-1)]))
				E147(147, null), //Ignis EX Only - Dead skill
				E151(151, "100% chance to BREAK {t}{0}"),
				E153(153, "Resets 「**{1}**」{0}"),
				E154(154, null),
				E155(155, null),
				E156(156, "Lower {t} BRV by {0}% of {evt}, battery self for {0}% of {evt}{1}"),
				E157(157, "Lower {t} BRV by {0}% of {evt}, battery party for {0}% of {evt}"),
				E159(159, null), //Aphmau / Ulti BT
				E160(160, "Move your next turn to just before the target next turn"),
				E164(164, "High Turn Rate when attacking a broken target", true, false),
				E165(165, "Instant Turn Rate and free ability use next turn(except LD) when breaking target", false, false),
				E176(176, "Sets HP to {0}"),
				E177(177, "Reverts base abilities to 「**Concentrate**」 for 1 turn"),
				E180(180, "Removes {0} buffs from {t} if broken"),
				E186(186, null), //Cloud BT Unknown Effect
				E187(187, null, true), //Gabranth ???
				E197(197, "Extends debuffs duration +{0} turns"),
				E190(190, "Delete {t} next turn"),
				E200(200, "Restores {t} HP by {0}% of {evt}, up to {1}% Max HP"),
				;

				private int id;
				private String baseDescription;
				private boolean abilityPower = false;
				private boolean allowRepeats = true;

				private EffectType(int id, String desc) { this.id = id; baseDescription = desc; }
				private EffectType(int id, String desc, boolean power) { this.id = id; baseDescription = desc; abilityPower = power; }
				private EffectType(int id, String desc, boolean power, boolean repeats) { this.id = id; baseDescription = desc; abilityPower = power; allowRepeats = repeats; }

				public int getId() { return id; }
				public String getBaseDescription() { return baseDescription; }
				public boolean isAbilityPower() { return abilityPower; }
				public boolean allowRepeats() { return allowRepeats; }

				private static class FixReturn{
					String[] values;
					String effectValueType;
					String target;
				}
				
				public String getDescription(Unit u, Details d, Hit_Data h) {
					return getDescription(u, d, h, Arrays.stream(h.getArguments()).map(i -> i.toString()).collect(Collectors.toList()).toArray(new String[0]));
				}
				public String getDescription(Unit u, Details d, Hit_Data h, String... values) {
					FixReturn ret = fix(u, d, h, values);
					String r = baseDescription;
					for(int i = 0; i < ret.values.length; i++)
						r = r.replace("{" + i + "}", ret.values[i] != null ? ret.values[i] : "");
					if(r.contains("{t}"))
						r = r.replace("{t}", ret.target);
					if(r.contains("{evt}"))
						r = r.replace("{evt}", ret.effectValueType);
					return r;
				}
				private FixReturn fix(Unit u, Details d, Hit_Data h, String[] v) {
					FixReturn ret = new FixReturn();
					ret.values = v;
					ret.effectValueType = ""+h.getEffect().getEffectValueType();
					if(h.getTarget() != null)
						ret.target = h.getTarget().getDesc();
					switch(id) {
						case 7: //Removes {0} debuffs from {t} - #ofRemovedDebuffs[, ?]) First can be -1 for all
						case 8: //Removes {0} buffs from {t}   - #ofRemovedDebuffs[, ?]) First can be -1 for all
							if(v[0].equals("-1")) v[0] = "all"; break;
						case 34: //"{0}") - Angel Wing Unique Buff - ([1]) No value = remove, Value = give
							if(v.length > 0) 
								v[0] = "Grants 「**Angel Wing**」 for 3 turns";
							else {
								ret.values = new String[] {"Dispels 「**Angel Wing**」"};
							}break;
						case 37:{ //Dispels 「**{0}**」 - (ID of buff) / (X, IDBuff)
							Ailment ail = u.getSpecificAilment(Integer.parseInt(v[v.length == 2 ? 1 : 0]));
							v[0] = v[0].toString().equalsIgnoreCase("-1") ? "all" : ((ail != null ? ail.getName() : "Unknown Ailment ID: " + v[0]));
							break; }
						case 38: //Recover {0} of {2} - (-1) = S1/AA all uses | (1) = S1/S2 1 use
							ret.values = new String[3];
							for(int i = 0; i < ret.values.length; i++)
								ret.values[i] = i < v.length ? v[i] : null;
							v = ret.values;
							if(v[0].equals("-1")) { //King S2
								v[0] = "all uses"; v[2] = "「**" + u.getBaseAbility(Ability.Type.S1).get(0).getName() + "**」, 「**" + u.getBaseAbility(Ability.Type.AA).get(0).getName() + "**」";}
							if(v[0].equals("1")) { //Selphie AA
								v[0] = "1 use"; v[2] = "「**" + u.getBaseAbility(Ability.Type.S1).get(0).getName() + "**」, 「**" + u.getBaseAbility(Ability.Type.S2).get(0).getName() + "**」";}
							break;
						case 41: //Doesn't have break ON PURPOSE, Wakka effect that makes a random 1/4 potency, then goes to BasedByStat
							if(h.getEffect().getEffectValueType() == 46)
								v[0] = v[0] + "/" + v[1] + "/" + v[2] + "/" + v[3];
							else if(h.getId() == 6074) //Deuce for her HP+ being based on # of notes
								v[0] = v[0] + "/" + v[1] + "/" + v[2];
						case 43:
						case 54:
						case 65: //(Potency, MaxHP%Healed)
						case 91:
						case 99:
						case 122:
						case 124:
						case 126:
						case 131:
						case 200:
							ret.effectValueType = BasedOnStat.get(h.getEffect().getEffectValueType()).getStat();
							break;
						case 48:
						case 48_2:
							v[1] = v.length == 3 ? v[2] : v[1]; 
							//Not Breaking on purpose
						case 46:
						case 51:
						case 113:
							v[0] = (v[0].equals("1") ? "1 turn" : v[0] + " turns");
							break;
						case 58:
							v[0] = v[0] + "0";
							v[1] = v[1] + "0";
							int fixBrvRate = (Integer.parseInt(v[1]) + Integer.parseInt(v[0]))/2;
							d.hits.forEach(dh -> dh.brvRate = fixBrvRate);
							break;
						case 70:
							ret.effectValueType = BasedOnStat.get(h.effect.effectValueType == 2 ? 12 : h.effect.effectValueType).getStat();
							break;
						case 72:
							v[0] = ""+(100-(int)((Integer.parseInt(v[0]) / (float)d.getMovementCost())*100));
							break;
						case 81:
							v[0] = ""+(Integer.parseInt(v[1]) / Integer.parseInt(v[0]))*100;
							break;
						case 89:
						case 143:{ //Increases 「**{1}**」 stacks by {0} - (# of stacks to increase, buffID)
							Ailment ail = u.getSpecificAilment(Integer.parseInt(v[1]));
							v[1] = v[1].toString().equalsIgnoreCase("-1") ? "all" : ((ail != null ? ail.getName() : "Unknown Ailment ID: " + v[1]));
							ret.values = new String[]{v[0].replace("-", ""), v[1], v[0].contains("-") ? "Decreases" : "Increases"};
							break; }
						case 93:
							ret.values = new String[] {u.getSpecificAilment(358).getName()};
							break;
						case 94:
							ret.values = new String[] {v[0], u.getSpecificAilment(330).getName()};
							break;
						case 100: //(Potency, MaxHP%Healed, YYY100, ?, ?) YYY = 100(%) / 300(%)
							v[2] = v[2].substring(0, 3);
							ret.effectValueType = BasedOnStat.get(h.getEffect().getEffectValueType()).getStat();
							break;
						case 106: //Old data? Sets overflow through argument instead of default field
							h.setMaxBrvOverflow(Integer.parseInt(v[0]));
							break;
						case 121: //(X, buffId) || (X, 2, -1)
							if(v.length == 3) {
								v[1] = v[0];
								v[0] = "Extends existing buffs by {1}";
								v[1] = v[1].equals("1") ? "1 turn" : v[1] + " turns";
							}
							else if(v.length == 2) {
								v[0] = "Extends 「**{1}**」 by " + (v[0].equals("1") ? "1 turn" : v[0] + " turns");
								Ailment ail = u.getSpecificAilment(Integer.parseInt(v[1]));
								v[1] = v[1].toString().equalsIgnoreCase("-1") ? "all" : ((ail != null ? ail.getName() : "Unknown Ailment ID: " + v[1]));
							}
							else
								v[0] = "**Unknown Effect Variant**";
							break;
						case 136: //Recover {0} of {2} - (#ofUses, 100, skillID)
							Ability ab = u.getSpecificAbility(Integer.parseInt(v[2]));;
							v[0] = v[0].equals("-1") ? "all uses" : (v[0].equals("1") ? "1 use" : (v[0] + " uses"));
							v[2] = v[2].equals("-1") ? "both abilities" : (ab != null ? ab.getName() : ("Unknown Skill ID: " + v[2]));
							break;
						case 139:{
							v[0] = (v[0].equals("1") ? "1 turn" : v[0] + " turns");
							Ailment ail = u.getSpecificAilment(Integer.parseInt(v[1]));
							v[1] = v[1].toString().equalsIgnoreCase("-1") ? "all" : ((ail != null ? ail.getName() : "Unknown Ailment ID: " + v[1]));
							break;}
						case 142:
							ret.effectValueType = BasedOnStat.get(h.getEffect().getEffectValueType()).getStat();
							break;
						case 151:
							v[0] = v[0].equals("2") ? " if BRV is even" : "";
							break;
						case 153:{
							if(v.length == 2) {
								v[0] = " to " + (v[0].equals("1") ? "1 stack" : v[0] + " stacks");
								Ailment ail = u.getSpecificAilment(Integer.parseInt(v[1]));
								v[1] = v[1].toString().equalsIgnoreCase("-1") ? "all" : ((ail != null ? ail.getName() : "Unknown Ailment ID: " + v[1]));
							}else {
								ret.values = v = new String[] {" charge", v[0]};
								Ability ab1 = u.getSpecificAbility(Integer.parseInt(v[1]));
								v[1] = v[1].toString().equalsIgnoreCase("-1") ? "all" : ((ab1 != null ? ab1.getName() : "Unknown Ability ID: " + v[1]));
							}
							break;}
						case 156:{
							if(v[0].length() > 4) { //ExDeath S2
								v[1] = v[0].substring(0, 3) + v[1].substring(3, 9);
								v[0] = v[0].substring(0, 3);
								Ailment ail = u.getSpecificAilment(Integer.parseInt(v[2]));
								v[2] = ail != null ? ail.getName() : "Unknown Ailment ID: " + v[2];
								v[1] = System.lineSeparator() + "Potencies above increase by 50% for each stack of 「**{2}**」 on target";
							} else //ExDeath S1
								v[1] = ""; }
						case 157: //ExDeath EX
							ret.effectValueType = BasedOnStat.get(h.getEffect().getEffectValueType()).getStat();
							break;
					}
					return ret;
				}
				
				public static EffectType get(int id) {
					for(EffectType e : values())
						if(e.getId() == id)
							return e;
					return null;
				}
			}
			public static class Effect{
				private EffectType effect;
				private int effectValueType;

				public Effect() {}
				public Effect(EffectType effect, int effectValueType) {
					this.effect = effect;
					this.effectValueType = effectValueType;
				}

				public EffectType getEffect() {
					return effect;
				}

				public void setEffect(EffectType effect) {
					this.effect = effect;
				}

				public int getEffectValueType() {
					return effectValueType;
				}

				public void setEffectValueType(int effectValueType) {
					this.effectValueType = effectValueType;
				}
			}
			
			private int id;
			private Type type;
			private String fakeDesc;
			private Integer[] arguments;
			private Attack_Type attackType;
			private Target target;
			private List<Element> elements = new LinkedList<Element>();
			private Effect effect;
			private int brvRate;
			private int maxBrvOverflow = 100;
			private int maxBrvOverflowOnBreak = 0;
			private int singleTargetBrvRate = 0;
			private int brvDamageLimit = 0;
			private int maxBrvLimit = 0;
			
			public Hit_Data() {}
			public Hit_Data(String desc) { fakeDesc = desc; }
			
			public int getId() {
				return id;
			}
			public void setId(int id) {
				this.id = id;
			}
			public Type getType() {
				return type;
			}
			public void setType(Type type) {
				this.type = type;
			}
			public Integer[] getArguments() {
				return arguments;
			}
			public void setArguments(Integer[] arguments) {
				this.arguments = arguments;
			}
			public Attack_Type getAttackType() {
				return attackType;
			}
			public void setAttackType(Attack_Type attackType) {
				this.attackType = attackType;
			}
			public Target getTarget() {
				return target;
			}
			public void setTarget(Target target) {
				this.target = target;
			}
			public List<Element> getElements() {
				return elements.stream().filter(e -> !e.equals(Element.Null)).collect(Collectors.toList());
			}
			public void addElements(List<Element> le) {
				elements.addAll(le);
			}
			public Effect getEffect() {
				return effect;
			}
			public void setEffect(Effect effect) {
				this.effect = effect;
			}
			public String getFakeDesc() {
				return fakeDesc;
			}
			public int getBrvRate() {
				return brvRate;
			}
			public void setBrvRate(int brvRate) {
				this.brvRate = brvRate;
			}
			public int getMaxBrvOverflow() {
				return maxBrvOverflow;
			}
			public void setMaxBrvOverflow(int maxBrvOverflow) {
				this.maxBrvOverflow = maxBrvOverflow;
			}
			public int getMaxBrvOverflowOnBreak() {
				return maxBrvOverflowOnBreak;
			}
			public void setMaxBrvOverflowOnBreak(int maxBrvOverflowOnBreak) {
				this.maxBrvOverflowOnBreak = maxBrvOverflowOnBreak;
			}
			public int getSingleTargetBrvRate() {
				return singleTargetBrvRate;
			}
			public void setSingleTargetBrvRate(int singleTargetBrvRate) {
				this.singleTargetBrvRate = singleTargetBrvRate;
			}

			public int getBrvDamageLimit() {
				return brvDamageLimit;
			}
			public void setBrvDamageLimit(int brvDamageLimit) {
				this.brvDamageLimit = brvDamageLimit;
			}
			public int getMaxBrvLimit() {
				return maxBrvLimit;
			}
			public void setMaxBrvLimit(int maxBrvLimit) {
				this.maxBrvLimit = maxBrvLimit;
			}

			@Override
			public boolean equals(Object other) {
				return ((Hit_Data)other).getFakeDesc() != null && this.getFakeDesc() != null ? 
							((Hit_Data)other).getFakeDesc().equals(this.getFakeDesc()) : 
							super.equals(other);
			}
			public Hit_Data clone() {
			    try {
					return (Hit_Data)super.clone();
				} catch (CloneNotSupportedException e) {
					return null;
				}
			}
		}
		
		private int movementCost;
		private Attack_Type attackType;
		private Command_Type commandType;
		private Target_Type targetType;
		private int chaseDmg; //can_initiate_chase * chase_dmg
		private List<Hit_Data> hits = new LinkedList<Hit_Data>();
		private List<Ailment> ailments = new LinkedList<Ailment>();
		
		public int getMovementCost() {
			return movementCost;
		}
		public void setMovementCost(int movementCost) {
			this.movementCost = movementCost;
		}
		public Attack_Type getAttackType() {
			return attackType;
		}
		public void setAttackType(Attack_Type attackType) {
			this.attackType = attackType;
		}
		public Command_Type getCommandType() {
			return commandType;
		}
		public void setCommandType(Command_Type commandType) {
			this.commandType = commandType;
		}
		public Target_Type getTargetType() {
			return targetType;
		}
		public void setTargetType(Target_Type targetType) {
			this.targetType = targetType;
		}
		public int getChaseDmg() {
			return chaseDmg;
		}
		public void setChaseDmg(int chaseDmg) {
			this.chaseDmg = chaseDmg;
		}
		public List<Hit_Data> getHits() {
			return hits;
		}
		public List<Ailment> getAilments() {
			return ailments;
		}
	}
	
	public static enum Type{
		BRV("brv", "brv+", "brv++", "brv+++", "brv++++"), HP("hp", "hp+", "hp++", "hp+++", "hp++++"), 
		S1("s1", "1"), S2("s2", "2"), AA("aa", "additional"), EX("ex", "ex+"), 
		LD("ld", "limited"), BT("bt", "burst"), CA("ca", "call");
		private List<String> names = new LinkedList<String>();
		
		private Type(String... skillNames) { 
			names = Arrays.asList(skillNames);
		}
		
		public List<String> getNames(){ return names; }
		
		public static Type getByTags(String s) {
			for(Type t : values())
				if(t.names.contains(s.replaceAll("\\+", "").toLowerCase()))
					return t;
			return null;
		}
	}
	public static class UpgradedAbility{
		public int id;
		public Ability original;
		public Ability upgrade;
		public Type type;
		public List<Integer> reqExtendPassives = new LinkedList<Integer>();
		public List<Integer> reqWeaponPassives = new LinkedList<Integer>();
	}
	
	private int id;
	private String name;
	private String description;
	private int useCount;
	private Type type;
	private Unit unit;
	private Details details = new Details();
	
	public Ability() {}
	public Ability(int id) { this.id = id; }

	public int getId() { return id; }
	public void setId(int id) { this.id = id; }
	public String getName() {
//		if(CharMatcher.ascii().matchesAllOf(name))
//			return name;
		return name;
	}
	public void setName(String name) { this.name = name; }
	public String getDescription() { return description; }
	public void setDescription(String description) { this.description = description; }
	public int getUseCount() { 
		if(useCount == -1) return 0;
		return unit.getBaseAbility(type) == null ? 0 : unit.getBaseAbility(type).get(0).useCount + Streams.concat(
								unit.getEquipment().stream().flatMap(e -> e.getPassives().stream()),
								unit.getJPPassives().values().stream())
							.filter(Methods.distinctByKey(p -> p.getId()))
							.flatMap(e -> e.getEffects().stream())
							.map(e -> e.getValue1())
							.filter(e -> e.type == Passive.Effect.E22 
										&& unit.getBaseAbility(type).get(0).getId() == e.values[0].intValue())
							.map(e -> e.values[1])
							.reduce((v1, v2) -> v1 + v2).orElse(0);
	}
	public void setUseCount(int useCount) { this.useCount = useCount; }
	public Type getType() { return type; }
	public void setType(Type type) { this.type = type; }
	public Unit getUnit() { return unit; }
	public void setUnit(Unit unit) { this.unit = unit; }
	public Details getDetails() { return this.details; }
	public void setDetails(Details details) { this.details = details; }
	public Hit_Data getHitDataById(int hitDataId) {
		return getDetails().getHits().stream().filter(hdd -> hdd.getId() == hitDataId).findFirst().orElse(null);
	}
	public void removeHitDataById(int hitDataId) {
		Hit_Data hd = getHitDataById(hitDataId);
		if(hd != null)
			getDetails().getHits().remove(hd);
	}
	public Ailment getAilmentById(int ailmentId) {
		return getDetails().getAilments().stream().filter(ai -> ai.getId() == ailmentId).findFirst().orElse(null);
	}
	public Ailment removeAilmentById(int ailmentId) {
		Ailment a = getAilmentById(ailmentId);
		if(a != null)
			getDetails().getAilments().remove(a);
		return a;
	}
	public void fixStupidCriticalDamage(int ailmentId, int critDamagePercentage) {
		Hit_Data hdd = this.getDetails().getHits().stream()
				.filter(hd -> hd.getEffect() != null)
				.filter(hd -> hd.getEffect().getEffect().equals(Ability.Details.Hit_Data.EffectType.E37) && hd.getArguments()[0] == ailmentId)
				.findFirst().orElse(null);
		if(hdd != null)
			removeHitDataById(hdd.getId());
		removeAilmentById(ailmentId);
		getDetails().getHits().stream()
			.filter(hd -> hd != null && hd.getType() != null && hd.getEffect() != null)
			.filter(hd -> hd.getType().equals(Ability.Details.Hit_Data.Type.BRV) || hd.getType().equals(Ability.Details.Hit_Data.Type.BRVIgnoreDEF))
			.peek(hd -> hd.setArguments(new Integer[]{critDamagePercentage}))
			.limit(1)
			.forEach(hd -> hd.getEffect().setEffect(Ability.Details.Hit_Data.EffectType.E135));
	}
	public void fixAddAuraEffect(int ailmentId) {
		Ailment a = this.getAilmentById(ailmentId);
		if(a != null) {
			if(a.getEffects().stream().noneMatch(e -> e.effectId == Ailment.EffectType.E60.getId()))
				a.getEffects().add(new Ailment.EffectGrouping(Ailment.EffectType.E60.getId()));
		}
	}
	public Ailment fixAddAilment(int ailmentId) {
		Ailment a = this.getUnit().getSpecificAilment(ailmentId);
		if(a != null) {
			if(!this.getDetails().getAilments().contains(a))
				this.getDetails().getAilments().add(a);
		}
		return a;
	}
	public Ailment removeTemporaryEffect(int ailmentId) {
		Ailment a = this.removeAilmentById(ailmentId);
		if(a != null) {
			Hit_Data hdd = this.getDetails().getHits().stream()
							.filter(hd -> hd.getEffect().getEffect().equals(Ability.Details.Hit_Data.EffectType.E37) && hd.getArguments()[0] == ailmentId)
							.findFirst().orElse(null);
			if(hdd != null)
				this.removeHitDataById(hdd.id);
		}
		return a;
	}
	public Ailment.Aura fixMissingAuraAilment(int ailmentId, int auraId, Ailment.EffectType ailmentEffect, Ailment.Target ailmentTarget) {
		Ailment ail = this.getAilmentById(ailmentId);
		if(ail == null) return null;
		Ailment.Aura aura = ail.getAuras().get(auraId);
		if(aura == null) return null;
		if(ailmentEffect != null)
			aura.ailmentEffect = ailmentEffect.getId();
		if(ailmentTarget != null)
			aura.target = ailmentTarget.getId();
		return aura;
	}
	public void fixDelayHitData(int hitDataId) {
		Ability.Details.Hit_Data hd = getHitDataById(hitDataId);
		if(hd != null) {
			getDetails().getHits().remove(hd);
			getDetails().getHits().add(hd);
		}
	}
	public void fixRemoveDispels() {
		java.util.Iterator<Hit_Data> iter = this.getDetails().getHits().iterator();
		while(iter.hasNext()) {
			Hit_Data hd = iter.next();
			if(hd.getEffect() != null && hd.getEffect().getEffect() == EffectType.E37)
				iter.remove();
		}
	}
	public Ailment addStaticAilmentEffect(int ailmentId, String text) {
		return addStaticAilmentEffect(ailmentId, text, null);
	}
	public Ailment addStaticAilmentEffect(int ailmentId, String text, Integer order) {
		Ailment ail = getAilmentById(ailmentId);
		if(ail == null) return null;
		if(order != null)
			ail.getEffects().add(order, new Ailment.EffectGrouping(text));
		else
			ail.getEffects().add(new Ailment.EffectGrouping(text));
		return ail;
	}
	public Hit_Data addStaticHit(String text) {
		return addStaticHit(text, null);
	}
	public Hit_Data addStaticHit(String text, Integer order) {
		Hit_Data adh = new Ability.Details.Hit_Data(text);
		if(!getDetails().getHits().contains(adh)) {
			if(order != null)
				getDetails().getHits().add(order, adh);
			else
				getDetails().getHits().add(adh);
		}
		return adh;
	}
	public int hitDataCount = -1;
	public void fixMergeAbility(int abilityId) {
		Ability a2 = this.getUnit().getSpecificAbility(abilityId);
		if(hitDataCount == -1)
			hitDataCount = this.getDetails().getHits().size();
		else if(hitDataCount <= 100) {
			this.getDetails().getHits().addAll(a2.getDetails().getHits());
			this.getDetails().getAilments().addAll(a2.getDetails().getAilments());
			hitDataCount = this.getDetails().getHits().size()+200;
		}
	}
	public Hit_Data addHits(Details.Hit_Data.Attack_Type attackType, Hit_Data.Type type, Target target) {
		return addHits(attackType, type, target, 0, 0);
	}
	public Hit_Data addHits(Details.Hit_Data.Attack_Type attackType, Hit_Data.Type type, Target target, int potency, int overflow) {
		Hit_Data adh = new Hit_Data();
		adh.setAttackType(attackType);
		adh.setBrvRate(potency);
		adh.setEffect(new Hit_Data.Effect(EffectType.E1, 0));
		adh.setMaxBrvOverflow(overflow);
		adh.setTarget(target);
		adh.setType(type);
		getDetails().getHits().add(adh);
		return adh;
	}
	public Hit_Data addHits(Details.Hit_Data.Attack_Type attackType, Hit_Data.Type type, Target target, int potency, int stBoost, int overflow) {
		Hit_Data adh = new Hit_Data();
		adh.setAttackType(attackType);
		adh.setBrvRate(potency);
		adh.setSingleTargetBrvRate(stBoost);
		adh.setEffect(new Hit_Data.Effect(EffectType.E1, 0));
		adh.setMaxBrvOverflow(overflow);
		adh.setTarget(target);
		adh.setType(type);
		getDetails().getHits().add(adh);
		return adh;
	}
	public Hit_Data addEffectHit(EffectType effectType, Target target, Integer order, Integer... args) {
		Hit_Data adh = new Hit_Data();
		adh.arguments = args;
		adh.setAttackType(Details.Hit_Data.Attack_Type.None);
		adh.setBrvRate(0);
		adh.setEffect(new Hit_Data.Effect(effectType, 0));
		adh.setMaxBrvOverflow(100);
		adh.setTarget(target == null ? Target.Self : target);
		adh.setType(Hit_Data.Type.Other); 
		if(order != null)
			getDetails().getHits().add(order, adh);
		else
			getDetails().getHits().add(adh);
		return adh;
	}
	public void setMastery(int bId, int uId, int uses) {
		if(getId() == bId)
			addStaticHit("Mastery after " + uses + " use" + (uses == 1 ? "" : "s"), 0);
		else if(getId() == uId)
			addStaticHit("Enabled after mastering 「**" + unit.getSpecificAbility(bId).getName() + "**」", 0);
	}
	
	private static class EffectBuilder{
		int count = 1;
		String desc = null;
		boolean merge;
		boolean hp;
		int attackTypeId;
		List<Element> elements = null;
		
		public EffectBuilder(String d) { desc = d; merge = false; hp = false; attackTypeId = -1; }
		public EffectBuilder(String d, boolean m, boolean hpp, int at) { desc = d; merge = m; hp = hpp; attackTypeId = at; }
		public void increase() { count++; }
		
		public boolean equals(Object other) {
			return desc.equals(((EffectBuilder)other).desc);
		}
		public int hashCode() {
			return desc.hashCode();
		}
		public String toString() {
			return (count > 1 ? (count + "x ") : "") + desc;
		}
		public void merge(EffectBuilder eb) {
			desc = this.toString() + " + " + eb.toString();
			count = 1;
			hp = eb.hp;
		}
	}
	public String generateDescription() {
		List<Integer> damage = new LinkedList<Integer>();
		List<EffectBuilder> effects = new LinkedList<EffectBuilder>();
		int stolenOverflow = 0, gainedOverflow = 0, stBRVRate = 0, maxBrvDmgLimit = 0, maxBRVHPLimit = 0;
		boolean fullAoE = false, ignoreDefense = false;
		int splash = -1, splashBroken = -1;
		for(Hit_Data hd : details.getHits()) {
			if(hd.getEffect() == null) continue;
			if(hd.getEffect().getEffect() == Hit_Data.EffectType.E107)
				fullAoE = true;
			else if(hd.getEffect().getEffect() == Hit_Data.EffectType.E44) {
				splash = hd.getArguments()[0];
				splashBroken = hd.getArguments().length >= 2 ? hd.getArguments()[1] : -1;
			}else
				continue;
			break;
		}
		for(Hit_Data hd : details.getHits()) {
			if(hd.getFakeDesc() != null) {
				effects.add(new EffectBuilder(hd.getFakeDesc()));
				continue;
			}
			if(hd.getEffect().getEffect() == null) {
				effects.add(new EffectBuilder("**Unknown Hit_Data " + hd.getId() + "**"));
				continue;
			}
			if(hd.getEffect().getEffect().getBaseDescription() != null) {
				if(hd.getEffect().getEffect().allowRepeats())
					effects.add(new EffectBuilder(hd.getEffect().getEffect().getDescription(getUnit(), getDetails(), hd), false, false, hd.getAttackType() == null ? -1 : hd.getAttackType().getId()));
				else {
					EffectBuilder bb = new EffectBuilder(hd.getEffect().getEffect().getDescription(getUnit(), getDetails(), hd), false, false, hd.getAttackType() == null ? -1 : hd.getAttackType().getId());
					if(effects.stream().noneMatch(h -> h.equals(bb)))
						effects.add(0, bb);
				}
			}
			if(hd.getEffect().getEffect().isAbilityPower()) {
				if(stolenOverflow <= 100)
					stolenOverflow = hd.getMaxBrvOverflow();
				if(maxBrvDmgLimit == 0)
					maxBrvDmgLimit = hd.getBrvDamageLimit();
				if(maxBRVHPLimit == 0)
					maxBRVHPLimit = hd.getMaxBrvLimit();
				if(stBRVRate == 0 && hd.getSingleTargetBrvRate() > 0)
					stBRVRate = hd.getSingleTargetBrvRate();
				if(hd.getType() == Hit_Data.Type.BRVIgnoreDEF)
					ignoreDefense = true;
				if(hd.getType() == Hit_Data.Type.BRV || hd.getType() == Hit_Data.Type.BRVIgnoreDEF) {
					damage.add(hd.getBrvRate());
					EffectBuilder eb = new EffectBuilder(hd.target.name() + "12345" + " BRV", true, false, hd.getAttackType().getId());
					eb.elements = hd.getElements();
					effects.add(eb);
				}
				else if(hd.getType() == Hit_Data.Type.HP || hd.getType() == Hit_Data.Type.HPSplash) {
					damage.add(-1);
					if(hd.getEffect().getEffect().getBaseDescription() == null) {
						if(hd.getTarget().equals(Hit_Data.Target.AoE))
							effects.add(new EffectBuilder(fullAoE ? "AoE HP(Full)" : "AoE HP(Split)", true, true, hd.getAttackType() == null ? -1 : hd.getAttackType().getId()));
						else
							effects.add(new EffectBuilder(splash > 0 ? "HP (" + splash + "% Splash" + (splashBroken != -1 ? ", " + splashBroken + "% if target broken" : "") + ")" : "HP", true, true, hd.getAttackType() == null ? -1 : hd.getAttackType().getId()));
					}
				}
			}else {
				if(gainedOverflow <= 100)
					gainedOverflow = hd.getMaxBrvOverflow();
			}
		}
		boolean replaceAttackType = false;
		boolean replaceElement = false;
		List<Element> elements = null;
		for(EffectBuilder eb : effects) {
			if(!eb.desc.contains("12345")) continue; //To make sure that I only look at actual BRV hits
			if(eb.attackTypeId != -1 && eb.attackTypeId != this.getDetails().getAttackType().getId()) {
				replaceAttackType = true;
			}
			if(eb.elements != null && elements == null)
				elements = eb.elements;
			if(elements != null && eb.elements != null && !eb.elements.equals(elements))
				replaceElement = true;
		}
		List<EffectBuilder> effectsFinal = new LinkedList<EffectBuilder>();
		if(gainedOverflow > 100 && gainedOverflow < 400)
			effectsFinal.add(new EffectBuilder("Gained BRV may exceed Max BRV up to " + gainedOverflow + "%"));
		if(ignoreDefense)
			effectsFinal.add(new EffectBuilder("BRV Hits ignore target's defense"));
		if(stBRVRate > 0) {
			List<Integer> damage2 = damage.stream().filter(i -> i != -1).collect(Collectors.toList());
			effectsFinal.add(new EffectBuilder("Raises BRV Damage by " + (int)((((damage2.get(0)+stBRVRate)/((float)damage2.get(0)))-1) * 100) + "% against ST"));
		}
		if(damage.size() > 0 || getId() == 8819) { //ID = Amidatelion BRV++
			if(this.getDetails().getChaseDmg() >= 50)
				effectsFinal.add(new EffectBuilder("Initiates a chase sequence (" + this.getDetails().getChaseDmg() + " [CU](https://www.reddit.com/r/DissidiaFFOO/comments/7x7ffp/chase_mechanic/))"));
			else if(this.getDetails().getChaseDmg() > 3)
				effectsFinal.add(new EffectBuilder("Easier to initiate a chase sequence (" + this.getDetails().getChaseDmg() + " [CU](https://www.reddit.com/r/DissidiaFFOO/comments/7x7ffp/chase_mechanic/))"));
			else if(this.getDetails().getChaseDmg() == 0)
				effectsFinal.add(new EffectBuilder("Cannot initiate a chase sequence"));
		}
		if(this.getDetails().getMovementCost() == 0)
			effectsFinal.add(new EffectBuilder("Instant turn rate"));
		else if(this.getDetails().getMovementCost() < 30)
			effectsFinal.add(new EffectBuilder("High turn rate"));
		else if(this.getDetails().getMovementCost() > 30)
			effectsFinal.add(new EffectBuilder("Low turn rate"));
		String last = null, potency = null; 
		int count = 1, totalPotency = 0;
		EffectBuilder prev = null;
		for(EffectBuilder eb : effects) {
			eb.desc = eb.desc.replace("12345", 
										(replaceAttackType ? 
											ImageUtils.getEmoteText("attackType_" + Hit_Data.Attack_Type.get(eb.attackTypeId)) : 
											"") + 
										(replaceElement && eb.elements != null ? 
											eb.elements.stream()
												.map(e -> ImageUtils.getEmoteText(e.getEmote()))
												.reduce("", (e1, e2) -> e1 + e2) : 
											""));
			if(prev == null) { effectsFinal.add(prev = eb); continue; }
			if(prev.equals(eb))
				prev.increase();
			else
				effectsFinal.add(prev = eb);
		}
		prev = null;
		effects = effectsFinal;
		effectsFinal = new LinkedList<EffectBuilder>();
		for(EffectBuilder eb : effects) {
			if(prev == null) { effectsFinal.add(prev = eb); continue; }
			if(prev.merge && eb.merge && !prev.hp)
				prev.merge(eb);
			else
				effectsFinal.add(prev = eb);
		}
		prev = null;
		effects = effectsFinal;
		effectsFinal = new LinkedList<EffectBuilder>();
		for(EffectBuilder eb : effects) {
			if(prev == null) { effectsFinal.add(prev = eb); continue; }
			if(prev.equals(eb) && !eb.hp)
				prev.increase();
			else
				effectsFinal.add(prev = eb);
		}
		if(getDetails().getAilments().stream().filter(a -> a != null && a.getId() != 374 && a.getCastId() != 463)
				.anyMatch(a -> a.getDuration() == 1 && a.getEffects().stream().anyMatch(e -> e.effectId == Ailment.EffectType.E44.getId())))
			effectsFinal.add(new EffectBuilder(Ailment.EffectType.E44.getBaseDescription()));
		for(Integer d : damage) {
			if(d > 0)
				totalPotency += d;
			if(last == null)
				last = ""+d;
			else if(last.equals(""+d))
				count++;
			else {
				String out = last + (count == 1 ? "%" :  "% x " + count);
				last = d.intValue() == -1 ? null : ""+d;
				count = 1;
				potency = potency == null ? out : (potency + " + " + out);
			}
		}
		if(last != null){
			String out = last + (count == 1 ? "%" :  "% x " + count);
			potency = potency == null ? out : (potency + " + " + out);
		}
		if(potency != null && totalPotency > 0) {
			if(maxBrvDmgLimit > 0)
				effectsFinal.add(new EffectBuilder("Maximum BRV damage limit +" + maxBrvDmgLimit + "% (up to " + (int)Math.floor(9999 * (1 + (maxBrvDmgLimit/100f))) + ")"));
			if(maxBRVHPLimit > 0)
				effectsFinal.add(new EffectBuilder("Maximum obtainable BRV & HP damage limit +" + maxBRVHPLimit + "% (up to " + (int)Math.floor(99999 * (1 + (maxBRVHPLimit/100f))) + ")"));
			potency = "BRV Potency: " + potency.replace(" + -1%", "").replace("-1% + ", "") + " = " + totalPotency + "%" + (stolenOverflow > 100 && stolenOverflow < 400 ? " (" + stolenOverflow + "% overflow)" : "");
			effectsFinal.add(new EffectBuilder(""));
			effectsFinal.add(new EffectBuilder(potency));
//			if(maxBrvDmgLimit > 0 || maxBRVHPLimit > 0)
//				effectsFinal.add(new EffectBuilder(""));
//			if(maxBrvDmgLimit > 0)
//				effectsFinal.add(new EffectBuilder("Maximum BRV damage limit +" + maxBrvDmgLimit + "% (up to " + (int)Math.floor(9999 * (1 + (maxBrvDmgLimit/100f))) + ")"));
//			if(maxBRVHPLimit > 0)
//				effectsFinal.add(new EffectBuilder("Maximum obtainable BRV & HP damage limit +" + maxBRVHPLimit + "% (up to " + (int)Math.floor(99999 * (1 + (maxBRVHPLimit/100f))) + ")"));
		}
		List<String> endResult = new LinkedList<String>();
		return Streams.concat(	endResult.stream(), 
								effectsFinal.stream()
									.map(s -> s.toString())
									.map(s -> (s.startsWith("HP") || s.startsWith("AoE HP") ? "Followed by an " : "") + s))
										.reduce((s1, s2) -> s1 + System.lineSeparator() + s2).orElse("");
	}
}