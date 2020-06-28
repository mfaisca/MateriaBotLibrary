package com.materiabot.commands.general;
import com.materiabot._Library;
import com.materiabot.Utils.Constants;
import com.materiabot.Utils.MessageUtils;
import com.materiabot.commands._BaseCommand;
import net.dv8tion.jda.api.entities.Message;

public class AdminCommand extends _BaseCommand{
	public AdminCommand() {
		super("admin");
	}

	@Override
	public void doStuff(final Message event) {
		try {
			String[] msg = event.getContentDisplay().split(" ");
			if(msg.length == 1) {
				MessageUtils.sendMessage(event.getChannel(), "$admin xxx");
				return;
			}
			switch(msg[1].toLowerCase()) {
				case "reload":
				case "reset": reloadPlugins(); break;
				case "ttpatreon": PatreonCommand.joinServerTonberryTroupeUpdate(); break;
			}
			MessageUtils.sendMessage(event.getChannel(), "Done");
		} catch(Exception e) {
			MessageUtils.sendMessage(event.getChannel(), "You fucked up");
		}
	}
	
	private void reloadPlugins() {
		_Library.reset();
	}

	@Override
	public boolean validatePermission(Message event) {
		return event.getAuthor().getIdLong() == Constants.QUETZ_ID;
	}
}