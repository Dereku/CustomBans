package me.itzrex.cbans;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class CustomBans extends JavaPlugin {

	public static CustomBans plugin;
	public static CustomBans geInstance(){
		return plugin;
	}
	public static File dataFile;
	public static File dataFile2;
	public static File players;
	public static YamlConfiguration dplayers;
	public static YamlConfiguration dconfig;
	public static YamlConfiguration dconfig2;
	public static String banned;
	public static String targetmsg;
	public static String kicked;
	public static String targetkmsg;
	public static String noperm;
	public static String prefix;
	public static String unbanned;
	
	@Override
	public void onEnable() {
		plugin = this;
		configInit();
		createFiles();
		getCommand("kick").setExecutor(new Kick());
		getCommand("ban").setExecutor(new Ban());
		getCommand("mute").setExecutor(new Mute());
		getCommand("tempmute").setExecutor(new TempMute());
		getCommand("unmute").setExecutor(new unMute());
		getCommand("custombans").setExecutor(new Maincmd());
		getCommand("checkban").setExecutor(new Checker());
		getCommand("unban").setExecutor(new unBan());
		getCommand("tempban").setExecutor(new TempBanner());
		getServer().getPluginManager().registerEvents(new PlayerListener(), this);
		getLogger().info("CustomBans v" + getDescription().getVersion() + " enabled!");
		getLogger().info("Bans data loaded. (" + dconfig.getStringList("banlist").size() + ")");
		getLogger().info("Mutes data loaded. (" + dconfig2.getStringList("mutelist").size() + ")");
	}
	
	public void configInit(){
		prefix = ChatColor.translateAlternateColorCodes('&', getConfig().getString("prefix"));
		banned = ChatColor.translateAlternateColorCodes('&', CustomBans.geInstance().getConfig().getString("messages.banned"));
		kicked = ChatColor.translateAlternateColorCodes('&', CustomBans.geInstance().getConfig().getString("messages.kicked"));
		targetkmsg = ChatColor.translateAlternateColorCodes('&', CustomBans.geInstance().getConfig().getString("messages.targetkmsg"));
		targetmsg = ChatColor.translateAlternateColorCodes('&', CustomBans.geInstance().getConfig().getString("messages.targetmsg"));
		noperm = ChatColor.translateAlternateColorCodes('&', CustomBans.geInstance().getConfig().getString("messages.noperm"));
		unbanned = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.unbanned"));
		saveDefaultConfig();
	}
	
	public void createFiles(){
		if(!new File(getDataFolder(), "players.yml").exists()){
			File playersd = new File(getDataFolder(), "players.yml");
			YamlConfiguration dplayer = YamlConfiguration.loadConfiguration(playersd);
			try {
				dplayer.save(playersd);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		players = new File(getDataFolder(), "players.yml");
		dplayers = YamlConfiguration.loadConfiguration(players);
		if(!new File(getDataFolder(), "bans.yml").exists()){
			File configFile = new File(getDataFolder(), "bans.yml");
			YamlConfiguration configuration = YamlConfiguration.loadConfiguration(configFile);
		      List<String> banlist = new ArrayList<String>();
		      configuration.set("banlist", banlist);
		    try {
				configuration.save(configFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	      dataFile = new File(getDataFolder(), "bans.yml");
	      dconfig = YamlConfiguration.loadConfiguration(dataFile);
	      if(!new File(getDataFolder(), "mutes.yml").exists()){
				File configFile = new File(getDataFolder(), "mutes.yml");
				YamlConfiguration configuration = YamlConfiguration.loadConfiguration(configFile);
			      List<String> mutelist = new ArrayList<String>();
			      configuration.set("mutelist", mutelist);
			    try {
					configuration.save(configFile);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		      dataFile2 = new File(getDataFolder(), "mutes.yml");
		      dconfig2 = YamlConfiguration.loadConfiguration(dataFile);
  }
}