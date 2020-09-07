package com.materiabot.IO.JSON.Unit;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import com.materiabot.GameElements.Unit;
import Shared.Methods;
import com.materiabot.GameElements.Ailment;
import com.materiabot.GameElements.Ailment.EffectGrouping;
import com.materiabot.GameElements.Ailment.Aura;
import com.materiabot.GameElements.Ailment.Target;
import com.materiabot.IO.JSON.JSONParser.MyJSONObject;

public class AilmentParser {
	private Unit unit;
	
	public AilmentParser(Unit u) { unit = u; }
	
	public List<Ailment> parseAilments(MyJSONObject obj, String abilityArray) {
		List<Ailment> ret = new LinkedList<Ailment>();
		for(MyJSONObject a : obj.getObjectArray(abilityArray)) {
			Ailment aa = parseAilment(a);
			if(aa != null)
				ret.add(aa);
		}
		return ret;
	}
	private Ailment parseAilment(MyJSONObject ailment) {
		Ailment ail = new Ailment(unit);
		ail.setId(ailment.getInt("id"));
		if(ail.getId() == -1)
			return null;
		ail.setCastId(ailment.getInt("cast_id"));
		ail.setName(Methods.getBestText(ailment.getStringArray(ailment.getObject("name"))));
		ail.setDescription(Methods.getBestText(ailment.getStringArray(ailment.getObject("desc"))).replace("\\n", System.lineSeparator()));
		ail.setRate(ailment.getObject("meta_data").getInt("rate"));
		ail.setRank(ailment.getObject("meta_data").getInt("rank")-1); //Indexing on source files starts at 1 instead of 0
		ail.setTarget(Target.get(ailment.getObject("meta_data").getInt("target")));
		ail.setDuration(ailment.getObject("meta_data").getInt("duration"));
		ail.setArgs(Arrays.asList(ailment.getObject("meta_data").getIntArray("arguments")).stream().mapToInt(i->i).toArray());
		ail.setBuffType(ailment.getObject("type_data").getInt("buff_type"));
		ail.setIconType(ailment.getObject("type_data").getInt("icon_type"));
		ail.setStackable(ailment.getObject("type_data").getInt("stackable") == 1);
		ail.setExtendable(ailment.getObject("type_data").getInt("extendable") == 1);
		ail.setFramed(ailment.getObject("type_data").getInt("removable") == 0);
		ail.setMaxStacks(ailment.getObject("type_data").getInt("max_stacks"));
		Integer[] effects = ailment.getObject("type_data").getIntArray("effects");
		for(int i = 0; i < effects.length; i++) {
			if(effects[i] == -1) continue;
			EffectGrouping eff = new Ailment.EffectGrouping();
			eff.effectId = effects[i];
			eff.rank = ail.getRank();
			eff.val_type = ailment.getObject("type_data").getIntArray("val_types")[i];
			eff.val_specify = ailment.getObject("type_data").getIntArray("val_specify")[i];
			Integer[] temp = ailment.getObject("rank_data").getIntArray(""+ailment.getObject("type_data").getIntArray("rank_tables")[i]);
			eff.rankData = temp != null ? Arrays.stream(temp).map(iii -> ""+iii).collect(Collectors.toList()).toArray(new String[0]) : null;
			ail.getEffects().add(eff);
		}
		for(MyJSONObject aura : ailment.getObjectArray("auras")) {
			Aura a = new Aura();
			a.id = aura.getInt("id");
			a.requiredConditions = aura.getIntArray("required_conditions");
			a.requiredValues = aura.getIntArray("required_values");
			a.effect = aura.getObject("effect_data").getInt("id");
			a.ailmentEffect = aura.getObject("effect_data").getInt("ailment_effect");
			a.target = aura.getObject("effect_data").getInt("target_id");
			a.valType = aura.getObject("effect_data").getInt("value_type");
			a.typeId = aura.getObject("effect_data").getInt("type_id");
			if(a.ailmentEffect == -1)
				a.ailmentEffect = a.typeId;
			a.rankData = Arrays.stream(aura.getObject("effect_data").getIntArray("rank_data")).map(iii -> ""+iii).collect(Collectors.toList()).toArray(new String[0]);;
			ail.getAuras().put(a.id, a);
		}
		unit.getAilments().put(ail.getId(), ail);
		return ail;
	}
}