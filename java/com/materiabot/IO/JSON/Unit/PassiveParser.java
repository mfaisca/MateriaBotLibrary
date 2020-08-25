package com.materiabot.IO.JSON.Unit;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import com.materiabot.GameElements.Passive;
import com.materiabot.GameElements.Passive.*;
import com.materiabot.IO.JSON.JSONParser;
import com.materiabot.IO.JSON.JSONParser.MyJSONObject;
import Shared.Dual;
import Shared.Methods;

public class PassiveParser {
	public PassiveParser() { }
	
	public List<Passive> parsePassives(MyJSONObject obj, String passiveArray) {
		List<Passive> passives = new LinkedList<Passive>();
		for(MyJSONObject s : obj.getObjectArray(passiveArray)) {
			Passive p = parsePassive(s);
			if(p != null)
				passives.add(p);
		}
		return passives;
	}
	public Passive parsePassive(MyJSONObject s){
		Passive p = new Passive();
		if(s.getInt("error") != null) return null;
		p.setId(s.getInt("id"));
		p.setName(Methods.getBestText(s.getStringArray(s.getObject("name"))));
		p.setDescription(Methods.getBestText(s.getStringArray(s.getObject("desc"))).replace("\\n", System.lineSeparator()));
		p.setShortDescription(Methods.getBestText(s.getStringArray(s.getObject("short_desc"))).replace("\\n", System.lineSeparator()));
		p.setCpCost(s.getObject("meta_data").getInt("cp"));
		p.setLevel(s.getObject("meta_data").getInt("level"));
		p.setTarget(Target.get(s.getObject("meta_data").getInt("target")));
		for(MyJSONObject e : s.getObjectArray("effects")) {
			Dual<JSONParser.ValueGrouping<Effect>, JSONParser.ValueGrouping<Required>> v = passiveExceptions(p, e);
			if(v != null){
				p.getEffects().add(v);
				continue;
			}
			if(p.getTarget() == null || p.getTarget().getId() < e.getInt("effect_target"))
				p.setTarget(Target.get(e.getInt("effect_target")));
			Effect eff = Effect.get(e.getInt("effect_id"));
			Required req = Required.get(e.getInt("required_id"));
			Integer[] ev = e.getIntArray("effect_values");
			Integer[] rv = e.getIntArray("required_values");
			if(eff == null)
				System.out.println("PE" + e.getInt("effect_id") + " | " + p.getName() + "(" + p.getId() + ") | Vals:(" + Arrays.toString(ev) + ")");
			if(req == null)
				System.out.println("PR" + e.getInt("required_id") + " | " + p.getName() + "(" + p.getId() + ") | Vals:(" + Arrays.toString(rv) + ") | Target: " + e.getInt("required_target") + "/" + e.getInt("required_target_value"));
			JSONParser.ValueGrouping<Effect> vge = eff == null ? 
											new JSONParser.ValueGrouping<Effect>(e.getInt("effect_id"), ev) : 
											new JSONParser.ValueGrouping<Effect>(eff, ev);
			JSONParser.ValueGrouping<Required> vgr = req == null ? 
											new JSONParser.ValueGrouping<Required>(e.getInt("required_id"), rv) : 
											new JSONParser.ValueGrouping<Required>(req, rv);
			v = new Dual<JSONParser.ValueGrouping<Effect>, JSONParser.ValueGrouping<Required>>(vge, vgr);
			p.getEffects().add(v);
		}
		return p;
	}
	private static Dual<JSONParser.ValueGrouping<Effect>, JSONParser.ValueGrouping<Required>> passiveExceptions(Passive p, MyJSONObject e) {
		Effect eff = null;
		Required req = null;
//		if(p.getId() == 0 && e.getInt("effect_id") == -0)
//			eff = Effect.E102; //Filler for Example
		if(p.getId() == 537 && e.getInt("effect_id") == 19)
			eff = Effect.E19_2; //For High Armor - Essence of Strategy
		if(p.getId() == 1742 && e.getInt("required_id") == 52)
			req = Required.R52_2;
		else if((p.getId() == 296 || p.getId() == 1803 || p.getId() == 5099) && e.getInt("required_id") == 59)
			req = Required.R59_2;
		else if(p.getId() == 1000 && e.getInt("required_id") == 30)
			req = Required.R1;
		else if(p.getId() == 201 && e.getInt("required_id") == 77) //Cloud NT
			req = Required.R77_2;
		else if(p.getId() == 988 && e.getInt("required_id") == 77) //Alisaie CL50
			req = Required.R77_3;
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