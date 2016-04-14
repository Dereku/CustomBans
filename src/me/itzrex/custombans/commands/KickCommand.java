package me.itzrex.custombans.commands;

import me.itzrex.custombans.CustomBans;
import me.itzrex.custombans.Msg;
import me.itzrex.custombans.util.Util;
import ru.tehkode.permissions.PermissionGroup;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

/**
 * Класс создан itzRex. Дата: 06.04.2016.
 */
public class KickCommand implements CommandExecutor {
    public static String prefix = Msg.get("prefix");
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        CommandSender p = sender;
        if(args.length == 0){
            if(!p.hasPermission("custombans.kick")){
                p.sendMessage(prefix + "§cНет прав");
                return true;
            }
            p.sendMessage(prefix + "§aИспользуйте - §7/kick [ник] [причина]");
            return true;
        }
        if(args.length > 0){
            Player pl = Bukkit.getPlayer(args[0]);
            boolean silent = Util.isSilent(args);
            String reason = Util.buildReason(args);
            String banner;

            if(sender instanceof Player){
                banner = ((Player) sender).getName();
            }
            else{
                banner = "Console";
            }
            if(!(sender instanceof Player)){
                String message = Msg.get("messages.you-kicked", new String[] {"admin", "reason"}, new String[] {banner, reason});
                CustomBans.getInstance().getBanManager().kick(args[0].toLowerCase(), message);
                message = Msg.get("prefix") + Msg.get("messages.all-kick-msg", new String[] {"admin", "name", "reason"}, new String[] {banner, args[0].toLowerCase(), reason});
                CustomBans.getInstance().getBanManager().announce(message, silent, sender);
                return true;
            }
            if(CustomBans.getInstance().getBanManager().isWhitelisted(args[0])){
                p.sendMessage(prefix + "§cИгрок защищён от кика.");
                return true;
            }
            if(pl != null){
                String name = pl.getName().toLowerCase();
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
                String message = Msg.get("messages.you-kicked", new String[] {"admin", "reason"}, new String[] {banner, reason});
                CustomBans.getInstance().getBanManager().kick(name, message);
                message = Msg.get("prefix") + Msg.get("messages.all-kick-msg", new String[] {"admin", "name", "reason"}, new String[] {banner, name, reason});
                CustomBans.getInstance().getBanManager().announce(message, silent, sender);
                return true;
            } else {
                p.sendMessage(prefix + Msg.get("messages.unknown"));
                return true;
            }
        }
        return true;
    }
}
