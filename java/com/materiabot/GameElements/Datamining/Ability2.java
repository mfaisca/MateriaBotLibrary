package com.materiabot.GameElements.Datamining;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import com.materiabot.GameElements.Element;

public class Ability2 {
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
		Melee(1, "Melee Attack"),
		Ranged(2, "Ranged Attack"),
		Magic(3, "Magic Attack"),
		MixedMeleeRanged(4, "Melee + Ranged Attack"),
		MixedMeleeMagic(5, "Melee + Magic Attack");

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
	
	public static abstract class Hit_Data {
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
		}
		
		public static enum Type{
			BRV(1), HP(2), Other(6), HPSplash(7), BRVIgnoreDEF(14), SketchSummon(15);
			
			private int id;
			
			private Type(int id) { this.id = id; }

			public int getId() {
				return id;
			}
		}
		public static enum Attack_Type{
			UnknownN1(-1), Unknown1(1), Unknown2(2), Unknown3(3);
			
			private int id;
			
			private Attack_Type(int id) { this.id = id; }
			
			public int getId() { return id; }
		}
		public static enum Target{
			ST(1, "target"),
			Self(2, "self"),
			Random(3, "random targets"), //Kuja/Lenna only
			AoE(5, "all enemies"),
			Party(6, "party"),
			Allies(7, "allies"),
			Splash(10, "splash"),
			SplitBRV(11, "split between enemies"),
			Ally(13, "ally"),
			SplitHP(18, "split between enemies"), //Cid/Prompto Only
			Traps(18, "traps???"); //Emperor only(S2 / EX)
			
			private int id;
			private String desc;
			
			private Target(int id, String desc) {this.id = id; this.desc = desc; }

			public int getId() {
				return id;
			}
			public String getDesc() {
				return desc;
			}
		}
		public static enum EffectType{ //EffectValueType irrelevant if not mentioned
			EN1(-1, "None"),//Exclusive to Yuri
			E1(1, "None"), 
			E7(7, "Removes {0} debuffs from {t}"),//(#ofRemovedDebuffs[, ?]) First can be -1 for all
			E8(8, "Removes {0} buffs from {t}"),//(#ofRemovedBuffs[, ?, ?])  First can be -1 for all
			E25(25, "Guaranteed hit"),
			E33(33, "Swap turns with selected ally"),//(-1) Argument unknown
			E34(34, "Angel Wing"),//([1]) No value = remove, Value = give
			E37(37, "Cancel buff"),//(ID of buff)
			E38(38, "Recover Abilities"), //(1) or (-1, -1) - First is both, second is only skill1
			E41(41, "Battery"),     //(Potency[[, ?], ?, ?]) - EffectValueType says what stat its based on
									//For effect_value_type = 46, there's 4 potencies and one is picked at random
			E42(42, "Gravity"),//(% of shave)
			E43(43, "Heal"), //(Potency[, ?]) - EffectValueType says what stat its based on
			E44(44, "HP Splash Damage"), //(% of splash)
			E46(46, "Turn Delay"), //(# of turns)
			E48(48, "Steal Buffs"), //(# of buffs, ?(-1), success%, ?(-1)) OR (# of buffs, success%, ?(-1))
			E50(50, ""), //Yuri Only
			E51(51, "Transfer debuffs"), //(Duration extended, ?, ?, ?) OR (?, ?, ?) - It always transfers to all, even though target is 1
			E52(52, "Reduce HP by current HP%"), //(%, ?) - Cecil, Rinoa, Yuri
			E54(54, "Revive"), //(% of effectValueType, ?)
			E55(55, ""), //TODO (100) - Balthier Great Aim and Yuffie I dont need this, but unknown what it refers to
			E57(57, "Insta Break"), //(success%)
			E58(58, "Random Damage"), //(minPower, maxPower, ?(3), ?(-1)) - Shadow Exclusive - minPower and maxPower are in tens(4, 6 = 40%, 60%)
			E61(61, "Battery for Target current BRV"), //(copy%)
			E65(65, "HP Heal based on damage dealt"), //(Potency, MaxHP%Healed) EffectValueType = What damage it is based on
			E69(62, ""), //(X) || Unique to Ignis Regroup - Unknown Effect, others are accounted for
			E70(70, "Battery"), //EffectValueType 2 = Copy Target BRV || Otherwise table
			E72(72, "Lower turn rate when breaking or hitting broken target"), //(New Cost[, ?(-1)]))
			E73(73, "100% Accuracy BRV & 50% Bonus DMG if target not targetting self"), //([?]) - No params - Fucking Lion
			E78(78, "BRV Hits apply a stacking IBRV debuff"), //Lenna Rapid Fire mechanic
			E80(80, "Copy random buff and extend its duration by 1"), //(?, ?, ?, ?) - Yuffie Snatch
			E81(81, "Stronger BRV Hits when targetted"), //(Base Multiplier(100), Targetted Multipler(300), ?, ?, ?) - Zack
			E84(84, ""), //Old Vanille Data
			E89(89, "Extends existing buff by X"), //(# of stacks to increase, buffID)
			E90(90, "Moves own next turn to just before target's next turn"),
			E93(93, "Adds an extra hit if [Royal Arms] is up"), //([-1]) Noctis unique hit (30/60/80/100/120)
			E94(94, "Increases damage based on party's [Shield] value"), //(10, -1) Unknown how to formulate it
			E97(97, "BRV Damage boosted up to X based on how much HP you're missing"), //(Potency, -1) Terra EX
			E99(99, "HP Heal based on valueType"), //(Potency[, MaxHP%Healed, ?]) EffectValueType = What damage it is based on
			E100(100, "HP Heal based on valueType, XXX% heal in excess goes to BRV"), //(Potency, MaxHP%Healed, XXX100, ?, ?) XXX = 100(%) / 300(%)
			E102(102, ""),		//Cait Sith Only
			E103(103, ""),		//Cait Sith Only	//I have no fucking idea how the arguments work
			E104(104, ""),  	//Cait Sith Only  //EffectValueType
			E105(105, ""),  	//Cait Sith Only  //EffectValueType
			E106(106, "BRV with Overflow"), //(?, -1) - Mentions overflow through an argument instead of the regular field, older model perhaps?
			E107(107, "100% HP Damage"),
			E110(110, "Free Turn"),
			E111(111, ""), //Old Data? Barret Counter
			E113(113, "Extends self-buffs by X turns"), //Prishe Only? (X, 1, -1)
			E114(114, ""), //TODO Fang EX (50, -1)
			E115(115, "Increase Damage by X% against ST"), //(X, -1)
			E116(116, ""), //TODO
			E117(117, "Increase Damage by X% against ST"), //(X)
			E120(120, "Increase Damage by X% against target with Turn Rate Down or SPD Down"), //(1, X)
			E121(121, "Extends [buff] by X turns || Extends own buffs by X turns"), //(X, buffId) || (X, 2, -1)
			E124(124, ""), //EffectValueType = 14 || Leon only || Could this be his unique debuff??
			E125(125, "Unbreak target"),
			E126(126, "Heal party by X%, allows overhealing up to Y%"), //Porom only (X, Y, ?)
			E128(128, ""), //Alphinaud Only - Something related to his summon?
			E129(129, "Release pet when broken"), //Alphinaud Only
			E131(131, "Raise party's BRV by X% of the party's highest current BRV"), //(X) || EffectValueType = 21 ||| Setzer Only
			E132(132, ""), //(X) || Unique to Ignis Regroup - Unknown Effect, others are accounted for
			E135(135, "Increased BRV damage by X% when dealing critical hits"),
			E136(136, "Recover skill uses "), //(#ofUses, 100, skillID)
			E137(137, "AOE HP Attacks based on party members current BRV"), //(100) / (1, 100) || Sherlotta Only
			E139(139, "X turn delay if buff Y is active"), //(X, Y) || Garland Only
			E140(140, ""), //Prompto Only - His AA
			E141(141, "Reduces Chakra by #"), //Lyse Only  (# of stacks to lose(negative), buff_id) - buff_id doesnt exist for some reason
			E147(147, ""), //Ignis EX Only - Dead skill
			E154(154, "Reduces all enemies BRV to 0"), //Yuri Only
			E156(156, "Battery self for {0}% of stat(effectValueType), lower (target) BRV by {0}% of stat(effectValueType)"), //ExDeath only - Stat is always 16(Attack)
			E157(157, "Battery party for {0}% of stat(effectValueType), lower (target) BRV by {0}% of stat(effectValueType)"), //ExDeath only - Stat is always 16(Attack)
			;

			private int id;
			private String baseDescription;
			
			private EffectType(int id, String desc) { this.id = id; baseDescription = desc; }

			public int getId() { return id; }
			public String getBaseDescription() { return baseDescription; }

			public String getDescription(Integer... values) {
				return getDescription(Arrays.stream(values).map(i -> i.toString()).collect(Collectors.toList()).toArray(new String[0]));
			}
			public String getDescription(String... values) {
				values = fix(values);
				String r = baseDescription;
				for(int i = 0; i < values.length; i++)
					r = r.replace("{" + i + "}", values[i]);
				return r;
			}
			private String[] fix(String[] v) {
				if(id == 7 || id == 8)
					if(v[0].equals("-1")) v[0] = "all";
				return v;
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
		private int[] arguments;
		private Attack_Type attackType;
		private Target target;
		private Element element;
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
		public int[] getArguments() {
			return arguments;
		}
		public void setArguments(int[] arguments) {
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
		public Element getElement() {
			return element;
		}
		public void setElement(Element element) {
			this.element = element;
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
	
	private int id;
	private String name;
	private String description;
	private int movementCost;
	private int useCount;
	private Type type;
	private Attack_Type attackType;
	private Command_Type commandType;
	private Target_Type targetType;
	private int chaseDmg; //can_initiate_chase * chase_dmg
	private List<Hit_Data> hits = new LinkedList<Hit_Data>();
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getMovementCost() {
		return movementCost;
	}
	public void setMovementCost(int movementCost) {
		this.movementCost = movementCost;
	}
	public int getUseCount() {
		return useCount;
	}
	public void setUseCount(int useCount) {
		this.useCount = useCount;
	}
	public Type getType() {
		return type;
	}
	public void setType(Type type) {
		this.type = type;
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
	public void setHits(List<Hit_Data> hits) {
		this.hits = hits;
	}
}