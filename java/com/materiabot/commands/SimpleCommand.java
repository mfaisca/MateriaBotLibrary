package com.materiabot.commands;
import java.util.Arrays;
import com.materiabot.Utils.Constants;
import com.materiabot.Utils.MessageUtils;
import com.materiabot.commands.general.HelpCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import org.apache.commons.lang3.text.WordUtils;

@SuppressWarnings("deprecation")
public class SimpleCommand extends _BaseCommand{
	private final String message;
	private final String helpShort;
	private final String helpLong;
	private final boolean removeMessage;
	private final Boolean isImage;
	private final Long ownerID;
	private String owner = null;

	public SimpleCommand(final String command, final String message, final String help) {
		this(Arrays.asList(command).toArray(new String[0]), null, false, message, help);
	}
	public SimpleCommand(final String[] commands, final String message, final String help) {
		this(commands, null, false, message, help);
	}
	
	public SimpleCommand(final String command, final String message, final String help, final boolean remove) {
		super(command);
		this.message = message.replace("\n", System.lineSeparator());
		helpShort = helpLong = help.replace("\n", System.lineSeparator());
		removeMessage = remove;
		isImage = false;
		ownerID = null;
	}

	public SimpleCommand(final String command, final Long ownerID, final boolean image, final String message, final String help) {
		super(command);
		this.message = message.replace("\n", System.lineSeparator());
		helpShort = helpLong = help.replace("\n", System.lineSeparator());
		removeMessage = false;
		isImage = image;
		this.ownerID = ownerID;
	}
	public SimpleCommand(final String command, final String owner, final boolean image, final String message, final String help) {
		super(command);
		this.message = message.replace("\n", System.lineSeparator());
		helpShort = helpLong = help.replace("\n", System.lineSeparator());
		removeMessage = false;
		isImage = image;
		this.ownerID = null;
		this.owner = owner;
	}
	public SimpleCommand(final String[] commands, final Long ownerID, final boolean image, final String message, final String help) {
		super(commands[0], Arrays.asList(commands).subList(1, commands.length).toArray(new String[0]));
		this.message = message.replace("\n", System.lineSeparator());
		helpShort = helpLong = help.replace("\n", System.lineSeparator());
		removeMessage = false;
		isImage = image;
		this.ownerID = ownerID;
	}

	@Override
	public void doStuff(final Message event) {
        if(removeMessage)
        	event.delete();
		if(isImage) {
			EmbedBuilder embed = new EmbedBuilder();
			embed.setImage(message);
			if(ownerID != null) {
				User u = Constants.getClient().getUserById(ownerID);
				embed.setFooter(u.getName() + "#" + u.getDiscriminator(), u.getAvatarUrl());
			}
			if(owner != null)
				embed.setFooter(owner);
			MessageUtils.sendEmbed(event.getChannel(), embed);
		}else {
			if(ownerID != null) {
				User u = Constants.getClient().getUserById(ownerID);
		        MessageUtils.sendMessage(event.getChannel(), message + System.lineSeparator() + "Credits to " + u.getName() + "#" + u.getDiscriminator() + " on Discord.");
			}
			else if(owner != null)
		        MessageUtils.sendMessage(event.getChannel(), message + System.lineSeparator() + "Credits to " + owner + ".");
			else
		        MessageUtils.sendMessage(event.getChannel(), message);
		}
	}
	
	@Override
	public String help(HelpCommand.HELP_TYPE helpType) {
		String ret = "";
		if(HelpCommand.HELP_TYPE.SHORT.equals(helpType)){
			ret += helpShort;
		}else{
			ret += "```md" + System.lineSeparator();
			ret += WordUtils.capitalize(getCommand()) + " Command" + System.lineSeparator();
			ret += "===============" + System.lineSeparator();
			ret += help(HelpCommand.HELP_TYPE.SHORT) + System.lineSeparator();
			ret += System.lineSeparator();
			ret += "[*][Usage][*]" + System.lineSeparator();
			ret += "* $" + getCommand() + System.lineSeparator();
			ret += ">    " + helpLong + System.lineSeparator();
			ret += "```";
		}
		return ret;
	}
}
