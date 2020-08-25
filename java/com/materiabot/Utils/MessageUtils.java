package com.materiabot.Utils;
import java.util.concurrent.CompletableFuture;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.User;

public abstract class MessageUtils {
	public static enum DefaultMessagess{
		CHAR_NOT_FOUND("Character Not Found Error"),
		EQUIP_NOT_FOUND("Equipment Not Found Error"),
		SKILL_NOT_FOUND("Skill Not Found Error"),
		PASSIVE_NOT_FOUND("Passive Not Found Error"),
		SKILL_SETUP_ERROR("Skill Setup Error"),
		SYNTAX_ERROR("Syntax Error"),
		UNKNOWN_ERROR("Unknown Error - Please contact Quetz!"),
		;

		private String msg;

		private DefaultMessagess(String msg) { this.msg = msg; }
		public String getMessage() { return msg; }
	}

	public static final String S = "‏‏‎ ";
	public static final int DISCORD_MESSAGE_LIMIT = 1800;

	public static final String empty(int l) {
		String s = "";
		for(int i = 0; i < l; i++)
			s += "‏‏‎ ‎";
		return s;
	}

	public static final CompletableFuture<Message> sendStatusMessageError(MessageChannel channel, String message) {
		return sendMessage(channel, ":no_entry: | " + message);
	}
	public static final CompletableFuture<Message> sendStatusMessageCrash(MessageChannel channel, String message) {
		return sendMessage(channel, ":ambulance: | " + message);
	}
	public static final CompletableFuture<Message> sendStatusMessageInfo(MessageChannel channel, String message) {
		return sendMessage(channel, ":information_source: | " + message);
	}
	public static final CompletableFuture<Message> sendStatusMessageWarn(MessageChannel channel, String message) {
		return sendMessage(channel, ":warning: | " + message);
	}
	public static final CompletableFuture<Message> sendWhisper(final Long userId, final String message){
		return sendMessage(Constants.getClient().retrieveUserById(userId).complete().openPrivateChannel().complete(), message);
	}
	public static final CompletableFuture<Message> sendMessage(final MessageChannel channel, final String message){
		return channel.sendMessage(message).submit();
	}

	public static final CompletableFuture<Message> sendImage(final MessageChannel channel, final String imageURL){
		return sendEmbed(channel, new EmbedBuilder().setImage(imageURL));
	}
	public static final Message sendFile(final MessageChannel channel, String name, byte[] f) {
		long time = System.currentTimeMillis();
		while(true)
			try{
				return channel.sendFile(f, name).complete();
			} catch(Exception e){ 
				if(System.currentTimeMillis() - time > 30000)
					break;
				Constants.sleep(2000);
			}
		return null;
	}
	public static final CompletableFuture<Message> sendError(MessageChannel channel, String message) {
		return sendEmbed(channel, message, null); 
	}
	public static final CompletableFuture<Message> sendEmbed(final MessageChannel channel, String message, String emote) {
		EmbedBuilder builder = new EmbedBuilder();
		if(emote != null)
			builder.setThumbnail(emote);
		builder.setDescription(message);
		return sendEmbed(channel, builder);
	}
	public static final CompletableFuture<Message> sendEmbed(final MessageChannel channel, final EmbedBuilder embed){
		MessageEmbed eb = embed.build();
    	if(eb.getFooter() == null) {
    		String footer = "If you like the bot, consider becoming a Patron:" + System.lineSeparator() + "https://patreon.com/MateriaBot";
    		embed.setFooter(footer, ImageUtils.getEmoteClassByName("patreon").getImageUrl());
    		eb = embed.build();
    	}
		return channel.sendMessage(eb).submit();
	}

	public static final Message editMessage(Message message, String msg) {
		return message.editMessage(msg).complete();
	}
	public static final Message editMessage(Message message, EmbedBuilder embed) {
		MessageEmbed eb = embed.build();
		if(eb.getFooter() == null) {
    		String footer = "If you like the bot, consider becoming a Patron:" + System.lineSeparator() + "https://patreon.com/MateriaBot";
    		embed.setFooter(footer, ImageUtils.getEmoteClassByName("patreon").getImageUrl());
    		eb = embed.build();
    	}
		return message.editMessage(eb).complete();
	}

	public static final Message addReactions(Message message, String... reactions) {
		for(String emote : reactions)
			message.addReaction(ImageUtils.getEmoteClassByName(emote)).queue();
		return message;
	}

	public static void removeUserReactions(Message message, User user) {
		if(user == null)
			message.clearReactions().complete();
		else
			for(MessageReaction mr : message.getReactions())
				message.removeReaction(mr.getReactionEmote().getEmote(), user).queue();
	}

	public static void removeReaction(Message message, User user, Emote reaction) {
		if(reaction == null)
			message.clearReactions().queue();
		else if(user == null)
			message.removeReaction(reaction).queue();
		else
			message.removeReaction(reaction, user).queue();
	}
	public static void removeEmojiReaction(Message message, User user, String emoji) {
		if(emoji == null)
			message.clearReactions().queue();
		else if(user == null)
			message.removeReaction(emoji).queue();
		else
			message.removeReaction(emoji, user).queue();
	}

	public static void deleteMessage(Message m) {
		m.delete().queue();
	}
}