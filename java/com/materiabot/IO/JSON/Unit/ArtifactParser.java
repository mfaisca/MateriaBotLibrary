package com.materiabot.IO.JSON.Unit;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Set;
import org.json.JSONObject;
import com.materiabot.GameElements.Artifact;
import com.materiabot.IO.JSON.JSONParser;

public class ArtifactParser {
	@SuppressWarnings("unchecked")
	public static void parseAllCharacterArtifacts() throws MalformedURLException, IOException {
		JSONObject json = JSONParser.loadContent(JSONParser.JSON_PATH.ARTIFACT_PATH, true).obtainJSON();
		HashMap<Integer, Artifact> passiveEffects = new HashMap<Integer, Artifact>();
		JSONObject passiveList = ((JSONObject)json.get("artifactList"));
		for(String passiveId : ((Set<String>)passiveList.keySet())){
			JSONObject eff = ((JSONObject)passiveList.get(passiveId));
			Artifact a = new Artifact();
			a.setId(eff.optInt("id"));
			a.setName(((JSONObject)eff.opt("name")).optString("gl"));
			a.setDescription(((JSONObject)eff.opt("desc")).optString("gl"));
			passiveEffects.put(a.getId(), a);
		}
	}
}