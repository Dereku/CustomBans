package me.itzrex.custombans.commands;

import me.itzrex.custombans.CustomBans;
import me.itzrex.custombans.Msg;
import me.itzrex.custombans.util.Util;
import ru.tehkode.permissions.PermissionGroup;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import java.io.File;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

/**
 * Класс создан itzRex. Дата: 06.04.2016.
 */
public class MuteCommand implements CommandExecutor {
    public static String prefix = Msg.get("prefix");
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        CommandSender p = sender;
        if(args.length == 0){
            if(!p.hasPermission("custombans.mute")){
                p.sendMessage(prefix + "§cНет прав");
                return true;
            }
            p.sendMessage(prefix + "§aИспользуйте - §7/mute [ник] [причина]");
            return true;
        }
        if(args.length > 0){
            String name = args[0];
            if(!(sender instanceof Player)){
                boolean silent = Util.isSilent(args);
                String reason = Util.buildReason(args);
                String banner = Util.getName(sender);
                CustomBans.getInstance().getBanManager().mute(name, banner, reason);
                String message = Msg.get("prefix") + Msg.get("messages.muted", new String[] {"admin", "name", "reason"}, new String[] {banner, name, reason});
                CustomBans.getInstance().getBanManager().announce(message, silent, sender);
                return true;
            }
            if(CustomBans.getInstance().getBanManager().isWhitelisted(args[0])){
                p.sendMessage(prefix + "§cИгрок защищён от мута.");
                return true;
            }
            File configFile2 = new File(CustomBans.getInstance().getDataFolder(), "limits.yml");
            YamlConfiguration configuration2 = YamlConfiguration.loadConfiguration(configFile2);
            PermissionGroup[] targetGroup = PermissionsEx.getUser(name).getGroups();
            PermissionGroup playerGroup = CustomBans.getInstance().getGroup((Player)sender);
            int targetPrior = 0;
            int playerPrior = 0;
            PermissionGroup[] array;
            int lenght = (array = targetGroup).length;
            for(int i = 0; i < lenght; i++){
            	PermissionGroup pg = array[i];
            	if(configuration2.getInt("priorities." + pg.getName()) >= targetPrior);
            	targetPrior = configuration2.getInt("priorities." + pg.getName());
            }
            playerPrior = configuration2.getInt("priorities." + playerGroup.getName());
            if(targetPrior >= playerPrior){
            	p.sendMessage(prefix + "§4Ошибка: Ваш приоритет: §6" + playerPrior + "§c. §cПриоритет цели: §6" + targetPrior);
            	return true;
            }
            boolean silent = Util.isSilent(args);
            String reason = Util.buildReason(args);
            String banner = Util.getName(sender);
            CustomBans.getInstance().getBanManager().mute(name, banner, reason);
            String message = Msg.get("prefix") + Msg.get("messages.muted", new String[] {"admin", "name", "reason"}, new String[] {banner, name, reason});
            CustomBans.getInstance().getBanManager().announce(message, silent, sender);
            return true;
        }
        return true;
    }
}
