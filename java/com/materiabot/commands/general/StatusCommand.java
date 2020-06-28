package com.materiabot.commands.general;
import com.materiabot.Utils.Constants;
import com.materiabot.Utils.MessageUtils;
import com.materiabot.commands._BaseCommand;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Activity.ActivityType;
import net.dv8tion.jda.api.entities.Message;

public class StatusCommand extends _BaseCommand{
	public StatusCommand() {
		super("status");
	}

	@Override
	public void doStuff(final Message event) {
		try {
			String[] msg = event.getContentDisplay().substring(event.getContentDisplay().indexOf(" ")+1).split(";;");
			if(msg.length == 1) {
				MessageUtils.sendMessage(event.getChannel(), "$status [default/tcc/playing/watching/streaming/listening];;Opera Omnia;;StreamURL");
				return;
			}
			Activity a = null;
			switch(msg[0].toLowerCase()) {
				case "default": a = Activity.playing("Opera Omnia"); break;
				case "tcc": a = Activity.streaming("TCC Podcast", "https://www.twitch.tv/thecrystalchronicles/"); break;
				case "playing": a = Activity.playing(msg[1]); break;
				case "watching": a = Activity.watching(msg[1]); break;
				case "streaming": if(Activity.isValidStreamingUrl(msg[2])) a = Activity.streaming(msg[1], msg[2]); else a = null; break;
				case "listening": a = Activity.listening(msg[1]); break;
			}
			Constants.getClient().getPresence().setPresence(OnlineStatus.ONLINE, a == null ? Activity.of(ActivityType.DEFAULT, "Opera Omnia") : a);
			MessageUtils.sendMessage(event.getChannel(), "Updated");
		} catch(Exception e) {
			MessageUtils.sendMessage(event.getChannel(), "You fucked up");
		}
	}

	@Override
	public boolean validatePermission(Message event) {
		return event.getAuthor().getIdLong() == Constants.QUETZ_ID;
	}
}