package me.itzrex.custombans;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import org.apache.commons.io.FileUtils;

import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * 
 * @author itzRex
 * @deprecated
 */
@Deprecated
public class Msg {

    private static final YamlConfiguration messages = new YamlConfiguration();
    private static final File messagesFile = new File(CustomBans.getInstance().getDataFolder(), "messages.yml");

    @Deprecated
    public static void reload() {
        if (!messagesFile.exists()) {
            try {
                FileUtils.copyInputStreamToFile(CustomBans.getInstance().getResource("messages.yml"), messagesFile);
            } catch (IOException ex) {
                CustomBans.getInstance().getLogger().log(Level.WARNING, "Failed to save messages.yml file.", ex);
            }
        }
        
        try {
            messages.load(messagesFile);
        } catch (IOException | InvalidConfigurationException ex) {
            CustomBans.getInstance().getLogger().log(Level.WARNING, "Failed to load messages.yml", ex);
            try {
                messages.load(CustomBans.getInstance().getResource("messages.yml"));
            } catch (IOException | InvalidConfigurationException ex1) {
                CustomBans.getInstance().getLogger().log(Level.WARNING, "Failed to load default messages. Larry, what the hell?", ex1);
            }
        }
    }

    @Deprecated
    public static String get(String loc, String[] keys, String[] values) {
        String msg = messages.getString(loc);

        if (msg == null || msg.isEmpty()) {
            return "Unknown message key: " + loc;
        }

        if (keys != null && values != null) {
            if (keys.length != values.length) { //Dirty, but we don't want to break a ban or something.
                throw new IllegalArgumentException("Invalid message request. keys.length should equal values.length!");
            }

            for (int i = 0; i < keys.length; i++) {
                msg = msg.replace("{" + keys[i] + "}", values[i]); //I could do case insensitive, but nty.
            }
        }

        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    @Deprecated
    public static String get(String loc) {
        return get(loc, null, null);
    }

}
