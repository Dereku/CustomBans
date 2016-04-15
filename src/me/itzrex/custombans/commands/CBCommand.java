package me.itzrex.custombans.commands;

import me.itzrex.custombans.CustomBans;
import me.itzrex.custombans.Msg;

import java.io.File;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * Класс создан itzRex. Дата: 06.04.2016.
 */
public class CBCommand implements CommandExecutor {
    static String prefix = Msg.get("prefix");
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        CommandSender p = sender;
        if(args.length == 0){
            if(!p.hasPermission("cusombans.cbcmd")){
                p.sendMessage(Msg.get("prefix") + "§cНет прав.");
                return true;
            }
            p.sendMessage(prefix + "by §eitzRex §c§l" + CustomBans.getInstance().getDescription().getVersion());
            p.sendMessage("§a* §fПерезагрузка базы данных и конфига - /" + label + " reload");
            p.sendMessage("§a* §fСписок игроков с защитой от бана - /" + label + " whitelist");
            p.sendMessage("§a* §fСписок лимитов для банов/мутов - /" + label + " banlimit/mutelimit");
            return true;
        }
        if(args.length == 1){
            if(args[0].equalsIgnoreCase("reload")){
                if(!p.hasPermission("cusombans.cbcmd")){
                    p.sendMessage(Msg.get("prefix") + "§cНет прав.");
                    return true;
                }
                Msg.reload();
                CustomBans.getInstance().reload();
                CustomBans.getInstance().reloadConfig();
                CustomBans.getInstance().getBanManager().reload();
                p.sendMessage(prefix + "Конфиг, база данных, лимиты - перезагружены.");
                return true;
            }
            if(args[0].equalsIgnoreCase("whitelist")){
                if(!p.hasPermission("cusombans.cbcmd")){
                    p.sendMessage(Msg.get("prefix") + "§cНет прав.");
                    return true;
                }
                p.sendMessage(prefix + "§aСписок игроков с защитой от бана:");
                for(String s : CustomBans.getInstance().getBanManager().getWhitelist()){
                    p.sendMessage("§e- §a" + s);
                }
                return true;
            }
           if(args[0].equalsIgnoreCase("banlimit"));
           File configFile2 = new File(CustomBans.getInstance().getDataFolder(), "limits.yml");
           YamlConfiguration configuration2 = YamlConfiguration.loadConfiguration(configFile2);
           ConfigurationSection sec = configuration2.getConfigurationSection("bans");
           p.sendMessage(prefix + "§aСписок групп с лимитами банов:");
           for(String s : sec.getKeys(false)){
        	   p.sendMessage("§fГруппа: §a" + s + "§f. Лимит: §a" + sec.getString(s));
           }
           return true;
        }
        if(args[0].equalsIgnoreCase("mutelimit"));
        File configFile2 = new File(CustomBans.getInstance().getDataFolder(), "limits.yml");
        YamlConfiguration configuration2 = YamlConfiguration.loadConfiguration(configFile2);
        ConfigurationSection sec = configuration2.getConfigurationSection("mutes");
        p.sendMessage(prefix + "§aСписок групп с лимитами банов:");
        for(String s : sec.getKeys(false)){
     	   p.sendMessage("§fГруппа: §a" + s + "§f. Лимит: §a" + sec.getString(s));
        }
        return true;
    }
}
