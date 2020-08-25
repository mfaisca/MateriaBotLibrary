package com.materiabot.commands.general;
import com.materiabot.Utils.Constants;
import com.materiabot.Utils.MessageUtils;
import com.materiabot.commands._BaseCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;

public class AboutCommand extends _BaseCommand{	
	public AboutCommand() {
		super("about", "credits", "credit", "author", "discord", "server", "thanks");
	}

	@Override
	public void doStuff(final Message event) {
		EmbedBuilder builder = new EmbedBuilder();
		builder.setAuthor("About MateriaBot", "https://discord.gg/XCTC7jY", event.getJDA().getSelfUser().getAvatarUrl());
		builder.addField("Developer", 
				"**Quetz** - Owner and developer of [MateriaBot](https://discord.gg/XCTC7jY)", false);
		builder.addField("Main Contributors", 
				"**Rem** - Owner of [DissidiaDB](https://dissidiadb.com/) - For giving all the information that the bot is based on, the bot wouldn't be as it is now without him" + System.lineSeparator() +
				"**Safeena** - [Safeena Spreadsheet](https://docs.google.com/spreadsheets/d/1T4_urW_OLF754oWGZ1SxL99cRpA_-fcGE_Q75OFJXZY) - Even though the bot no longer is based on this info, this was used for a long time and was invaluable to the bot" + System.lineSeparator() + 
				"**Dreamy** - Character Images and Emotes - Friend and designer of the edited character images and most of the bot's custom emotes" + System.lineSeparator() + 
				"", false);
		builder.addField("Friends & Other Contributors", 
				"**Tonberry Troupe** - [Infographs and more](https://www.tonberrytroupe.com/) - Testers of the bot during development, did a lot of ideas bouncing on them to figure out the best way to present information" + System.lineSeparator() + 
				//"**Caius**, **Shinri**, **Eons** - Monster Info - These are the guys that provide all the information regarding the monsters" + System.lineSeparator() + 
				"**Moyama** - Schedule Updating - Administrator of [Maincord](http://discord.gg/dffoo), he's usually the person that updates and adds all the new content to the $schedule" + System.lineSeparator() + 
				"", false);
		builder.addField("Friends & Other Contributors", 
				"**The Crystal Chronicles** - [Podcast](https://thecrystalchronicles.simplecast.com/)" + System.lineSeparator() + 
				"**Macnol** - [Call to Arms and more @ DissidiaInfo](http://dissidiainfo.com/)" + System.lineSeparator() + 
				"**Black Cloud** - [DE Chaos Rosters](https://docs.google.com/spreadsheets/d/1Lz1uLCTVtkGjqrltqqPJtGoBy9C3vjC4YxJ-w6o3FRE)" + System.lineSeparator() + 
				"**[Vash1306](https://old.reddit.com/user/Vash1306/submitted/)** - \"$timeline\" First Design" + System.lineSeparator() +
				"**[JakeMattAntonio](https://old.reddit.com/user/JakeMattAntonio/submitted/)** - \"$timeline\" Second Design" + System.lineSeparator() +
				"**Falen** - [Visual Character Library](http://dissidia-france.com/discord/)" + System.lineSeparator() +
				"**Animagnam/dommyc** - [OO Tracker](https://ootracker.com/)" + System.lineSeparator() +
				"", false);
		builder.addField("Special Mentions", "All the patrons that monetarily support the bot's cost through [Patreon](https://www.patreon.com/MateriaBot)! Check them out with $patreon", false);
		builder.setFooter("");
		MessageUtils.sendEmbed(event.getChannel(), builder);
	}
	
	@Override
	public String help(HelpCommand.HELP_TYPE helpType) {
		String ret = "";
		if(HelpCommand.HELP_TYPE.SHORT.equals(helpType)){
			ret += "Shows info about who created this bot.";
		}else{
			ret += "```md" + System.lineSeparator();
			ret += "Author Command" + System.lineSeparator();
			ret += "===============" + System.lineSeparator();
			ret += help(HelpCommand.HELP_TYPE.SHORT) + System.lineSeparator();
			ret += System.lineSeparator();
			ret += "[*][Usage][*]" + System.lineSeparator();
			ret += "* " + Constants.DEFAULT_PREFIX + "about" + System.lineSeparator();
			ret += ">    Pretty obvious, don't you think?" + System.lineSeparator();
			ret += "```";
		}
		return ret;
	}
}