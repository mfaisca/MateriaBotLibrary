package com.materiabot.IO.JSON.Unit;
import java.util.Arrays;
import com.materiabot.GameElements.Unit;
import com.google.common.base.CharMatcher;
import com.materiabot.GameElements.Ability;
import com.materiabot.GameElements.Datamining.Ability2;
import com.materiabot.GameElements.Datamining.Ailment;
import com.materiabot.GameElements.Datamining.Ailment.Target;
import com.materiabot.IO.JSON.JSONParser.MyJSONObject;

public class AbilityParser {
	public static class AilmentParser{
		
	}
	private Unit unit;
	private String region;
	
	public AbilityParser(Unit u, String r) { unit = u; region = r.toLowerCase(); }
	
	public void parseAbilities(MyJSONObject obj, String abilityArray) {
		for(MyJSONObject a : obj.getObjectArray(abilityArray)) {
			if(a.getInt("error") != null) continue; //Some exception I made???
			if(region.equals("gl") && !CharMatcher.ascii().matchesAllOf(a.getObject("name").getString(region))) //Ignore JP Skills in GL
				continue;
			parseAbility(a);
		}
	}
	public Ability parseAbility(MyJSONObject ab) {
		Ability a = new Ability();
		a.setId(ab.getInt("id"));
		a.setName(ab.getObject("name").getString(region));
		a.setDescription(ab.getObject("desc").getString(region).replace("\\n", System.lineSeparator()));
		a.setUseCount(ab.getInt("use_count"));
		a.setDetails(new Ability2());
		a.getDetails().setAttackType(Ability2.Attack_Type.get(ab.getObject("type_data").getInt("attack_type")));
		a.setUnit(unit);
		unit.getAbilities().put(a.getId(), a);
		//TODO Parse HitData Here
		for(MyJSONObject ailment : ab.getObjectArray("ailments")) {
			Ailment ail = new Ailment();
			ail.setId(ailment.getInt("id"));
			ail.setCastId(ailment.getInt("cast_id"));
			ail.setName(ailment.getObject("name").getString(region));
			ail.setDescription(ailment.getObject("desc").getString(region));
			ail.setRate(ailment.getObject("meta_data").getInt("rate"));
			ail.setRank(ailment.getObject("meta_data").getInt("rank"));
			ail.setTarget(Target.get(ailment.getObject("meta_data").getInt("target")));
			ail.setDuration(ailment.getObject("meta_data").getInt("duration"));
			ail.setArgs(Arrays.asList(ailment.getObject("meta_data").getIntArray("arguments")).stream().mapToInt(i->i).toArray());
			//TODO Parse Ailments here
			unit.getAilments().put(ail.getId(), ail);
		}
		return a;
	}
}