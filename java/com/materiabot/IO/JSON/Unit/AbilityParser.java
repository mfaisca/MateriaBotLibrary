package com.materiabot.IO.JSON.Unit;
import java.util.Arrays;
import java.util.stream.Collectors;
import com.materiabot.GameElements.Unit;
import Shared.Methods;
//import com.google.common.base.CharMatcher;
import com.materiabot.GameElements.Ability;
import com.materiabot.GameElements.Ability.Details;
import com.materiabot.GameElements.Ability.Details.Attack_Type;
import com.materiabot.GameElements.Ability.Details.Command_Type;
import com.materiabot.GameElements.Ability.Details.Hit_Data.EffectType;
import com.materiabot.GameElements.Ability.Details.Target_Type;
import com.materiabot.GameElements.Element;
import com.materiabot.IO.JSON.JSONParser.MyJSONObject;

public class AbilityParser {
	private Unit unit;
	
	public AbilityParser(Unit u) { unit = u; }
	
	public void parseAbilities(MyJSONObject obj, String abilityArray) {
		for(MyJSONObject a : obj.getObjectArray(abilityArray)) {
			if(a.getInt("error") != null) continue; //Some exception I made???
//			if(!CharMatcher.ascii().matchesAllOf(a.getObject("name").getString("gl"))) //Ignore JP Skills in GL
//				continue;
			parseAbility(a);
		}
	}
	public Ability parseAbility(MyJSONObject ab) {
		Ability a = new Ability();
		a.setId(ab.getInt("id"));
//		if(ab.getObject("name").getString("en").length() == 0 && 
//			ab.getObject("name").getString("gl").length() == 0) return null;
		a.setName(Methods.getBestText(ab.getStringArray(ab.getObject("name"))));
		a.setDescription(Methods.getBestText(ab.getStringArray(ab.getObject("desc"))).replace("\\n", System.lineSeparator()));
		a.setUseCount(ab.getInt("use_count"));
		a.setDetails(new Details());
		a.getDetails().setAttackType(Details.Attack_Type.get(ab.getObject("type_data").getInt("attack_type")));
		a.setUnit(unit);
		Details d = new Details();
		a.setDetails(d);
		d.setAttackType(Attack_Type.get(ab.getObject("type_data").getInt("attack_type")));
		d.setChaseDmg(ab.getObject("chase_data").getInt("can_initiate_chase") * ab.getObject("chase_data").getInt("chase_dmg"));
		d.setCommandType(Command_Type.get(ab.getObject("type_data").getInt("command_type")));
		d.setMovementCost(ab.getInt("movement_cost"));
		d.setTargetType(Target_Type.get(ab.getObject("type_data").getInt("target_type")));
		unit.getAbilities().put(a.getId(), a);
		for(MyJSONObject data : ab.getObjectArray("hit_data")) {
			Details.Hit_Data hd = new Details.Hit_Data();
			hd.setId(data.getInt("id"));
			hd.setType(Details.Hit_Data.Type.get(data.getInt("type")));
			if(hd.getType() == null) {
				continue;
			}
			hd.setArguments(data.getIntArray("arguments"));
			hd.setBrvRate(data.getObject("brv_data").getInt("brv_rate"));
			hd.setMaxBrvOverflow(data.getObject("brv_data").getInt("max_brv_overflow"));
			hd.setMaxBrvOverflowOnBreak(data.getObject("brv_data").getInt("max_brv_overflow_with_break"));
			hd.setSingleTargetBrvRate(data.getObject("brv_data").getInt("single_target_brv_rate"));
			hd.setBrvDamageLimit(data.getObject("brv_data").getInt("brv_damage_limit_up"));
			hd.setMaxBrvLimit(data.getObject("brv_data").getInt("max_brv_limit_up"));
			hd.setAttackType(Details.Hit_Data.Attack_Type.get(data.getInt("attack_type")));
			hd.addElements(Arrays.asList(data.getIntArray("element")).stream().map(e -> Element.get(e.intValue())).collect(Collectors.toList()));
			hd.setTarget(Details.Hit_Data.Target.get(data.getInt("target")));
			if(hd.getTarget() == null)
				System.out.println("ST" + data.getInt("target") + " | " + a.getName() + "(" + a.getId() + ") | HD ID: " + hd.getId());
			hd.setEffect(new Details.Hit_Data.Effect());
			hd.getEffect().setEffect(EffectType.get(data.getInt("effect")));
			if(hd.getEffect().getEffect() == null)
				System.out.println("SE" + data.getInt("effect") + " | " + a.getName() + "(" + a.getId() + ") | HD ID: " + hd.getId());
			hd.getEffect().setEffectValueType(data.getInt("effect_value_type"));
			if(hd.getEffect().getEffectValueType() == -1)
				System.out.println("SEVT" + data.getInt("effect_value_type") + " | " + a.getName() + "(" + a.getId() + ") | HD ID: " + hd.getId());
			d.getHits().add(hd);
		}
		d.getAilments().addAll(new AilmentParser(unit).parseAilments(ab, "ailments"));
		return a;
	}
}