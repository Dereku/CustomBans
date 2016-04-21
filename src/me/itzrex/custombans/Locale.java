package me.itzrex.custombans;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Level;
import org.bukkit.ChatColor;

/**
 * Класс локалей плагина CustomBans
 *
 * @version 1.0
 * @since 1.5
 * @author Dereku
 */
public final class Locale {

    private static final String NO_KEY_FOUND = "Key \"%s\" not found!";
    private final Properties messages = new Properties();
    private final HashMap<String, MessageFormat> messagesCache = new HashMap<>();
    private CustomBans plugin;
    private String prefix;

    public Locale(CustomBans pl, File messagesFile) {
        this.plugin = pl;
        this.prefix = ChatColor.translateAlternateColorCodes('&', pl.getConfig().getString("language.prefix")).trim();
        try (FileReader fr = new FileReader(messagesFile)) {
            this.messages.load(fr);
            this.plugin.getLogger().info(this.getLogMessage("locale-loaded"));
        } catch (IOException ex) {
            this.plugin.getLogger().log(Level.WARNING, "Failed to load locale " + messagesFile.getName(), ex);
        }
    }

    /**
     * Получение сообщения для логгироания.
     *
     * @param key ключ сообщения из локали
     * @return сообщение для вывода в лог
     */
    public String getLogMessage(String key) {
        return ChatColor.stripColor(
                this.getMessage(key)
        );
    }

    /**
     * Получение сообщения с аргументами для логгироания.
     *
     * @param key ключ сообщения
     * @param args аргументы сообщения
     * @return сообщение для вывода в лог
     */
    public String getLogMessage(String key, String... args) {
        return ChatColor.stripColor(
                this.getMessage(key, args)
        );
    }

    /**
     * Получение сообщения для отправки CommandSender
     *
     * @param key ключ сообщения
     * @return Сообщение для отправки CommandSender
     */
    public String getMessage(String key) {
        return this.messages.getProperty(key, String.format(NO_KEY_FOUND, key));
    }

    /**
     * Получение сообщения с аргументами для отправки CommandSebder
     *
     * @param key ключ сообщения
     * @param args аргменты сообщения
     * @return сообщение для отправки CommandSender
     */
    public String getMessage(String key, String... args) {
        String message = this.messages.getProperty(key);
        MessageFormat mf = this.messagesCache.get(message);
        if (mf == null) {
            mf = new MessageFormat(message);
        }
        this.messagesCache.put(message, mf);
        return mf.format(args);
    }

    /**
     * Получение сообщения с префиксом для отправки CommandSender
     *
     * @param key ключ сообщения
     * @return сообщения для отправки CommandSender
     */
    public String getPrefixiedMessage(String key) {
        if (this.prefix.equals("")) {
            return this.getMessage(key);
        }
        return String.format("%1s %2s", this.prefix, this.getMessage(key));
    }

    /**
     * Получение сообщения с префиксом и аргументами для отправки CommandSender
     *
     * @param key ключ сообщения
     * @param args аргументы сообщения
     * @return сообщения для отправки CommandSender
     */
    public String getPrefixiedMessage(String key, String... args) {
        if (this.prefix.equals("")) {
            return this.getMessage(key, args);
        }
        return String.format("%1s %2s", this.prefix, this.getMessage(key, args));
    }
}
