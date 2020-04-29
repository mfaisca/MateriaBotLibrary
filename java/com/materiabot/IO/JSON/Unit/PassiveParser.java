package com.materiabot.IO.JSON.Unit;
import java.util.LinkedList;
import java.util.List;
import com.materiabot.GameElements.Passive;
import com.materiabot.GameElements.Passive.*;
import com.materiabot.IO.JSON.JSONParser;
import com.materiabot.IO.JSON.JSONParser.MyJSONObject;
import Shared.Dual;

public class PassiveParser {
	private String region;
	
	public PassiveParser(String r) { region = r.toLowerCase(); }
	
	public List<Passive> parsePassives(MyJSONObject obj, String passiveArray) {
		List<Passive> passives = new LinkedList<Passive>();
		for(MyJSONObject s : obj.getObjectArray(passiveArray)) {
			Passive p = new Passive();
			p.setId(s.getInt("id"));
			p.setName(s.getObject("name").getString(region));
			p.setDescription(s.getObject("desc").getString(region).replace("\\n", System.lineSeparator()));
			p.setShortDescription(s.getObject("short_desc").getString(region).replace("\\n", System.lineSeparator()));
			p.setCpCost(s.getObject("meta_data").getInt("cp"));
			p.setLevel(s.getObject("meta_data").getInt("level"));
			p.setTarget(Target.get(s.getObject("meta_data").getInt("target")));
			for(MyJSONObject e : s.getObjectArray("effects")) {
				Dual<JSONParser.ValueGrouping<Effect>, JSONParser.ValueGrouping<Required>> v = passiveExceptions(p, e);
				if(v != null){
					p.getEffects().add(v);
					continue;
				}
				Effect eff = Effect.get(e.getInt("effect_id"));
				Required req = Required.get(e.getInt("required_id"));
				Integer[] ev = e.getIntArray("effect_values");
				Integer[] rv = e.getIntArray("required_values");
				JSONParser.ValueGrouping<Effect> vge = eff == null ? 
												new JSONParser.ValueGrouping<Effect>(e.getInt("effect_id"), ev) : 
												new JSONParser.ValueGrouping<Effect>(eff, ev);
				JSONParser.ValueGrouping<Required> vgr = req == null ? 
												new JSONParser.ValueGrouping<Required>(e.getInt("required_id"), rv) : 
												new JSONParser.ValueGrouping<Required>(req, rv);
				v = new Dual<JSONParser.ValueGrouping<Effect>, JSONParser.ValueGrouping<Required>>(vge, vgr);
				p.getEffects().add(v);
			}
			passives.add(p);
		}
		return passives;
	}
	private static Dual<JSONParser.ValueGrouping<Effect>, JSONParser.ValueGrouping<Required>> passiveExceptions(Passive p, MyJSONObject e) {
		Effect eff = null;
		Required req = null;
		if(p.getId() == 0 && e.getInt("effect_id") == -0)
			eff = Effect.E102; //Filler for Example
		if(p.getId() == 1742 && e.getInt("required_id") == 52)
			req = Required.R52_2;
		else if(p.getId() == 296 && e.getInt("required_id") == 59)
			req = Required.R59_2;
		else if(p.getId() == 1000 && e.getInt("required_id") == 30)
			req = Required.R1;
		///////////////////////////////////////////////////////
		if(eff == null && req == null)
			return null;
		if(eff == null)
			eff = Effect.get(e.getInt("effect_id"));
		else if(req == null)
			req = Required.get(e.getInt("required_id"));
		return new Dual<JSONParser.ValueGrouping<Effect>, JSONParser.ValueGrouping<Required>>(
					new JSONParser.ValueGrouping<Effect>(eff, e.getIntArray("effect_values")), 
					new JSONParser.ValueGrouping<Required>(req, e.getIntArray("required_values")));
	}
}