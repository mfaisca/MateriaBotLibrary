package com.materiabot.Utils;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import com.materiabot.IO.SQL.SQLAccess;
import Shared.BotException;
import net.dv8tion.jda.api.entities.Message;

public class LogUtils{
	private static class SQLHandler extends Handler{
		@Override
		public void publish(LogRecord arg0) {
			try {
				SQLAccess.executeInsert("INSERT INTO Error_Logs(messageId, level, myMessage, logMessage) VALUES (?, ?, ?, ?)", arg0.getParameters()[0], arg0.getLevel().intValue(), arg0.getParameters()[1], arg0.getParameters()[2]);
			} catch (BotException e) {
				System.out.println("Error writing to DB");
			}
		}
		@Override public void flush() {}
		@Override public void close() throws SecurityException {}
	}

	private static Logger log;

	static {
		log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME); //TODO Make lots of logs everywhere
		log.setLevel(Level.WARNING); 					   //Make a command that allows changing logging levels dynamically
		try {
			FileHandler fileTxt = new FileHandler("logger.txt");
			fileTxt.setFormatter(new SimpleFormatter());
			log.addHandler(fileTxt);
			log.addHandler(new SQLHandler());
		} catch (SecurityException | IOException e) {
			log = null;
			System.out.println("ERROR OPENING LOG");
		}
	}

	public static void error(Message event, String msg){
		log(event, Level.SEVERE, msg, null);
	}
	public static void warn(Message event, String msg){
		log(event, Level.WARNING, msg, null);
	}
	public static void info(Message event, String msg){
		log(event, Level.INFO, msg, null);
	}
	public static void log(Message event, String msg){
		log(event, Level.FINE, msg, null);
	}
	public static void error(Message event, String msg, Throwable e){
		log(event, Level.SEVERE, msg, e);
	}
	public static void warn(Message event, String msg, Throwable e){
		log(event, Level.WARNING, msg, e);
	}
	public static void info(Message event, String msg, Throwable e){
		log(event, Level.INFO, msg, e);
	}
	public static void log(Message event, String msg, Throwable e){
		log(event, Level.FINE, msg, e);
	}
	private static void log(Message event, Level level, String msg, Throwable e) {
		if(log == null || event == null) {
			System.out.println("ERROR LOGGING - PLEASE FIX");
			return;
		}
		Object[] args = new Object[3];
		args[0] = event.getIdLong();
		args[1] = msg;
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
		args[2] = e == null ? null : e.getMessage() + System.lineSeparator() + sw.toString();
		log.log(level, msg, args);
	}
}