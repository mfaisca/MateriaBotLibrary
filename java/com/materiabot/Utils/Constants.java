package com.materiabot.Utils;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import com.materiabot.IO.SQL.SQLAccess;
import com.materiabot.commands._BaseCommand;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;

public class Constants {
	private static JDA client;
	public static final String DEFAULT_PREFIX;
	public static final HashMap<Long, String> PREFIX = new HashMap<Long, String>();
	public static final long QUETZ_ID = 141599746987917312L;
	public static final long INK_ID = 290867157435416577L;
	public static final long DREAMY_ID = 194476008395505664L;
	public static final Long MATERIABOT_SERVER_ID = 544340710862618624L;
	public static final List<_BaseCommand> COMMANDS = new CopyOnWriteArrayList<_BaseCommand>();
	public static final boolean DEBUG;

	static {
		DEBUG = isHeaven();
		String p = DEBUG ? "%" : "$";
		DEFAULT_PREFIX = p;
	}
	
	public static final boolean isHeaven() {
		try {
			return InetAddress.getLocalHost().getHostName().equalsIgnoreCase("HEAVEN");
		} catch (UnknownHostException e) {}
		return false;
	}
	
	public static JDA getClient() { return client; }
	public static boolean canDeleteMessages(Guild g) {
		return g.getMember(client.getSelfUser()).hasPermission(Permission.MESSAGE_MANAGE);
	}
	public static void setClient(JDA c) { client = c; }
	
	public static final void sleep(int sleep){
		try {
			Thread.sleep(sleep);
		} catch (InterruptedException e) {}
	}
	public static String getGuildPrefix(Guild g) {
		if(g == null || client.isUnavailable(g.getIdLong())) 
			return Constants.DEFAULT_PREFIX;
		if(!PREFIX.containsKey(g.getIdLong())) {
			String p = SQLAccess.getGuildPrefix(g);
			PREFIX.put(g.getIdLong(), p);
		}
		return PREFIX.get(g.getIdLong());
	}
}