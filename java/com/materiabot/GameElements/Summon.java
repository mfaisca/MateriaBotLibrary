package com.materiabot.GameElements;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.materiabot._Library;

public class Summon{	
	public static class SummonPassive{
		private String name, description, shortDesc;
		private boolean recommended = false;

		public SummonPassive(String n, String d){
			this(n, d, d, false);
		}
		public SummonPassive(String n, String d, boolean r){
			this(n, d, d, r);
		}
		public SummonPassive(String n, String d, String sd){
			this(n, d, sd, false);
		}
		public SummonPassive(String n, String d, String sd, boolean r) {
			name = n; description = d; shortDesc = sd; recommended = r;
		}
		
		public String getName() { return name; }
		public String getDescription() { return description; }
		public String getShortDesc() { return shortDesc; }
		public boolean isRecommended() { return recommended; }
		public void setRecommended(boolean recommended) { this.recommended = recommended; }
	}
	
	private Element element;
	private String attackName, blessing, ability, chargeType, nodeCount;
	private List<String> nicknames = new LinkedList<String>();
	private int turns, maxBrvBonus, maxLevel;
	private SummonPassive[] boardPassives;
	private String specialBoostedChars;
	private Unit[] chars = new Unit[6];
	
	public Summon(List<String> names, int level, Element element, String attackName, String blessing, String ability, int turns, int maxbrvBonus, String chargeSpeed) {
		this(names, level, element, attackName, blessing, null, ability, null, turns, maxbrvBonus, chargeSpeed);
	}
	public Summon(List<String> names, int level, Element element, String attackName, String blessing, String ability, 
					String nodeCount, String specialBoostedChars, int turns, int maxbrvBonus, String chargeSpeed, 
					SummonPassive... boardPassives) {
		nicknames = names.stream().map(s -> s.toLowerCase()).collect(Collectors.toList());
		this.maxLevel = level;
		this.element = element;
		this.attackName = attackName;
		this.blessing = blessing;
		this.ability = ability;
		this.nodeCount = nodeCount;
		this.boardPassives = boardPassives;
		this.turns = turns;
		this.maxBrvBonus = maxbrvBonus;
		this.chargeType = chargeSpeed;
		this.specialBoostedChars = specialBoostedChars;
	}
	
	public String getName() {
		return StringUtils.capitalize(nicknames.get(0));
	}
	public Element getElement() {
		return element;
	}
	public String getAttackName() {
		return attackName;
	}
	public String getBlessing() {
		return blessing;
	}
	public String getAbility() {
		return ability;
	}
	public String getChargeType() {
		return chargeType;
	}
	public String getNodeCount() {
		return nodeCount;
	}
	public List<String> getNicknames() {
		return nicknames;
	}
	public int getTurns() {
		return turns;
	}
	public int getMaxBrvBonus() {
		return maxBrvBonus;
	}
	public int getMaxLevel() {
		return maxLevel;
	}
	public SummonPassive[] getBoardPassives() {
		return boardPassives;
	}
	public Unit[] getSpecialBoosteds() {
		int i = 0;
		if(specialBoostedChars != null)
			for(String ch : StringUtils.split(specialBoostedChars, "|"))
				chars[i++] = _Library.L.getUnit(ch);
		return chars;
	}
	public String getEmoteFrame() { return getName().replace(" ", "").toLowerCase() + "Frame"; }
	public String getEmoteCrystal() { return getName().replace(" ", "").toLowerCase() + "Crystal"; }
	public String getEmoteFace() { return getName().replace(" ", "").toLowerCase() + "Face"; }

	public static final Summon getSummonFromWoIWeapon(Unit u) {
		Summon s = null;
		switch(u.getName().toLowerCase()){
			case "cloud": s = _Library.getSummon("Ifrit"); break;
			case "rem": s = _Library.getSummon("Shiva"); break;
			case "yuna": s = _Library.getSummon("Ramuh"); break;
			case "laguna": s = _Library.getSummon("Leviathan"); break;
			case "squall": s = _Library.getSummon("Brothers"); break;
			case "zidane": s = _Library.getSummon("Pandemonium"); break;
			case "hope": s = _Library.getSummon("Alexander"); break;
			case "edgar": s = _Library.getSummon("Diabolos"); break;
			case "bartz": s = _Library.getSummon("Odin"); break;
			case "vaan": s = _Library.getSummon("Bahamut"); break;
		}
		return s;
	}
}