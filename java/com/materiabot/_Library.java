package com.materiabot;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.materiabot.GameElements.Element;
import com.materiabot.GameElements.Summon;
import com.materiabot.GameElements.Unit;
import com.materiabot.GameElements.Summon.SummonPassive;
import com.materiabot.IO.JSON.UnitParser;

public class _Library {
	public static final _Library GL = new _Library("gl");
	public static final _Library JP = new _Library("jp");
	public static _Library get(String r) { return r.equalsIgnoreCase("JP") ? JP : GL; }	

	private _Library(String r) {
		name = r;
		UNIT_CACHE = CacheBuilder.newBuilder()
				.expireAfterAccess(15, TimeUnit.MINUTES).build(new CacheLoader<String, Unit>(){
					@Override
					public Unit load(String key) throws Exception {
						return new UnitParser(r).parseUnit(key);
					}
				});
	}
	/////////////////////////////////////////////////////////////////////////////////////////////////////
	public LoadingCache<String, Unit> UNIT_CACHE;
	private String name;
	public static List<Summon> SUMMON_LIST = new LinkedList<Summon>();

	static { //Temporary???
			SUMMON_LIST.add(new Summon(Arrays.asList("Chocobo", "Choco"), 20, null, "Chocobo Kick", "Raises party's HP by 250", 
					"1500 Melee BRV Physical damage" + System.lineSeparator() + "Raises ATK of all party members by 10%", 5, 2000, "Fast"));
			SUMMON_LIST.add(new Summon(Arrays.asList("Sylph"), 20, Element.Wind, "Whispering Wind", "Raises Debuff Evasion Rate by 30% when BRV >= Initial BRV", 
					"1200 BRV and HP recovery" + System.lineSeparator() + "Grants Wind attribute to the party", 5, 2000, "Fast"));
			SUMMON_LIST.add(new Summon(Arrays.asList("Ifrit"), 30, Element.Fire, "Hellfire", "Raises Attack by 25% when HP > 80%", 
					"4000 Fire BRV Magical damage" + System.lineSeparator() + "Grants Fire attribute to the party", 
					"10|8|6|14|12", "Vanille|Snow|Cater|Terra|Celes|Balthier", 6, 6000, "Moderately Fast", 
					new SummonPassive("Fire Resist Up", "Lowers Fire damage taken by 10%.", "Fire dmg taken -10%"),
					new SummonPassive("Fire Resist All", "Lowers Party Fire damage taken by 3%.", "Fire party dmg taken -3%"), 
					new SummonPassive("Fire Power Up", "Raises Fire BRV damage dealt by 10%.", "Fire dmg dealt +10%"),
					new SummonPassive("Ifrit Attack Up", "Raises Attack by 10% when HP > 80%.", "ATK+10% if HP>80%", true), 
					new SummonPassive("Ifrit Base&Attack Up", "Raises Initial BRV by 10% and Attack by 2% when HP > 80%.", "IBrv+10% & ATK+2% if HP>80%", true), 
					new SummonPassive("Ifrit Bonus Up", "Raises Break Bonus by 5% when HP > 80%.", "Break Bonus +5% if HP>80%"), 
					new SummonPassive("Ifrit Critical Power Up", "Raises Critical BRV damage by 10% when HP > 80%.", "Crit dmg dealt +10% if HP>80%", true)));
			SUMMON_LIST.add(new Summon(Arrays.asList("Shiva"), 30, Element.Ice, "Diamond Dust", "Raises Attack by 15%" + System.lineSeparator() + "Raises Speed by 20% when BRV < Initial BRV.", 
					"4000 Ice BRV Magical damage" + System.lineSeparator() + "Grants Ice attribute to the party", 
					"14|12|8|10|6", "Serah|Seifer|Thancred|Steiner|Auron|Prishe", 6, 6000, "Moderately Fast", 
					new SummonPassive("Ice Resist Up", "Lowers Ice damage taken by 10%.", "Ice dmg taken -10%"),
					new SummonPassive("Ice Resist All", "Lowers Party Ice damage taken by 3%.", "Ice party dmg taken -3%"), 
					new SummonPassive("Ice Power Up", "Raises Ice BRV damage dealt by 10%.", "Ice dmg dealt +10%"),
					new SummonPassive("Shiva Evasion Up", "Raises Evasion Rate by 10% when HP < 80%.", "Evade+10% if HP<80%", true), 
					new SummonPassive("Shiva Base&Guard Up", "Raises Initial BRV by 10% and Defense by 20% when HP < 80%.", "IBrv+10% & DEF+20% if HP<80%", true), 
					new SummonPassive("Shiva Bonus Up", "Raises Break Bonus by 5% when HP < 80%.", "Break Bonus +5% if HP<80%"), 
					new SummonPassive("Shiva Critical Power Up", "Raises Critical BRV damage by 10% when HP < 80%.", "Crit dmg dealt +10% if HP<80%", true)));
			SUMMON_LIST.add(new Summon(Arrays.asList("Ramuh"), 30, Element.Lightning, "Judgement Bolt", "Raises Max BRV by 15%" + System.lineSeparator() + "Raises Defense by 40% when HP < 80%", 
					"4000 Lightning BRV Magical damage" + System.lineSeparator() + "Grants Lightning attribute to the party", 
					"8|6|10|12|14", "Setzer|Onion Knight|Lenna|Maria|Cid|Eiko", 6, 6000, "Moderately Fast", 
					new SummonPassive("Lightning Resist Up", "Lowers Lightning damage taken by 10%.", "Lightning dmg taken -10%"),
					new SummonPassive("Lightning Resist All", "Lowers Party Lightning damage taken by 3%.", "Lightning party dmg taken -3%"), 
					new SummonPassive("Lightning Power Up", "Raises Lightning BRV damage dealt by 10%.", "Lightning dmg dealt +10%"),
					new SummonPassive("Ramuh Evasion Up", "Raises Evasion Rate by 10% when HP > 80%.", "Evade+10% if HP>80%", true), 
					new SummonPassive("Ramuh Base&Guard Up", "Raises Initial BRV by 10% and Defense by 20% when HP > 80%.", "IBrv+10% & DEF+20% if HP>80%", true), 
					new SummonPassive("Ramuh Chase BRV Damage Up", "Raises BRV damage dealt during a Chase Sequence by 10% when HP > 80%.", "Chase BRV dmg dealt +10% if HP>80%"), 
					new SummonPassive("Ramuh Resist Up", "Lowers BRV damage taken by 5% when HP > 80%.", "BRV dmg taken -5% if HP>80%", true)));
			SUMMON_LIST.add(new Summon(Arrays.asList("Leviathan", "Levi"), 30, Element.Water, "Tidal Wave", "Lowers enemy Speed by 10%" + System.lineSeparator() + "Grants 60% Initial BRV Regen when HP < 80%", 
					"4000 Water BRV Magical damage" + System.lineSeparator() + "Grants Water attribute to the party", 
					"6|14|12|8|10", "Krile|Laguna|Zell|Kefka|Tifa|Shantotto", 6, 6000, "Moderately Fast", 
					new SummonPassive("Water Resist Up", "Lowers Water damage taken by 10%.", "Water dmg taken -10%"),
					new SummonPassive("Water Resist All", "Lowers Party Water damage taken by 3%.", "Water party dmg taken -3%"), 
					new SummonPassive("Water Power Up", "Raises Water BRV damage dealt by 10%.", "Water dmg dealt +10%"),
					new SummonPassive("Leviathan Boost Up", "Raises Max BRV by 10% when HP < 80%.", "MBrv+10% if HP<80%", true), 
					new SummonPassive("Leviathan Base&Attack Up", "Raises Initial BRV by 10% and Attack by 2% when HP < 80%.", "IBrv+10% & ATK+2% if HP<80%", true), 
					new SummonPassive("Leviathan Life Up", "Raises Max HP by 10%.", "HP+10%", true), 
					new SummonPassive("Leviathan Resist Up", "Lowers BRV damage taken by 5% when HP < 80%.", "BRV dmg taken -5% if HP<80%")));
			SUMMON_LIST.add(new Summon(Arrays.asList("Brothers"), 30, Element.Earth, "Brotherly Love", "Raises Max BRV by 25% when HP > 50%", 
					"4000 Earth BRV Physical damage" + System.lineSeparator() + "Grants Earth attribute to the party", 
					"12|10|14|6|8", "Yuffie|Bartz|Galuf|Zidane|Paine|Shadow", 6, 6000, "Moderately Fast", 
					new SummonPassive("Earth Resist Up", "Lowers Earth damage taken by 10%.", "Earth dmg taken -10%"),
					new SummonPassive("Earth Resist All", "Lowers Party Earth damage taken by 3%.", "Earth party dmg taken -3%"), 
					new SummonPassive("Earth Power Up", "Raises Earth BRV damage dealt by 10%.", "Earth dmg dealt +10%"),
					new SummonPassive("Brothers Boost Up", "Raises Max BRV by 5% when HP > 60% Max HP", "MBrv+5% if HP>60%", true), 
					new SummonPassive("Brothers Boost Guard Up", "Raises Max BRV by 2% and Defense by 10% when HP > 60% Max HP", "MBrv+2% & DEF+10% if HP>60%", true), 
					new SummonPassive("Brothers Receive Heal Up", "Raises HP recovered by 5% when BRV > 50% Max BRV", "HP heal received +5% if BRV>50%"), 
					new SummonPassive("Brothers Protect Up", "Lowers HP damage taken by 5% when BRV > 50% Max BRV", "HP dmg taken -5% if BRV>50%", true)));
			SUMMON_LIST.add(new Summon(Arrays.asList("Pandemonium", "Pandemona", "Pande", "Panda"), 30, Element.Wind, "Tornado Zone", "Raises HP and BRV damage by 25% during a Chase Sequence", 
					"4000 Wind BRV Magical damage" + System.lineSeparator() +"Grants Wind attribute and faster Chase to the party", 
					"10|14|6|8|12", "Edge|Penelo|Y'shtola|Vivi|Freya|Lion", 6, 6000, "Moderately Fast",  
					new SummonPassive("Wind Resist Up", "Lowers Wind damage taken by 10%.", "Wind dmg taken -10%"),
					new SummonPassive("Wind Resist All", "Lowers Party Wind damage taken by 3%.", "Wind party dmg taken -3%"), 
					new SummonPassive("Wind Power Up", "Raises Wind BRV damage dealt by 10%.", "Wind dmg dealt +10%"),
					new SummonPassive("Pandemonium Attack Up", "Raises Attack by 10% when HP < 80% Max HP", "ATK+10% if HP<80%", true), 
					new SummonPassive("Pandemonium Boost Guard Up", "Raises Max BRV by 2% and Defense by 20% when HP < 80% Max HP", "MBrv+2% & DEF+20% if HP<80%", true), 
					new SummonPassive("Pandemonium Chase HP Damage Up", "Raises HP damage dealt during a Chase Sequence by 5%", "Chase HP dmg dealt +5%", true), 
					new SummonPassive("Pandemonium Chase BRV Damage Up", "Raises BRV damage dealt during a Chase Sequence by 20% when HP < 80%.", "Chase BRV dmg dealt +20% if HP<80%")));
			SUMMON_LIST.add(new Summon(Arrays.asList("Alexander", "Alex"), 20, Element.Holy, "Divine Judgment", "Raises Max HP by 15%" + System.lineSeparator() + "Grants 15% HP Regen for 10 turns when HP < 60%",
					"4000 Holy BRV Magical damage" + System.lineSeparator() + "Grants Holy attribute to the party", 
					"14|8|12|10|6", "Sazh|Raijin|Rosa|Tidus|Ashe|Firion", 6, 6000, "Moderately Fast",  
					new SummonPassive("Holy Resist Up", "Lowers Holy damage taken by 10%.", "Holy dmg taken -10%"),
					new SummonPassive("Holy Resist All", "Lowers Party Holy damage taken by 3%.", "Holy party dmg taken -3%"), 
					new SummonPassive("Holy Power Up", "Raises Holy BRV damage dealt by 10%.", "Holy dmg dealt +10%"),
					new SummonPassive("Alexander Boost Up", "Raises Max BRV by 5% after hitting HP<60% once", "MBrv+5% if HP<60% once"), 
					new SummonPassive("Alexander Base Attack Up", "Raises Initial BRV by 5% and Attack by 2% after hitting HP<60% once", "IBrv+5% & ATK+2% if HP<60% once"), 
					new SummonPassive("Alexander Target Power", "When attacking an enemy targeting self:\n- Increases BRV damage dealt by 10%", "BRV dmg dealt +10% if enemy targetting self"), 
					new SummonPassive("Alexander Receive Heal Up", "When BRV < 50% Max BRV- Increases HP recovered by 5%", "HP recovered +5% if MaxBRV<50%")));
			SUMMON_LIST.add(new Summon(Arrays.asList("Diabolos", "Diablos", "Dia"), 30, Element.Dark, "Dark Messenger", "Raises Initial BRV by 30%" + System.lineSeparator()
					+ "Permanently boosts Elemental Weakness Attacks by 30% when HP < 80%", "4000 Dark BRV Magical damage" + System.lineSeparator() + "Grants Dark attribute and faster Chase to the party", 
					"12|10|14|6|8", "Wakka|Aerith|Deuce|Kuja|Sabin|Vaan", 6, 6000, "Moderately Fast",  
					new SummonPassive("Dark Resist Up", "Lowers Dark damage taken by 10%.", "Dark dmg taken -10%"),
					new SummonPassive("Dark Resist All", "Lowers Party Dark damage taken by 3%.", "Dark party dmg taken -3%"), 
					new SummonPassive("Dark Power Up", "Raises Dark BRV damage dealt by 10%.", "Dark dmg dealt +10%"),
					new SummonPassive("Diabolos Attack Up", "Raises Attack by 5% after hitting HP<60% once", "ATK+5% if HP<60% once", true), 
					new SummonPassive("Diabolos Boost Guard Up", "Raises Max BRV by 2% and Defense by 10% when HP < 60% once", "MBrv+2% & DEF+20% if HP<80% once"), 
					new SummonPassive("Diabolos Receive BRV Heal Up", "Raises BRV gained by 5%", "BRV Gained +5%", true), 
					new SummonPassive("Diabolos Protect Up", "Lowers HP damage taken by 5% when Max BRV < 50%", "HP dmg taken -5% if BRV<50%")));
			SUMMON_LIST.add(new Summon(Arrays.asList("Odin"), 30, null, "Zantetsuken", "Decreases DEF of all enemies by 15%" + System.lineSeparator() + "After HP drops below 80%, permanently increases ATK by 30%.", 
					"4000 Melee BRV Melee damage with a low BREAK chance" + System.lineSeparator() + "Raises Attack of Swords and Greatswords users by 25%", 
					"8|6|10|12|14", "Noctis|Cecil Paladin|Fang|Irvine|Seven|Fran", 6, 6000, "Moderately Fast", 
					new SummonPassive("Physical Resist Up", "Lowers Physical damage taken by 10%.", "Physical dmg taken -10%"),
					new SummonPassive("Physical Resist All", "Lowers Party Physical damage taken by 3%.", "Physical party dmg taken -3%"), 
					new SummonPassive("Physical Power Up", "Raises Physical BRV damage dealt by 10%.", "Physical dmg dealt +10%"),
					new SummonPassive("Odin Attack Up", "Raises Attack by 5% when HP > 60%.", "ATK+5% if HP>60%", true), 
					new SummonPassive("Odin Base&Attack Up", "Raises Initial BRV by 5% and ATK by 2% when HP > 60%.", "IBrv+5% & ATK+5% if HP>60%", true), 
					new SummonPassive("Odin Sneak Power", "Raises BRV damage dealt by 10% when not being targetted", "BRV dmg dealt +10% if not targetted", true), 
					new SummonPassive("Odin Receive BRV Heal Up", "Raises BRV gained by 5%", "BRV gained +5%")));
			SUMMON_LIST.add(new Summon(Arrays.asList("Bahamut", "Baha"), 30, null, "Megaflare", "All enemies 15% Attack Down" + System.lineSeparator() + "Raises BREAK Bonus by 10% after an enemy is broken (up to 100%)", 
					"6000 BRV Magical damage" + System.lineSeparator() + "Raises non-elemental BRV damage by 100%", 
					"6|12|8|14|10", "Kuja|Rydia|Garnet|Lulu|Papalymo|Porom", 6, 6000, "Moderately Fast", 
					new SummonPassive("Magic Resist Up", "Lowers Magic damage taken by 10%.", "Magic dmg taken -10%"),
					new SummonPassive("Magic Resist All", "Lowers Party Magic damage taken by 3%.", "Magic party dmg taken -3%"), 
					new SummonPassive("Magic Power Up", "Raises Magic BRV damage dealt by 10%.", "Magic dmg dealt +10%"),
					new SummonPassive("Bahamut Boost Up", "Raises Max BRV by 10% when HP > 80% Max HP", "MBrv+10% if HP>80%", true), 
					new SummonPassive("Bahamut Boost Guard Up", "Raises Max BRV by 2% and Defense by 20% when HP > 80% Max HP", "MBrv+2% & DEF+20% if HP>80%", true), 
					new SummonPassive("Bahamut Life Up", "Raises Max HP by 10%.", "HP+10%", true), 
					new SummonPassive("Bahamut Knock Back HP Damage Up", "Raises HP damage dealt during a Chase Sequence by 5%", "Chase HP dmg dealt +5%")));
			SUMMON_LIST.add(new Summon(Arrays.asList("Spirit Moogle", "Spirit Mog", "Moogle", "Mog"), 20, null, "Don't fluff me, kupo~", "Increases Terra's Affection by 10", 
					"Removes Terra's debuffs" + System.lineSeparator() + 
					"Raises Terra's ATK by 10%", 1, 6000, "Very Fast"));
	}

	public String getName() { return name; }
	public Unit getUnit(String u) {
		try {
			return UNIT_CACHE.get(u);
		} catch (Exception e) {
			//System.out.println("Failed to register unit " + u); //FIXME 
		}
		return null;
	}
	public static Summon getSummon(String summonName) {
		return SUMMON_LIST.stream().filter(s -> s.getNicknames().contains(summonName.toLowerCase())).findFirst().orElse(null);
	}

	public static void reset() {
		GL.UNIT_CACHE.invalidateAll();
		JP.UNIT_CACHE.invalidateAll();
	}
}