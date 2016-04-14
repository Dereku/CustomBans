package me.itzrex.custombans;

import me.itzrex.custombans.commands.*;
import me.itzrex.custombans.db.Database;
import me.itzrex.custombans.db.DatabaseCore;
import me.itzrex.custombans.db.MySQLCore;
import me.itzrex.custombans.db.SQLiteCore;
import me.itzrex.custombans.listeners.PlayerListener;
import me.itzrex.custombans.managers.BanManager;
import ru.tehkode.permissions.PermissionGroup;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.MetricsLite;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Класс создан itzRex. Дата: 06.04.2016.
 */
public class CustomBans extends JavaPlugin {

    public static CustomBans instance;
    public static CustomBans getInstance(){
        return instance;
    }
    public static String defaultreason = "Не указана";
    public boolean isMySQL = getConfig().getBoolean("mysql.enable");
    public Database db;
    private BanManager manager;
    public static File messageFile;
    public File limitFile;
    public YamlConfiguration limitYaml;
    public static FileConfiguration messageConfig;
    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        DatabaseCore dCore;
        ConfigurationSection sec = getConfig().getConfigurationSection("mysql");
        if(isMySQL){
            Bukkit.getLogger().info("[CustomBans] Using MySQL...");
            String host = sec.getString("host");
            String user = sec.getString("user");
            String pass = sec.getString("pass");
            String name = sec.getString("name");
            String port = sec.getString("port");
            dCore = new MySQLCore(host, user, pass, name, port);
        } else {
            Bukkit.getLogger().info("[CustomBans] Using SQLite...");
            dCore = new SQLiteCore(new File(getDataFolder(), "bans.db"));
        }
        try {
            this.db = new Database(dCore);
        } catch(Database.ConnectionException e){
            Bukkit.getLogger().info("Failed connection to database. Disabling CustomBans :(");
            Bukkit.getPluginManager().disablePlugin(this);
        }
        try {
            new MetricsLite(this).start();
        } catch (IOException e) {
            // Metrics failed.
        }
        Msg.reload();
        manager = new BanManager(this);
        getCommand("custombans").setExecutor(new CBCommand());
        getCommand("ban").setExecutor(new BanCommand());
        getCommand("unban").setExecutor(new unBanCommand());
        getCommand("kick").setExecutor(new KickCommand());
        getCommand("mute").setExecutor(new MuteCommand());
        getCommand("unmute").setExecutor(new unMuteCommand());
        getCommand("tempban").setExecutor(new TempBanCommand());
        getCommand("tempmute").setExecutor(new TempMuteCommand());
        getCommand("banip").setExecutor(new IPBanCommand());
        getCommand("unbanip").setExecutor(new unBanIPCommand());
        getCommand("baninfo").setExecutor(new BanInfoCommand());
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        reload();
    }
    
    public PermissionGroup getGroup(Player player)
    {
      PermissionGroup[] groups = PermissionsEx.getUser(player).getGroups();
      if (groups.length == 0) {
        return (PermissionGroup)PermissionsEx.getPermissionManager().getDefaultGroups(player.getLocation().getWorld().getName()).get(0);
      }
      return groups[0];
    }
        
    public static void reload(){
        File f = new File(CustomBans.getInstance().getDataFolder(), "limits.yml");
        YamlConfiguration cfg = new YamlConfiguration();
        try{
            YamlConfiguration defaults = new YamlConfiguration();
            InputStream in = CustomBans.getInstance().getResource("limits.yml");
            defaults.load(in);
            in.close();

            if(f.exists()){
                cfg.load(f); //If the existing message file exists, load it as well.
            }
            else{
                //Save the file to disk if the messages.yml file doesn't exist yet.
                FileOutputStream out = new FileOutputStream(f);
                in = CustomBans.getInstance().getResource("limits.yml");
                byte[] buffer = new byte[1024];
                int len = in.read(buffer);
                while (len != -1) {
                    out.write(buffer, 0, len);
                    len = in.read(buffer);
                }
                in.close();
                out.close();
            }
            cfg.setDefaults(defaults);
        } catch(FileNotFoundException e){
            e.printStackTrace();
        } catch(InvalidConfigurationException e){
            e.printStackTrace();
            System.out.println("Invalid messages.yml config. Using defaults.");
            try {
                cfg.load(CustomBans.getInstance().getResource("limits.yml"));
            } catch (Exception ex){
                ex.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public Database getDb(){
        return db;
    }
    public BanManager getBanManager(){
        return manager;
    }
}
