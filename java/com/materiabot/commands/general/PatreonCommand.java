package com.materiabot.commands.general;
import java.awt.Color;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import Shared.BotException;
import com.materiabot.IO.SQL.SQLAccess;
import com.materiabot.Utils.Constants;
import com.materiabot.Utils.ImageUtils;
import com.materiabot.Utils.MessageUtils;
import com.materiabot.Utils.ImageUtils.Emotes;
import com.materiabot.commands._BaseCommand;
import com.patreon.PatreonAPI;
import com.patreon.resources.Campaign;
import com.patreon.resources.Pledge;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;

public class PatreonCommand extends _BaseCommand{
	private static final String PATREON_LINK = "https://www.patreon.com/MateriaBot";
	private static final int NUMBER_OF_VISIBLE_PATRONS = 15;
	
	public PatreonCommand() { 
		super("patreon","donate","donation");
	}
	public static boolean isUserPatreon(User u) {
		Guild materiaServer = u.getJDA().getGuildById(Constants.MATERIABOT_SERVER_ID);
		return u.getIdLong() == Constants.QUETZ_ID || 
				Stream.of(materiaServer.getMembersWithRoles(materiaServer.getRoleById(554660297927819264L)), 
						materiaServer.getMembersWithRoles(materiaServer.getRoleById(554660182093987869L)))
							.flatMap(l -> l.stream()).distinct().anyMatch(m -> m.getUser().equals(u));
	}
	
	private static EmbedBuilder build(String bannerURL, List<String> patrons, String oldest, String mostRecent, int totalPatrons) {
		EmbedBuilder builder = new EmbedBuilder();
		builder.setTitle("MateriaBot Patreon");
		builder.setThumbnail(Constants.getClient().getSelfUser().getAvatarUrl());
		builder.setColor(new Color(249,104,84));
		builder.setImage(bannerURL);
		builder.addField("Link", PATREON_LINK, false);
		builder.addField("Special Mentions", 
				"Oldest Patron: " + oldest + System.lineSeparator() + 
				"Newest Patron: " + mostRecent + System.lineSeparator(), true);
		builder.addField("Patron Count", totalPatrons + " Patreons", true);
		int i = 0;
		while((i * NUMBER_OF_VISIBLE_PATRONS) < patrons.size()) {
			builder.addField(i == 0 ? "Patrons" : MessageUtils.S, 
							patrons.subList(
								i * NUMBER_OF_VISIBLE_PATRONS, 
								(i * NUMBER_OF_VISIBLE_PATRONS + NUMBER_OF_VISIBLE_PATRONS) >= 
									patrons.size() ? patrons.size() : 
									i * NUMBER_OF_VISIBLE_PATRONS + NUMBER_OF_VISIBLE_PATRONS)
							.stream().reduce((o1,  o2) -> o1 + System.lineSeparator() + o2).orElse("---"), false);
			i++;
		}
		return builder;
	}
	
	//This method exists to update Tonberry Troupe Patreon roles on MateriaBot Server due to PatreonBot not working with multiple accounts
	public static final void joinServerTonberryTroupeUpdate() {
		try {
			Guild materiaServer = Constants.getClient().getGuildById(Constants.MATERIABOT_SERVER_ID);
			final PatreonAPI apiClient = new PatreonAPI(SQLAccess.getKeyValue(SQLAccess.PATREON_TT_ACCESS_TOKEN));
			final Campaign campaign = apiClient.fetchCampaigns().get().get(0);
			final List<Pledge> pledges = apiClient.fetchAllPledges(campaign.getId());
			final List<String> patreonDiscordIdsT2 = new LinkedList<String>();
			final List<String> patreonDiscordIdsT3 = new LinkedList<String>();
			for(Pledge p : pledges)
				if(p.getPatron().getSocialConnections() != null)
					if(p.getPatron().getSocialConnections().getDiscord() != null)
						if(p.getReward().getTitle().equals("Tonberry")) {
							if(p.getReward().getTitle().equals("Tonberry King"))
								patreonDiscordIdsT3.add(p.getPatron().getSocialConnections().getDiscord().getUser_id());
							patreonDiscordIdsT2.add(p.getPatron().getSocialConnections().getDiscord().getUser_id());
						}
			final Role tonberry = materiaServer.getRoleById(679814832626991129L);
			final Role tonberryKing = materiaServer.getRoleById(682232684466274307L);
			for(final Member m : materiaServer.getMembersWithRoles(tonberryKing)) {
				if(!patreonDiscordIdsT3.contains(m.getId())) {
					MessageUtils.sendWhisper(Constants.INK_ID, m.getEffectiveName() + " is no longer a Tonberry King.");
					materiaServer.removeRoleFromMember(m, tonberryKing).submit();
				}
			}
			for(final Member m : materiaServer.getMembersWithRoles(tonberry)) {
				if(!patreonDiscordIdsT2.contains(m.getId())) {
					MessageUtils.sendWhisper(Constants.INK_ID, m.getEffectiveName() + " is no longer a Tonberry.");
					materiaServer.removeRoleFromMember(m, tonberry).submit();
				}
			}
			for(final String mm : patreonDiscordIdsT2) {
				Member m = materiaServer.getMemberById(mm);
				if(m == null) continue;
				if(!m.getRoles().contains(tonberry)) {
					MessageUtils.sendWhisper(Constants.INK_ID, m.getEffectiveName() + " is a new Tonberry.");
					materiaServer.addRoleToMember(m, tonberry).submit();
				}
			}
			for(final String mm : patreonDiscordIdsT3) {
				final Member m = materiaServer.getMemberById(mm);
				if(m == null) continue;
				if(!m.getRoles().contains(tonberryKing)) {
					MessageUtils.sendWhisper(Constants.INK_ID, m.getEffectiveName() + " is a new Tonberry King.");
					materiaServer.addRoleToMember(m, tonberry).submit();
					materiaServer.addRoleToMember(m, tonberryKing).submit();
				}
			}
		} catch (IOException e) {
			MessageUtils.sendWhisper(Constants.INK_ID, "Patreon Key is dead, please refresh." + System.lineSeparator() + "https://www.patreon.com/portal/registration/register-clients");
			MessageUtils.sendWhisper(Constants.DREAMY_ID, "Patreon Key is dead, please refresh." + System.lineSeparator() + "https://www.patreon.com/portal/registration/register-clients");
			MessageUtils.sendWhisper(Constants.QUETZ_ID, "**Tonberry Troupe** Patreon Key is dead, please refresh." + System.lineSeparator() + "https://www.patreon.com/portal/registration/register-clients");
			e.printStackTrace();
		}
	}

	@Override
	public void doStuff(Message message) {
		try {
			PatreonAPI apiClient = new PatreonAPI(SQLAccess.getKeyValue(SQLAccess.PATREON_ACCESS_TOKEN));
			if(message.getGuild().getIdLong() == Constants.MATERIABOT_SERVER_ID && message.getAuthor().getIdLong() == Constants.QUETZ_ID && message.getContentRaw().contains(" ")) {
				SQLAccess.executeInsert("UPDATE Configs SET value = ? WHERE keyy = 'PATREON_ACCESS_TOKEN'", message.getContentRaw().split(" ")[1]);
				MessageUtils.sendMessage(message.getChannel(), "Patreon API Key Updated");
				message.delete();
			}
			else if(message.getGuild().getIdLong() == Constants.MATERIABOT_SERVER_ID && (message.getAuthor().getIdLong() == Constants.INK_ID || message.getAuthor().getIdLong() == Constants.DREAMY_ID) && message.getContentRaw().contains(" ")) {
				SQLAccess.executeInsert("UPDATE Configs SET value = ? WHERE keyy = 'PATREON_TT_ACCESS_TOKEN'", message.getContentRaw().split(" ")[1]);
				MessageUtils.sendMessage(message.getChannel(), "Patreon API Key Updated");
				message.delete();
			}else {
				final SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
				Campaign campaign = apiClient.fetchCampaigns().get().get(0);
				List<Pledge> pledges = apiClient.fetchAllPledges(campaign.getId());
				int totalPatrons = pledges.size();
				String mostRecent = pledges.stream().sorted((p1, p2) -> {
					try { return f.parse(p2.getCreatedAt()).compareTo(f.parse(p1.getCreatedAt())); }
					catch (ParseException e) { ; } return 0;
					}).findFirst().map(p -> (p.getReward() == null ? ImageUtils.getEmoteText(Emotes.UNKNOWN_EMOTE.get()) : ImageUtils.getEmoteText(p.getReward().getTitle())) + " " + p.getPatron().getFullName()).orElse(null);
				String oldest = pledges.stream().sorted((p1, p2) -> {
					try { return f.parse(p1.getCreatedAt()).compareTo(f.parse(p2.getCreatedAt()));}
					catch (ParseException e) { ; } return 0;
					}).findFirst().map(p -> (p.getReward() == null ? ImageUtils.getEmoteText(Emotes.UNKNOWN_EMOTE.get()) : ImageUtils.getEmoteText(p.getReward().getTitle())) + " " + p.getPatron().getFullName()).orElse(null);
				List<String> patrons = pledges.stream()
					.sorted((p1, p2) -> p2.getAmountCents() - p1.getAmountCents())
					.map(p -> (p.getReward() == null ? ImageUtils.getEmoteText(Emotes.UNKNOWN_EMOTE.get()) : ImageUtils.getEmoteText(p.getReward().getTitle())) + " " + p.getPatron().getFullName()).collect(Collectors.toList());
				MessageUtils.sendEmbed(message.getChannel(), build(campaign.getImageUrl(), patrons, oldest, mostRecent, totalPatrons));
			}
		} catch (IOException | BotException e) {
			MessageUtils.sendWhisper(Constants.QUETZ_ID, "Patreon Key is dead, please refresh." + System.lineSeparator() + "https://www.patreon.com/portal/registration/register-clients");
			MessageUtils.sendMessage(message.getChannel(), "Error connecting with Patreon API. Try again later.");
			e.printStackTrace();
		}
	}
}