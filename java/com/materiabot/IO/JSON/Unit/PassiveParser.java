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
		Required req = null;
		MyJSONObject[] r = s.getObjectArray("conditions");
		if(r.length == 0)
			req = Required.RN1;
		else
			req = Required.get(r[0].getInt("required_id"));
		for(MyJSONObject e : s.getObjectArray("effects")) {
			MyJSONObject rr = r.length != 0 ? r[0] : null;
			Dual<JSONParser.ValueGrouping<Effect>, JSONParser.ValueGrouping<Required>> v = passiveExceptions(p, e, rr);
			if(v != null){
				p.getEffects().add(v);
				continue;
			}
			if(p.getTarget() == null || p.getTarget().getId() < e.getInt("effect_target"))
				p.setTarget(Target.get(e.getInt("effect_target")));
			Effect eff = Effect.get(e.getInt("effect_id"));
			Integer[] ev = e.getIntArray("effect_values");
			Integer[] rv = null;
			if(eff == null)
				System.out.println("PE" + e.getInt("effect_id") + " | " + p.getName() + "(" + p.getId() + ") | Vals:(" + Arrays.toString(ev) + ")");
			JSONParser.ValueGrouping<Effect> vge = eff == null ? 
					new JSONParser.ValueGrouping<Effect>(e.getInt("effect_id"), ev) : 
					new JSONParser.ValueGrouping<Effect>(eff, ev);
			int rId = 1;
			if(s.getObjectArray("conditions").length > 0) {
				MyJSONObject c = s.getObjectArray("conditions")[0];
				rId = c.getInt("required_id");
				rv = c.getIntArray("required_values");
				if(req == null)
					System.out.println("PR" + c.getInt("required_id") + " | " + p.getName() + "(" + p.getId() + ") | Vals:(" + Arrays.toString(rv) + ") | Target: " + c.getInt("required_target") + "/" + c.getInt("required_target_value"));
			}
			JSONParser.ValueGrouping<Required> vgr = req == null ? 
											new JSONParser.ValueGrouping<Required>(rId, rv) : 
											new JSONParser.ValueGrouping<Required>(req, rv);
			v = new Dual<JSONParser.ValueGrouping<Effect>, JSONParser.ValueGrouping<Required>>(vge, vgr);
			p.getEffects().add(v);
		}
		return p;
	}
	private static Dual<JSONParser.ValueGrouping<Effect>, JSONParser.ValueGrouping<Required>> passiveExceptions(Passive p, MyJSONObject e, MyJSONObject r) {
		Effect eff = null;
		Required req = null;
//		if(p.getId() == 0 && e.getInt("effect_id") == -0)
//			eff = Effect.E102; //Filler for Example
		if(p.getId() == 537 && e.getInt("effect_id") == 19)
			eff = Effect.E19_2; //For High Armor - Essence of Strategy
		int reqId = r == null ? -1 : r.getInt("required_id");
		
		if(p.getId() == 1742 && reqId == 52)
			req = Required.R52_2;
		else if((p.getId() == 296 || p.getId() == 1803 || p.getId() == 5099) && reqId == 59)
			req = Required.R59_2;
		else if(p.getId() == 1000 && reqId == 30)
			req = Required.R1;
		else if(p.getId() == 201 && reqId == 77) //Cloud NT
			req = Required.R77_2;
		else if(p.getId() == 988 && reqId == 77) //Alisaie CL50
			req = Required.R77_3;
		///////////////////////////////////////////////////////
		if(eff == null && req == null)
			return null;
		if(eff == null)
			eff = Effect.get(e.getInt("effect_id"));
		else if(req == null)
			req = Required.get(reqId);
		return new Dual<JSONParser.ValueGrouping<Effect>, JSONParser.ValueGrouping<Required>>(
					new JSONParser.ValueGrouping<Effect>(eff, e.getIntArray("effect_values")), 
					new JSONParser.ValueGrouping<Required>(req, reqId));
	}
}