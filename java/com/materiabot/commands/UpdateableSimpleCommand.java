package com.materiabot.commands;
import java.sql.ResultSet;
import org.apache.commons.lang3.StringUtils;
import com.materiabot.IO.SQL.SQLAccess;
import com.materiabot.Utils.Constants;
import com.materiabot.Utils.LogUtils;
import com.materiabot.Utils.MessageUtils;
import com.materiabot.commands.general.HelpCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;

public class UpdateableSimpleCommand extends _BaseCommand{
	private final String help, reply;
	private final Long guildId;
	private final String ownerStr;
	
	public UpdateableSimpleCommand(final String command, final long guildId, final String reply, final String owner, final String help) {
		super(null, command.split(";;"));
		this.guildId = guildId == 0 ? null : new Long(guildId);
		this.reply = reply;
		this.help = help;
		ownerStr = owner;
	}

	@Override
	public boolean validateCommand(Message event) {
		if(super.validateCommand(event))
			return guildId == null || event.getGuild().getIdLong() == guildId.longValue();
		return false;
	}
	@Override
	public boolean validatePermission(Message event) {
		String message = event.getContentRaw();
		if(message.trim().contains(" "))
			return event.getAuthor().getId().equals(ownerStr) || event.getAuthor().getIdLong() == Constants.QUETZ_ID;
		return true;
	}

	@Override
	public void doStuff(Message message) {
		String text = null;
		try {
			String msg = message.getContentRaw().trim();
			if(msg.contains(" ")){
				msg = msg.substring(msg.indexOf(" ") + 1).trim();
				if(msg.equalsIgnoreCase("clear")){
					SQLAccess.executeInsert("UPDATE Commands SET data = NULL WHERE name = ?", triggerWords.stream().reduce((o1, o2) -> o1 + ";;" + o2).get());
					MessageUtils.sendMessage(message.getChannel(), "Cleared");
				}else if(msg.length() > 0){
					SQLAccess.executeInsert("UPDATE Commands SET data = ? WHERE name = ?", msg, triggerWords.stream().reduce((o1, o2) -> o1 + ";;" + o2).get());
					MessageUtils.sendMessage(message.getChannel(), "Updated");
				}else{
					MessageUtils.sendMessage(message.getChannel(), "Error reading line. Let Quetz know!");
				}
			}
			else{
				ResultSet rs = SQLAccess.executeSelect("SELECT data FROM Commands WHERE name LIKE ?", "%" + triggerWords.stream().reduce((o1, o2) -> o1 + ";;" + o2).get() + "%");
				if(!rs.next())
					MessageUtils.sendMessage(message.getChannel(), "Nothing has been set for this command yet!");
				else
					text = rs.getString("data").trim();
			}
		} catch (Exception e) {
			LogUtils.error(message, e.getMessage(), e);
			e.printStackTrace();
		}
		if(text == null)
			return;
		if(isImage()) {
			EmbedBuilder embed = new EmbedBuilder();
			embed.setImage(text);
			if(ownerStr != null && StringUtils.isNumeric(ownerStr)) {
				User owner = message.getJDA().retrieveUserById(ownerStr).complete();
				embed.setFooter("Credits to " + owner.getName() + "#" + owner.getDiscriminator(), owner.getAvatarUrl());
			}
			else if(ownerStr != null)
				embed.setFooter("Credits to " + ownerStr);
			MessageUtils.sendEmbed(message.getChannel(), embed);
		}else {
			if(ownerStr != null)
				if(StringUtils.isNumeric(ownerStr)) {
					User owner = message.getJDA().retrieveUserById(ownerStr).complete();
					text += System.lineSeparator() + "Credits to " + owner.getName() + "#" + owner.getDiscriminator();
				} else
					text += System.lineSeparator() + "Credits to " + ownerStr;
			MessageUtils.sendMessage(message.getChannel(), text);
		}
	}
	
	private boolean isImage() {
		String[] imageExtensions = new String[]{".png", ".jpg", ".jpeg", ".bmp", ".gif"};
		for(String a : imageExtensions)
			if(reply.toLowerCase().endsWith(a))
				return true;
		return false;
	}

	@Override
	public String help(HelpCommand.HELP_TYPE helpType) {
		return help;
	}
}