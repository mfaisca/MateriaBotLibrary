package com.materiabot.IO.JSON.Unit;
import java.util.List;
import java.util.Arrays;
import java.util.LinkedList;
import com.materiabot.GameElements.Unit;
import com.materiabot.GameElements.Ability;
import com.materiabot.GameElements.Datamining.Ailment;
import com.materiabot.GameElements.Datamining.Ailment.Target;
import com.materiabot.IO.JSON.JSONParser.MyJSONObject;

public class AbilityParser {
	public static class AilmentParser{
		
	}
	private Unit unit;
	private String region;
	
	public AbilityParser(Unit u, String r) { unit = u; region = r.toLowerCase(); }
	
	public List<Ability> parseAbilities(MyJSONObject obj) {
		List<Ability> ret = new LinkedList<Ability>();
		for(MyJSONObject a : obj.getObjectArray("completeListOfAbilities")) {
			Ability ab = parseAbility(a);
			ret.add(ab);
		}
		return ret;
	}
	private Ability parseAbility(MyJSONObject ab) {
		Ability a = new Ability();
		a.setId(ab.getInt("id"));
		a.setName(ab.getObject("name").getString(region));
		a.setDescription(ab.getObject("desc").getString(region).replace("\\n", System.lineSeparator()));
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