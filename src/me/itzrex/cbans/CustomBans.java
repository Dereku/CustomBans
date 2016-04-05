package me.itzrex.cbans;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.net.ConnectException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import me.itzrex.cbans.db.Database;
import me.itzrex.cbans.db.Database.ConnectionException;
import me.itzrex.cbans.db.DatabaseCore;
import me.itzrex.cbans.db.DatabaseHelper;
import me.itzrex.cbans.db.MySQLCore;
import me.itzrex.cbans.managers.BanManager;
import me.itzrex.cbans.utils.Metrics;
import me.itzrex.cbans.utils.Metrics.Graph;

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
	public static File dataFile3;
	public static File players;
	public static YamlConfiguration dplayers;
	public static YamlConfiguration dconfig;
	public static YamlConfiguration dconfig2;
	public static YamlConfiguration dconfig3;
	public static String banned;
	public static String targetmsg;
	public static String kicked;
	public static String targetkmsg;
	public static String noperm;
	public static String prefix;
	public static String unbanned;
	public Database db;
	public BanManager manager;
	public boolean isMySQL = getConfig().getBoolean("mysql");
    public Metrics getMetrics(){ return metrics; }
    private Metrics metrics;

	
	/*
	 * Главный класс.
	 * CustomBans by @itzRex
	 * Open Source
	 */
	@Override
	public void onEnable() {
		plugin = this;
		//Загружаем конфиг
		saveDefaultConfig();
		prefix = ChatColor.translateAlternateColorCodes('&', getConfig().getString("prefix"));
		//Создаём файлы bans.yml, mutes.yml, players.yml
		if(getConfig().getBoolean("mysql")){
			getLogger().info("Using MySQL system...");
			DatabaseCore core;
			String user = getConfig().getString("connect.user");
			String pass = getConfig().getString("connect.pass");
			String host = getConfig().getString("connect.host");
			String name = getConfig().getString("connect.name");
			String port = getConfig().getString("connect.port");
			getLogger().info("Successfully connected to database!");
			core = new MySQLCore(host, user, pass, name, port);
			final boolean readOnly = getConfig().getBoolean("connect.readonly");
			try {
				this.db = new Database(core){
					@Override
					public void execute(String query, Object... objs){
						if(readOnly) return;
						else super.execute(query, objs);
					}
				};
			} catch (ConnectionException e1) {
				e1.printStackTrace();
				System.out.println("Failed to create connection to database. Disabling CustomBans :(");
				getServer().getPluginManager().disablePlugin(this);
				return;
			}
			try {
				DatabaseHelper.setup(db);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			manager = new BanManager(this);
			startMetrics();
			getLogger().info("================================================");
			getLogger().info("Author: itzRex");
			getLogger().info("Mutes data loaded (" + manager.mutes.size() + ")");
			getLogger().info("Bans data loaded (" + manager.bans.size() + ")");
			getLogger().info("================================================");
		} else {
			getLogger().info("Using YAML system...");
			createFiles();
			createpl();
			getLogger().info("================================================");
			getLogger().info("Author: itzRex");
			getLogger().info("Mutes data loaded (" + dconfig2.getStringList("mutelist").size() + ")");
			getLogger().info("Bans data loaded (" + dconfig.getStringList("banlist").size() + ")");
			getLogger().info("================================================");
		}
		//Достаём команды.;
		for(String s : getDescription().getCommands().keySet()){
			getLogger().info("Register command - /" + s);
		}
		getCommand("kick").setExecutor(new Kick());
		getCommand("ban").setExecutor(new Ban());
		getCommand("mute").setExecutor(new Mute());
		getCommand("unmute").setExecutor(new unMute());
		getCommand("custombans").setExecutor(new Maincmd());
		getCommand("unban").setExecutor(new unBan());
		getCommand("warn").setExecutor(new WarnCommand());
		//Регистрация событий
		getServer().getPluginManager().registerEvents(new PlayerListener(), this);
		//Вывод данных в консоль.
		getLogger().info("CustomBans v" + getDescription().getVersion() + " enabled.");
	}
	
	public void startMetrics(){
		try {
        	if(metrics != null) return; //Don't start two metrics. 
        	
        	metrics = new Metrics(this);
        	if(metrics.start() == false) return; //Metrics is opt-out.
        	
        	Graph bans = metrics.createGraph("Bans");
        	Graph mutes = metrics.createGraph("Mutes");
        	
        	bans.addPlotter(new Metrics.Plotter() {
				@Override
				public int getValue() {
					return getBanManager().bans.size();
				}
			});
        	
        	mutes.addPlotter(new Metrics.Plotter() {
				@Override
				public int getValue() {
					return getBanManager().mutes.size();
				}
			});
        	
        }
        catch(IOException e){
        	e.printStackTrace();
        	System.out.println("Metrics start failed");
        }
    }	
	public Database getDb(){
		return db;
	}
	public BanManager getBanManager(){
		return manager;
	}
	public  void createpl(){
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
	}
	//Создание файлов.
	public void createFiles(){
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
		      dconfig2 = YamlConfiguration.loadConfiguration(dataFile2);
	}
}