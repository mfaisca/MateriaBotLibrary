package com.materiabot.Utils;
import java.util.Optional;
import com.materiabot.GameElements.Equipment;
import com.materiabot.GameElements.Unit;
import net.dv8tion.jda.api.entities.Emote;

public abstract class ImageUtils {
	public static enum Emotes{
		UNKNOWN_EMOTE("unknownEmote"),
		GEAR_NO_LB("iconnolb"),
		GEAR_LB("iconlb"),
		BLUE_ORB("blue_orb"),
		BLUE_POP("blue_pop"),
		SILVER_ORB("silver_orb"),
		SILVER_POP("silver_pop"),
		GOLD_ORB("gold_orb"),
		GOLD_POP("gold_pop"),
		BURST_ORB("burst_orb"),
		BURST_POP("burst_pop"),
		
		;private String text;
		
		private Emotes(String s) { text = s; }
		public String get() { return text; }
	}
	public static enum Images{
		BLUE_ANIM("https://cdn.discordapp.com/attachments/570156853296758794/719123410223104020/blue_1.gif"),
		SILVER_ANIM("https://cdn.discordapp.com/attachments/570156853296758794/719123423921438750/silverfast.gif"),
		GOLD_ANIM("https://cdn.discordapp.com/attachments/570156853296758794/719123419945369640/goldfast.gif"),
		BURST_ANIM("https://cdn.discordapp.com/attachments/570156853296758794/719123430124814374/burst_1.gif"),
		SORRY_MOOGLE_URL("https://dissidiadb.com/static/img/0002.0260da5.png"),
		SURPRISED_MOOGLE_URL("https://dissidiadb.com/static/img/0014.6ec7022.png"),
		
		;private String text;
		
		private Images(String s) { text = s; }
		public String get() { return text; }
	}
	
	public static String getCharacterGearURL(Unit u, Equipment.Rarity equip) {
//		String unitName = u.getName().replace(" ", "").replace("'", "");
//		String gearTag = equip.getImageName();
		//http://dissidiacompendium.com/images/static/characters/Ace/4a.png
		return "https://dissidiadb.com/static/img/gear/" + u.getEquipment(equip).getId() + ".png";
	}
	
	public static Emote getEmoteClassByName(String name) {
		final String name2 = name.replaceAll(" ", "").replaceAll("'", "").replaceAll("&", "");
		Emote emo = Constants.getClient().getGuilds().stream()
				.filter(s -> {
					return s.getOwnerIdLong() == Constants.QUETZ_ID && s.getIdLong() != Constants.MATERIABOT_SERVER_ID;
				})
				.flatMap(g -> {
					return g.getEmotes().stream();
				})
				.filter(e -> {
					return e.getName().equalsIgnoreCase(name2);
				})
				.findFirst()
				.orElse(name2.equalsIgnoreCase(ImageUtils.Emotes.UNKNOWN_EMOTE.get()) ? null : getEmoteClassByName(ImageUtils.Emotes.UNKNOWN_EMOTE.get()));
		return emo;
	}
	public static final String getEmoteText(String name) {
		Optional<Emote> o = Optional.ofNullable(getEmoteClassByName(name));
		return o.isPresent() ? "<:" + o.get().getName() + ":" + o.get().getId() + ">" : (name.equalsIgnoreCase(ImageUtils.Emotes.UNKNOWN_EMOTE.get()) ? null : getEmoteText(ImageUtils.Emotes.UNKNOWN_EMOTE.get()));
	}
}