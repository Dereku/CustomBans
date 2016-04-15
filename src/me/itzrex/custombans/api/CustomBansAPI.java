package me.itzrex.custombans.api;

import java.util.logging.Level;
import org.bukkit.Bukkit;

import me.itzrex.custombans.CustomBans;
import me.itzrex.custombans.Msg;
import me.itzrex.custombans.managers.Ban;
import me.itzrex.custombans.managers.BanManager;
import me.itzrex.custombans.managers.Mute;

public class CustomBansAPI {

	public static CustomBans getPlugin(){
		return CustomBans.getInstance();
	}
	public static BanManager getBanManager(){
		return getPlugin().getBanManager();
	}
	
	public static String getReason(String name){
		Ban ban = getBanManager().getBan(name);
		String reason = ban.getReason();	
		return reason;
		
	}
	
	public static boolean unban(String name){
		Ban ban = getBanManager().getBan(name);
		if(ban != null){
			getBanManager().unban(name);
		} else {
			Bukkit.getLogger().log(Level.WARNING, "Player not banned.");
		}
		return ban != null;
	}
	
	public static boolean ban(String name, String reason, String banner){
		Ban ban = getBanManager().getBan(name);
		if(ban != null){
			getBanManager().ban(name, reason, banner);
		}
 		return ban != null;
	}
	
	public static void tempban(String name, String reason, String banner, long expires){
		getBanManager().tempban(name, reason, banner, expires);
	}
	
	public static boolean mute(String name, String reason, String banner){
		Mute mute = getBanManager().getMute(name);
		if(mute != null){
			getBanManager().mute(name, banner, reason);
		} else {
			Bukkit.getLogger().log(Level.WARNING, "Player not banned.");
		}
		return mute != null;
	}
	public static boolean unmute(String name){
		Mute mute = getBanManager().getMute(name);
		if(mute != null){
			getBanManager().unmute(name);
		 } else {
		   Bukkit.getLogger().log(Level.WARNING, "Player not banned.");
		}
		return mute != null;
	}
	
	public static void tempmute(String name, String reason, String banner, long expires){
		getBanManager().tempmute(name, banner, reason, expires);
	}
	
	public static void reloadPlugin(){
		getBanManager().reload();
		Msg.reload();
		getPlugin().reload();
	}
	
	public static void setWhitelisted(String name, boolean white){
		getBanManager().setWhitelisted(name, white);
	}
	public static boolean isBanned(String name){
		return getBanManager().getBan(name) != null;
	}
}
