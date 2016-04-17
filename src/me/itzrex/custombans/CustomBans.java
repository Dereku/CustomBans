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
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.MetricsLite;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import org.apache.commons.io.FileUtils;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * Класс создан itzRex. Дата: 06.04.2016.
 */
public class CustomBans extends JavaPlugin {

    public static String defaultreason = "Не указана";
    private static CustomBans instance;

    public static CustomBans getInstance() {
        return instance;
    }

    private final YamlConfiguration limitYaml = new YamlConfiguration();
    private Database db;
    private BanManager manager;
    private File limitFile;

    @Override
    public void onEnable() {
        instance = this; //TODO: Избавиться от этого
        this.saveDefaultConfig();
        this.limitFile = new File(this.getDataFolder(), "limits.yml");
        ConfigurationSection sec = getConfig().getConfigurationSection("mysql");
        DatabaseCore dCore;
        if (this.getConfig().getBoolean("mysql.enable")) {
            this.getLogger().info("Using MySQL...");
            String host = sec.getString("host");
            String user = sec.getString("user");
            String pass = sec.getString("pass");
            String name = sec.getString("name");
            String port = sec.getString("port");
            dCore = new MySQLCore(host, user, pass, name, port);
        } else {
            this.getLogger().info("Using SQLite...");
            dCore = new SQLiteCore(new File(getDataFolder(), "bans.db"));
        }
        try {
            this.db = new Database(dCore);
        } catch (Database.ConnectionException e) {
            this.getLogger().info("Failed connection to database. Disabling CustomBans :(");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        try {
            new MetricsLite(this).start();
        } catch (IOException e) {
            // Metrics failed.
        }
        Msg.reload();
        this.manager = new BanManager(this);
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
        loadLimits();
    }

    public PermissionGroup getGroup(Player player) {
        PermissionGroup[] groups = PermissionsEx.getUser(player).getGroups();
        if (groups.length == 0) {
            return PermissionsEx.getPermissionManager().getDefaultGroups(player.getLocation().getWorld().getName()).get(0);
        }
        return groups[0];
    }

    public void loadLimits() {
        if (!this.limitFile.exists()) {
            try {
                FileUtils.copyInputStreamToFile(this.getResource("limits.yml"), this.limitFile);
            } catch (IOException ex) {
                this.getLogger().log(Level.WARNING, "Failed to save limits.yml file", ex);
            }
        }

        try {
            this.limitYaml.load(this.limitFile);
        } catch (IOException | InvalidConfigurationException ex) {
            this.getLogger().log(Level.WARNING, "Failed to load limits.yml", ex);
            try {
                this.limitYaml.load(this.getResource("limits.yml"));
            } catch (IOException | InvalidConfigurationException ex1) {
                this.getLogger().log(Level.WARNING, "Failed to load default limits. Larry, what the hell?", ex1);
            }
        }
    }

    public FileConfiguration getLimits() {
        return this.limitYaml;
    }

    public Database getBansDatabase() {
        return db;
    }

    public BanManager getBanManager() {
        return manager;
    }
}
