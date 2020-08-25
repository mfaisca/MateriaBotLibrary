package com.materiabot.commands.general;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.lang3.text.WordUtils;
import com.materiabot.Utils.Constants;
import com.materiabot.Utils.MessageUtils;
import com.materiabot.commands._BaseCommand;
import Shared.BotException;
import net.dv8tion.jda.api.entities.Message;

@SuppressWarnings("deprecation")
public class HelpCommand extends _BaseCommand{	
	public static enum HELP_TYPE{
		SHORT("short", "s"), LONG("long", "l", "detailed", "d"), SHOW("show");
		private List<String> parses = new LinkedList<String>();
		
		private HELP_TYPE(String... strs) {
			parses.addAll(Arrays.asList(strs));
		}
		
		public static HELP_TYPE parse(String s) {
			if(SHORT.parses.contains(s.toLowerCase()))
				return SHORT;
			if(LONG.parses.contains(s.toLowerCase()))
				return LONG;
			return SHORT;
		}
	}

	public HelpCommand() {
		super("help", "commands");
	}

	@Override
	public void doStuff(final Message event) {
		try {
			String msg = event.getContentDisplay();
			if(!msg.contains(" "))
				showAllCommands(event);
			else {
				msg = msg.substring(msg.indexOf(" ") + 1).trim();
				showSpecificHelp(event, msg);
			}
		} catch (BotException e) {
			e.printStackTrace();
		}
	}

	private void showAllCommands(final Message event) throws BotException {
		String str = "";
		str += "```md" + System.lineSeparator();
		str += "List of Command" + System.lineSeparator();
		str += "===============" + System.lineSeparator();
		str += System.lineSeparator();
		for(_BaseCommand command : Constants.COMMANDS) {
			if(command.help(HELP_TYPE.SHOW) == null) continue;
			if(command.help(HELP_TYPE.SHORT) == null) continue;
			str += "* " + WordUtils.capitalize(command.getCommand()) + " Command" + System.lineSeparator();
			str += ">    " + command.help(HELP_TYPE.SHORT) + System.lineSeparator();
		}
		str += "```";
		MessageUtils.sendMessage(event.getChannel(), str);
	}
	
	private void showSpecificHelp(final Message event, String command) throws BotException {
		for(_BaseCommand command2 : Constants.COMMANDS) 
			if(command2.getTriggerWords().contains(command)) {
				if(command2.help(HELP_TYPE.LONG) != null)
					MessageUtils.sendMessage(event.getChannel(), command2.help(HELP_TYPE.LONG));
				return;
			}
	}
	
	@Override
	public String help(HELP_TYPE helpType) {
		String ret = "";
		if(HELP_TYPE.SHORT.equals(helpType)){
			ret += "Shows the help information for commands";
		}else{
			ret += "```md" + System.lineSeparator();
			ret += "Help Command" + System.lineSeparator();
			ret += "===============" + System.lineSeparator();
			ret += help(HELP_TYPE.SHORT) + System.lineSeparator();
			ret += System.lineSeparator();
			ret += "[*][Usage][*]" + System.lineSeparator();
			ret += ">    Did you really ask for the help for the help command?" + System.lineSeparator();
			ret += "```";
		}
		return ret;
	}
}