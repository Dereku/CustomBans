package me.itzrex.custombans.api;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.craftbukkit.libs.jline.internal.Log.Level;
import org.bukkit.entity.Player;

import com.avaje.ebean.LogLevel;

import me.itzrex.custombans.CustomBans;
import me.itzrex.custombans.Msg;
import me.itzrex.custombans.managers.Ban;
import me.itzrex.custombans.managers.BanManager;
import me.itzrex.custombans.managers.Mute;
import me.itzrex.custombans.managers.TempBan;

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
	
	public static void unban(String name){
		Ban ban = getBanManager().getBan(name);
		if(ban != null){
			getBanManager().unban(name);
		} else {
			Bukkit.getLogger().info(Level.ERROR + "Player not banned.");
		}
	}
	
	public static void ban(String name, String reason, String banner){
		Ban ban = getBanManager().getBan(name);
		if(ban != null){
			getBanManager().ban(name, reason, banner);
		}
	}
	
	public static void tempban(String name, String reason, String banner, long expires){
		getBanManager().tempban(name, reason, banner, expires);
	}
	
	public static void mute(String name, String reason, String banner){
		Mute mute = getBanManager().getMute(name);
		if(mute != null){
			getBanManager().mute(name, banner, reason);
	   } else {
			Bukkit.getLogger().info(Level.ERROR + "Player not banned.");
		}
	}
	public static void unmute(String name){
		Mute mute = getBanManager().getMute(name);
		if(mute != null){
			getBanManager().unmute(name);
		 } else {
		   Bukkit.getLogger().info(Level.ERROR + "Player not banned.");
		}
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
		Ban ban = getBanManager().getBan(name);
		if(ban != null){
			return false;
		} else {
			return true;
		}
	}
}
