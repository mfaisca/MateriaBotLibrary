package com.materiabot.commands;
import java.util.List;

import com.materiabot.Utils.Constants;
import com.materiabot.Utils.CooldownManager;
import com.materiabot.commands.general.HelpCommand;
import java.util.LinkedList;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;

public abstract class _BaseCommand{	
	//https://old.reddit.com/r/DissidiaFFOO/comments/gfj0nh/draw_probabilities_in_the_btld_era/
	protected List<String> triggerWords = new LinkedList<String>();

	public String getCommand() { return triggerWords.get(0); }
	public List<String> getTriggerWords() { return triggerWords; }

	protected _BaseCommand(String keyword, String... keywords) {
		if(keyword != null)
			triggerWords.add(keyword);
		for(String k : keywords) triggerWords.add(k);
	}
	
	public boolean validateCommand(final Message message) {
		if(message.getContentRaw().isEmpty()) return false;
		String prefix = message.isFromGuild() ? Constants.getGuildPrefix(message.getGuild()) : Constants.DEFAULT_PREFIX;
		String command = message.getContentRaw().toLowerCase().substring(prefix.length());
		command = command.substring(0, command.indexOf(" ") == -1 ? command.length() : command.indexOf(" "));
		return message.getContentRaw().startsWith(prefix) && triggerWords.contains(command);
	}
	
	public boolean validateReaction(final Message message) { return false; }
	
	public boolean validatePermission(Message message) { return true; }
	public CooldownManager.Type getCooldown(Message message) { return CooldownManager.Type.REGULAR; }

	public void doAddReactionStuff(MessageReactionAddEvent event) {}
	public void doRemoveReactionStuff(MessageReactionRemoveEvent event) {}
	public abstract void doStuff(Message message);
	
	public String help(HelpCommand.HELP_TYPE helpType) { return null; }
}