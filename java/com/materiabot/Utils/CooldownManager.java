package com.materiabot.Utils;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.materiabot.commands.general.PatreonCommand;
import net.dv8tion.jda.api.entities.User;

public class CooldownManager {
	public static enum Type{
		REGULAR(5, 2), PULL(30, 20), GOLDPULL(600, 60), CLEVERBOT(30, 2);
		private int regularCD, patreonCD;
		
		private Type(int rCD, int pCD) { regularCD = rCD; patreonCD = pCD; }
		
		public int getRegularCooldown() { return regularCD; }
		public int getPatreonCooldown() { return patreonCD; }
	}
	
	private static final CooldownManager SINGLETON = new CooldownManager();
	public static final LoadingCache<String, HashMap<Type, Long>> USER_COOLDOWNS = 
			CacheBuilder.newBuilder()
			.expireAfterAccess(15, TimeUnit.MINUTES).build(new CacheLoader<String, HashMap<Type, Long>>(){
				public HashMap<Type, Long> load(String key) throws Exception { return new HashMap<Type, Long>(); }
			});
    
	private CooldownManager() {}
	public CooldownManager get() { return SINGLETON; }

	public static void clearCooldowns(User user, Type type) {
		USER_COOLDOWNS.getUnchecked(user.getId()).remove(type);
	}
	public static int userCooldown(User user) {
		return userCooldown(user, Type.REGULAR);
	}
	public static int userCooldown(User user, Type type) {
		if(user.getIdLong() == Constants.QUETZ_ID) return -1;
		HashMap<Type, Long> userCDs = USER_COOLDOWNS.getUnchecked(user.getId());
		if(userCDs.containsKey(type)) {
    		long cooldown = userCDs.get(type);
    		if(cooldown > System.currentTimeMillis())
    			return (int) (cooldown - System.currentTimeMillis());
		}
		userCDs.put(type, System.currentTimeMillis() + ((PatreonCommand.isUserPatreon(user) ? 
															type.getPatreonCooldown() : 
															type.getRegularCooldown()
														) * 1000));
		return -1;
	}
}