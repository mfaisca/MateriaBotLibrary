package com.materiabot;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import com.materiabot.GameElements.Ability;
import com.materiabot.GameElements.Ailment;
import com.materiabot.GameElements.Passive;
import com.materiabot.IO.JSON.JSONParser;
import com.materiabot.IO.JSON.JSONParser.MyJSONObject;

public class JSON_Analyzer {
	public static void main(String[] args) throws Exception {
		mainAnalyzer(args);
	}
	public static void mainAnalyzer(String[] args) throws IOException {
		File main = new File("./resources/gl");
		HashMap<Integer, List<String>> helperMap = new HashMap<Integer, List<String>>();
		for(File f : main.listFiles()){
			String charName = getNameFromFileName(f.getName());
			MyJSONObject obj = JSONParser.loadContent(f.getAbsolutePath(), false);
			MyJSONObject[] abilities = obj.getObjectArray("completeListOfAbilities");
			MyJSONObject[] passives = obj.getObjectArray("awakeningPassives");
			boolean parseAbilities = true, parsePassives = false, parseAilments = false, single = false;
			if(parseAilments)
				for(MyJSONObject ability : abilities) {
					try {
						boolean brea = false;
						for(char c : ability.getString("name").toCharArray())
							if(!(('A' <= c && c <= 'z') || Arrays.asList('\'', '+', '&', '-', ':', '(', ')', '5').contains(c) || Character.isWhitespace(c)))
								brea = true;
						if(brea)
							continue;
						for(MyJSONObject ailment : ability.getObjectArray("ailments")) {
							int i = -1;
							for(int e : ailment.getObject("type_data").getIntArray("effects")) {
								i++;
								if(Ailment.Effect.EffectType.get(e) != null)
									continue;
								Integer key = e;
								if(!helperMap.containsKey(key))
									helperMap.put(key, new LinkedList<String>());
								//helperMap.get(key).add(charName + "@" + ability.getInt("id") + "@" + ailment.getInt("id") + "/" + ailment.getInt("cast_id") + "@" + ailment.getObject("meta_data").getInt("target") + "(" + ailment.getObject("type_data").getInt("buff_type") + "/" + ailment.getObject("type_data").getIntArray("effect_val_types")[i] + "/" + ailment.getObject("type_data").getIntArray("effect_rank_tables")[i] + "/" + ailment.getObject("rank_data").getIntArray("values")[i] + ")");
								helperMap.get(key).add(charName + "@" + ability.getInt("id") + " " + ability.getString("name") + "\n" + 
								"\t\t" + ailment.getInt("id") + "/" + ailment.getInt("cast_id") + " " + ailment.getString("name") + "@" + ailment.getObject("meta_data").getInt("target") + "\n" + 
								"\t\t" + ailment.getObject("type_data").getInt("buff_type") + "/" + ailment.getObject("type_data").getIntArray("effect_val_types")[i] + "/" + ailment.getObject("type_data").getIntArray("effect_rank_tables")[i] + "/" + ailment.getObject("rank_data").getIntArray("values")[i]);
							}
						}
					} catch(Exception e) {
						System.out.print("");
					}
				}
			if(parseAbilities)
				for(MyJSONObject ability : abilities) {
					try {
						boolean brea = false;
						for(char c : ability.getObject("name").getString("gl").toCharArray())
							if(!(('A' <= c && c <= 'z') || Arrays.asList('\'', '+', '&', '-', ':', '(', ')', '5').contains(c) || Character.isWhitespace(c)))
								brea = true;
						if(brea)
							continue;
						int i = 0;
						if(!single)
							for(MyJSONObject oo : ability.getObjectArray("hit_data")) {
								if(Ability.Details.Hit_Data.EffectType.get(oo.getInt("effect")) != null && Ability.Details.Hit_Data.EffectType.get(oo.getInt("effect")).getBaseDescription().length() >= 0)
									continue;
								Integer key = oo.getInt("effect")*100 + oo.getInt("effect_value_type");
								if(!helperMap.containsKey(key))
									helperMap.put(key, new LinkedList<String>());
								helperMap.get(key).add(charName + "@" + ability.getObject("name").getString("gl") + "/" + i++ + "(" + ability.getInt("id") + "/" + oo.getInt("id") + ")");
							}
						else {
							Integer key = ability.getObject("effects").getInt("effect_id");
							if(!helperMap.containsKey(key))
								helperMap.put(key, new LinkedList<String>());
							helperMap.get(key).add(charName + "@" + ability.getObject("name").getString("gl") + "/" + i++ + "(" + ability.getInt("id") + ")");
						}
					} catch(Exception e) {
						//System.out.println();
					}
				}
			if(parsePassives)
				for(MyJSONObject passive : passives) {
					try {
						boolean brea = false;
						for(char c : passive.getObject("name").getString("gl").toCharArray())
							if(!(('A' <= c && c <= 'z') || Arrays.asList('\'', '+', '&', '-', ':', '(', ')', '5').contains(c) || Character.isWhitespace(c)))
								brea = true;
						if(brea)
							continue;
						int i = 0;
						if(!single)
							for(MyJSONObject oo : passive.getObjectArray("effects")) {
								Integer key = oo.getInt("required_id");
								if(Passive.Required.get(key) != null)    //CHANGE_THIS
									continue;
								if(!helperMap.containsKey(key))
									helperMap.put(key, new LinkedList<String>());
								helperMap.get(key).add(charName + "@" + passive.getObject("name").getString("gl") + "/" + i++ + "(" + passive.getInt("id") + ")");
							}
						else {
							Integer key = passive.getObject("type_data").getInt("condition_type");
							if(!helperMap.containsKey(key))
								helperMap.put(key, new LinkedList<String>());
							helperMap.get(key).add(charName + "@" + passive.getObject("name").getString("gl") + "/" + i++ + "(" + passive.getInt("id") + ")");
						}
					} catch(Exception e) {
						
					}
				}
		}
		for(Entry<Integer, List<String>> s : helperMap.entrySet().stream().sorted((s1, s2) -> s1.getKey().compareTo(s2.getKey())).collect(Collectors.toList())) {
			System.out.println(s.getKey() + ": {");
			int cur = 0;
			for(String ss : s.getValue()) {
				if(++cur == 100) break;
				System.out.println("\t" + ss);
			}
			System.out.println("}");
		}
	}
	
	public static final String getNameFromFileName(String name){
		int i = 0;
		for(char c : name.toCharArray())
			if('0' <= c && c <= '9')
				i++;
			else
				break;
		return name.substring(i, name.indexOf("."));
	}
}