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
	public static File players;
	public static YamlConfiguration dplayers;
	public static YamlConfiguration dconfig;
	public static String banned;
	public static String targetmsg;
	public static String noperm;
	public static String prefix;
	public static String unbanned;
	
	@Override
	public void onEnable() {
		try {
		plugin = this;
		configInit();
		createFiles();
		getCommand("ban").setExecutor(new Ban());
		getCommand("custombans").setExecutor(new Maincmd());
		getCommand("checkban").setExecutor(new Checker());
		getCommand("unban").setExecutor(new unBan());
		getCommand("tempban").setExecutor(new TempBanner());
		getServer().getPluginManager().registerEvents(new PlayerListener(), this);
		getLogger().info("CustomBans v" + getDescription().getVersion() + " enabled!");
		getLogger().info("Bans data loaded. (" + dconfig.getStringList("banlist").size() + ")");
		} catch(NullPointerException e){
			createFiles();
		      dataFile = new File(getDataFolder(), "bans.yml");
		      dconfig = YamlConfiguration.loadConfiguration(dataFile);
			
		}
	}
	
	public void configInit(){
		saveDefaultConfig();
		prefix = ChatColor.translateAlternateColorCodes('&', getConfig().getString("prefix"));
		banned = ChatColor.translateAlternateColorCodes('&', CustomBans.geInstance().getConfig().getString("messages.banned"));
		targetmsg = ChatColor.translateAlternateColorCodes('&', CustomBans.geInstance().getConfig().getString("messages.targetmsg"));
		noperm = ChatColor.translateAlternateColorCodes('&', CustomBans.geInstance().getConfig().getString("messages.noperm"));
		unbanned = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.unbanned"));
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
		      dataFile = new File(getDataFolder(), "bans.yml");
		      dconfig = YamlConfiguration.loadConfiguration(dataFile);
	}
  }
}