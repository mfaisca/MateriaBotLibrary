package com.materiabot.GameElements;
import com.google.common.collect.Streams;
import com.materiabot.GameElements.Ability.Details.Hit_Data;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Ability {
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
			RangedMelee(4, "Melee + Ranged Attack"),
			MagicMelee(5, "Melee + Magic Attack"),
			MagicRanged(-1, "Ranged + Magic Attack"),
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
			Party2(7, "Party?");

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
		
		public static class Hit_Data {
			public static enum BasedOnStat{
				Stat1(1, "???"),
				Stat2(2, "Attack"),
				Stat3(3, "Initial BRV"),
				Stat4(4, "Total Party Max BRV"), //Unique to Steiner
				Stat6(6, "Max HP"),
				Stat7(7, "Current BRV"),
				Stat8(8, "Initial BRV"),
				Stat9(9, "Max BRV"),
				Stat11(11, "Max HP"),
				Stat12(12, "Target Current BRV"), //Locke unused skills?
				Stat13(13, "BRV Damage Dealt"),
				Stat14(14, "HP Damage Dealt"),
				Stat15(15, "Attack"),
				Stat16(16, "Attack"),
				Stat29(29, "Unknown"), //Jecht unused skill?
				Stat36(36, "HP Damage Dealt"), //Serah EX only?
				Stat37(37, "Total Party Current HP"), //Unique to Ignis?
				Stat46(46, "Attack"), //Wakka EX only, Random between 4 values on arguments
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
				UnknownN1(-1), Unknown1(1), Unknown2(2), Unknown3(3);
				
				private int id;
				
				private Attack_Type(int id) { this.id = id; }
				
				public int getId() { return id; }
				public static Attack_Type get(int id) {
					return Arrays.asList(values()).stream().filter(t -> t.getId() == id).findFirst().orElse(null);
				}
			}
			public static enum Target{
				ST(1, "target"),
				Self(2, "self"),
				Random(3, "random targets"), //Kuja/Lenna only
				AoE(5, "all enemies"),
				Party(6, "party"),
				Allies(7, "allies"),
				Splash(10, "splash"),
				Split(11, "split between enemies"),
				Ally(13, "ally"),
				SplitHP(18, "split between enemies"), //Cid/Prompto Only
				Traps(18, "traps???"), //Emperor only(S2 / EX)
				Caller(29, "caller"),
				
				;private int id;
				private String desc;
				
				private Target(int id, String desc) {this.id = id; this.desc = desc; }

				public int getId() {
					return id;
				}
				public String getDesc() {
					return desc;
				}
				public static Target get(int id) {
					return Arrays.asList(values()).stream().filter(t -> t.getId() == id).findFirst().orElse(null);
				}
			}
			public static enum EffectType{ //EffectValueType irrelevant if not mentioned
				EN1(-1, null, true),//Basic Hits - Exclusive to Yuri
				E1(1, null, true), //Basic Hits
				E7(7, "Removes {0} debuffs from {t}"),//(#ofRemovedDebuffs[, ?]) First can be -1 for all
				E8(8, "Removes {0} buffs from {t}"),//(#ofRemovedBuffs[, ?, ?])  First can be -1 for all
				E25(25, "BRV Hits are guaranted hits"),
				E33(33, "Give turn to selected ally"),//(-1) Argument unknown
				E34(34, "{0}"),//Angel Wing Unique Buff - ([1]) No value = remove, Value = give
				E37(37, "Dispels 「**{0}**」"),//(ID of buff)
				E38(38, "Recover {0} of {2}"), //(1) or (-1, -1) - First is both, second is only skill1
				E41(41, "Raises {t} BRV by {0}% of {evt}"), //Battery - (Potency[[, ?], ?, ?]) - EffectValueType says what stat its based on
															//For effect_value_type = 46, there's 4 potencies and one is picked at random
				E42(42, "{0}% Gravity on {t}"),//(% of shave)
				E43(43, "Recover {t} HP by {0}% of {evt}"), //(Potency[, ?]) - EffectValueType says what stat its based on
				E44(44, "{0}% HP Splash Damage", true), //HP Splash Damage - (% of splash)
				E46(46, "Delays {t} by {0}"), //(# of turns)
				E48(48, "{1}% chance to steal {0} buffs from {t}"), //(# of buffs, ?(-1), success%, ?(-1)) OR (# of buffs, success%, ?(-1)) OR (# of buffs, success%, ?(-1))
				E50(50, null), //Yuri Only
				E51(51, "Transfer debuffs"), //(Duration extended, ?, ?, ?) OR (?, ?, ?) - It always transfers to all, even though target is 1
				E52(52, "Reduce {t} HP by {0}%"), //(%, ?) - Cecil, Rinoa, Yuri
				E54(54, "Revive"), //(% of effectValueType, ?)
				E55(55, ""), //TODO (100) - Balthier Great Aim and Yuffie I dont need this, but unknown what it refers to
				E57(57, "{0}% chance to Break {t}"), //(success%)
				E58(58, "BRV hits have a random potency between {0}% and {1}%", true, false), //(minPower, maxPower, ?(3), ?(-1)) - Shadow Exclusive - minPower and maxPower are in tens(4, 6 = 40%, 60%)
				E61(61, "Battery for Target current BRV"), //(copy%)
				E65(65, "HP Heal based on damage dealt"), //(Potency, MaxHP%Healed) EffectValueType = What damage it is based on
				E69(62, null), //(X) || Unique to Ignis Regroup - Unknown Effect, others are accounted for
				E70(70, "Battery"), //EffectValueType 2 = Copy Target BRV || Otherwise table
				E72(72, "Lower turn rate when breaking or hitting broken target"), //(New Cost[, ?(-1)]))
				E73(73, "100% Accuracy BRV & 50% Bonus DMG if target not targetting self"), //([?]) - No params - Fucking Lion
				E78(78, "BRV Hits apply a stacking IBRV debuff"), //Lenna Rapid Fire mechanic
				E80(80, "Copy random buff and extend its duration by 1"), //(?, ?, ?, ?) - Yuffie Snatch
				E81(81, "Stronger BRV Hits when targetted"), //(Base Multiplier(100), Targetted Multipler(300), ?, ?, ?) - Zack
				E84(84, null), //Old Vanille Data
				E89(89, "Increases 「**{1}**」 stacks by {0}"), //(# of stacks to increase, buffID)
				E90(90, "Moves own next turn to just before target's next turn"),
				E93(93, "Adds an extra hit if [Royal Arms] is up"), //([-1]) Noctis unique hit (30/60/80/100/120)
				E94(94, "Increases damage based on party's [Shield] value"), //(10, -1) Unknown how to formulate it
				E97(97, "BRV Damage boosted up to X based on how much HP you're missing"), //(Potency, -1) Terra EX
				E99(99, "HP Heal based on valueType"), //(Potency[, MaxHP%Healed, ?]) EffectValueType = What damage it is based on
				//HP Heal based on valueType, XXX% heal in excess goes to BRV
				E100(100, "Restores {t} HP by {0}% of {evt}, up to {1}% Max HP" + System.lineSeparator() + 
						  "{2}% of excess healing is converted to BRV"), //(Potency, MaxHP%Healed, XXX100, ?, ?) XXX = 100(%) / 300(%)
				E102(102, ""),		//Cait Sith Only
				E103(103, ""),		//Cait Sith Only	//I have no fucking idea how the arguments work
				E104(104, ""),  	//Cait Sith Only  //EffectValueType
				E105(105, ""),  	//Cait Sith Only  //EffectValueType
				E106(106, null, true), //(Overflow%) - Mentions overflow through an argument instead of the regular field, older model perhaps?
				E107(107, "100% AoE HP Damage", true),
				E110(110, "Free Turn"),
				E111(111, null), //Old Data? Barret Counter
				E113(113, "Extends self-buffs by X turns"), //Prishe Only? (X, 1, -1)
				E114(114, ""), //TODO Fang EX (50, -1)
				E115(115, "Increase Damage by X% against ST"), //(X, -1)
				E116(116, ""), //TODO
				E117(117, "Raises BRV Damage by {0}% against Broken Targets", true, false), //(X)
				E120(120, "Increase Damage by X% against target with Turn Rate Down or SPD Down"), //(1, X)
				E121(121, "{0}"), //(X, buffId) || (X, 2, -1)
				E122(122, "Raises BRV by X% of Y"), //(X) || effectvaluetype = stat its based on
				E124(124, ""), //EffectValueType = 14 || Leon only || Could this be his unique debuff??
				E125(125, "Unbreak target"),
				E126(126, "Heal party by X%, allows overhealing up to Y%"), //Porom only (X, Y, ?)
				E128(128, ""), //Alphinaud Only - Something related to his summon?
				E129(129, "Release pet when broken"), //Alphinaud Only
				E131(131, "Raise party's BRV by X% of the party's highest current BRV"), //(X) || EffectValueType = 21 ||| Setzer Only
				E132(132, null), //(X) || Unique to Ignis Regroup - Unknown Effect, others are accounted for
				E135(135, "Increased BRV damage by X% when dealing critical hits"),
				E136(136, "Recover {0} of {2}"), //(#ofUses, 100, skillID)
				E137(137, "AOE HP Attacks based on party members current BRV"), //(100) / (1, 100) || Sherlotta Only
				E139(139, "X turn delay if buff Y is active"), //(X, Y) || Garland Only
				E140(140, ""), //Prompto Only - His AA
				E141(141, "Reduces Chakra by #"), //Lyse Only  (# of stacks to lose(negative), buff_id) - buff_id doesnt exist for some reason
				E142(142, "Reduce target's BRV by X% based on own Y"), //(X) - Y = effect_value_type = based on stat X
				E147(147, null), //Ignis EX Only - Dead skill
				E154(154, "Reduces all enemies BRV to 0"), //Yuri Only
				E156(156, "Battery self for {0}% of stat(effectValueType), lower (target) BRV by {0}% of stat(effectValueType)"), //ExDeath only - Stat is always 16(Attack)
				E157(157, "Battery party for {0}% of stat(effectValueType), lower (target) BRV by {0}% of stat(effectValueType)"), //ExDeath only - Stat is always 16(Attack)
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
					for(int i = 0; i < values.length; i++)
						r = r.replace("{" + i + "}", ret.values[i]);
					r = r.replace("{t}", ret.target);
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
							else
								v[0] = "Dispels 「**Angel Wing**」";
							break;
						case 37:{ //Dispels 「**{0}**」 - (ID of buff)
							Ailment ail = u.getSpecificAilment(Integer.parseInt(v[0]));
							v[0] = v[0].toString().equalsIgnoreCase("-1") ? "all" : ((ail != null ? ail.getName() : "Unknown Ailment ID: " + v[0]));
							break; }
						case 38: //Recover {0} of {2} - (-1) = S1/AA all uses | (1) = S1/S2 1 use
							ret.values = new String[3];
							for(int i = 0; i < ret.values.length; i++)
								ret.values[i] = i < v.length ? v[i] : null;
							v = ret.values;
							if(v[0].equals("-1")) //King S2
								v[0] = "all uses"; v[2] = "「**" + u.getBaseAbility(Ability.Type.S1) + "**」, 「**" + u.getBaseAbility(Ability.Type.AA) + "**」";
							if(v[0].equals("1")) //Selphie AA
								v[0] = "1 use"; v[2] = "「**" + u.getBaseAbility(Ability.Type.S1) + "**」, 「**" + u.getBaseAbility(Ability.Type.S2) + "**」";
							break;
						case 41: //Doesn't have break ON PURPOSE, Wakka effect that makes a random 1/4 potency, then goes to BasedByStat
							if(h.getEffect().getEffectValueType() == 46)
								v[0] = v[Shared.Methods.RNG.nextInt(4)];
						case 43:
							ret.effectValueType = BasedOnStat.get(h.getEffect().getEffectValueType()).getStat();
							break;
						case 46:
							v[0] = (v[0].equals("1") ? "1 turn" : v[0] + " turns");
							break;
						case 48:
							v[1] = v.length == 3 ? v[2] : v[1];
							break;
						case 58:
							v[0] = v[0] + "0";
							v[1] = v[1] + "0";
							int fixBrvRate = (Integer.parseInt(v[1]) + Integer.parseInt(v[0]))/2;
							d.hits.forEach(dh -> dh.brvRate = fixBrvRate);
							break;
						case 89:{ //Increases 「**{1}**」 stacks by {0} - (# of stacks to increase, buffID)
							Ailment ail = u.getSpecificAilment(Integer.parseInt(v[1]));
							v[1] = v[1].toString().equalsIgnoreCase("-1") ? "all" : ((ail != null ? ail.getName() : "Unknown Ailment ID: " + v[1]));
							if(!d.getAilments().contains(ail))
								d.getAilments().add(ail);
							break; }
						case 100: //(Potency, MaxHP%Healed, XXX100, ?, ?) XXX = 100(%) / 300(%)
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
							Ability ab = u.getSpecificAbility(Integer.parseInt(v[2]));
							v[0] = v[0].equals("-1") ? "all uses" : (v[0].equals("1") ? "1 use" : (v[0] + " uses"));
							v[2] = v[2].equals("-1") ? "both abilities" : (ab != null ? ab.getName() : ("Unknown Skill ID: " + v[2]));
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
				public Effect(EffectType effect, int effectValueType, int brvRate) {
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
			private Integer[] arguments;
			private Attack_Type attackType;
			private Target target;
			private List<Element> elements = new LinkedList<Element>();
			private Effect effect;
			private int brvRate;
			private int maxBrvOverflow = 100;
			private int maxBrvOverflowOnBreak = 0;
			private int singleTargetBrvRate = 0;
			
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
				return elements;
			}
			public Effect getEffect() {
				return effect;
			}
			public void setEffect(Effect effect) {
				this.effect = effect;
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
		LD("ld"), BT("bt"), CA("ca", "call");
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
	private Details details;

	public int getId() { return id; }
	public void setId(int id) { this.id = id; }
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	public String getDescription() { return description; }
	public void setDescription(String description) { this.description = description; }
	public int getUseCount() { 
		return unit.getBaseAbility(type).get(0).useCount + Streams.concat(
								unit.getEquipment().stream().flatMap(e -> e.getPassives().stream()),
								unit.getPassives().values().stream())
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
	
	public String generateDescription() {
		List<Integer> damage = new LinkedList<Integer>();
		List<String> effects2 = new LinkedList<String>();
		List<Integer> usedEffects = new LinkedList<Integer>();
		int stolenOverflow = 0, gainedOverflow = 0;
		for(Hit_Data hd : details.getHits()) {
			if(hd.getEffect().getEffect() == null) {
				effects2.add("**Unknown Hit_Data " + hd.getId() + "**");
				continue;
			}
			if(hd.getEffect().getEffect().isAbilityPower()) {
				if(stolenOverflow <= 100)
					stolenOverflow = hd.getMaxBrvOverflow();
				
				if(hd.getType() == Hit_Data.Type.BRV || hd.getType() == Hit_Data.Type.BRVIgnoreDEF) {
					damage.add(hd.getBrvRate());
					effects2.add(hd.target.name() + " BRV123");
				}
				else if(hd.getType() == Hit_Data.Type.HP || hd.getType() == Hit_Data.Type.HPSplash) {
					damage.add(-1);
					if(hd.getEffect().getEffect().getBaseDescription() == null)
						effects2.add("Followed by an HP Attack");
				}
			}else {
				if(gainedOverflow <= 100)
					gainedOverflow = hd.getMaxBrvOverflow();
			}
			if(hd.getEffect().getEffect().getBaseDescription() == null) continue;
			if(hd.getEffect().getEffect().allowRepeats())
				effects2.add(hd.getEffect().getEffect().getDescription(getUnit(), getDetails(), hd));
			else {
				if(usedEffects.stream().noneMatch(h -> h.equals(hd.getEffect().getEffect().getId())))
					effects2.add(hd.getEffect().getEffect().getDescription(getUnit(), getDetails(), hd));
				usedEffects.add(hd.getEffect().getEffect().getId());
			}
		}
		int totalPotency = 0;
		String potency = null;
		String last = null, build = null, bigLast = null; int count = 1, bigCount = 1;
		List<String> effects = new LinkedList<String>();
		if(gainedOverflow > 100)
			effects.add("Gained BRV may exceed Max BRV up to " + gainedOverflow + "%");
		for(String eff: effects2) {
			if(eff.contains("BRV123") || eff.equals("Followed by an HP Attack")) {
				if(last == null)
					last = eff;
				else if(last.equals(eff))
					count++;
				else {
					eff = (count == 1 ? "" :  (count + " ")) + last.replace("123", "") + " + " + eff.replace("123", "").replace("Followed by an HP Attack", "HP");
					last = null;
					count = 1;
					if(eff.equals(bigLast))
						bigCount++;
					else
						build = (build == null ? "" : build + " + ") + eff;
					bigLast = eff;
				}
			}
			else {
				if(bigCount > 1) {
					effects.remove(bigLast);
					effects.add("(" + bigLast + ") x " + bigCount);
				}
				else if(build != null)
					effects.add(build);
				effects.add(eff);
				bigCount = 1;
				bigLast = null;
				build = null;
			}
		}
		if(last != null) {
			if(build != null)
				build += " + " + last.replace("Followed by an HP Attack", "HP");
			else
				effects.add((count == 1 ? "" :  (count + " ")) + last.replace("123", ""));
		}	
		if(build != null)
			effects.add(build);
		if(bigCount > 1) {
			effects.remove(bigLast);
			effects.add("(" + bigLast + ") x " + bigCount);
			bigCount = 1;
			bigLast = null;
		}
		last = null; count = 1;
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
			potency = "BRV Potency: " + potency.replace(" + -1%", "") + " = " + totalPotency + "%" + (stolenOverflow > 100 ? " (" + stolenOverflow + "% overflow)" : "");
			effects.add("");
			effects.add(potency);
		}
		return effects.stream().reduce((s1, s2) -> s1 + System.lineSeparator() + s2).orElse("");
	}
}