package me.itzrex.custombans.commands;

import me.itzrex.custombans.CustomBans;
import me.itzrex.custombans.Msg;
import me.itzrex.custombans.managers.BanIP;
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
 * Класс создан itzRex. Дата: 07.04.2016.
 */
public class IPBanCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        CommandSender p = sender;
        boolean silent = Util.isSilent(args);
        String ip;
        String reason = Util.buildReason(args);
        String banner = Util.getName(sender);
        if(args.length > 0){
            String name = args[0];
            if(!p.hasPermission("custombans.banip")){
                p.sendMessage(Msg.get("prefix") + "§cНет прав");
                return true;
            }
            if(name.isEmpty()){
                p.sendMessage(Msg.get("prefix") + "§cНе введён ник.");
                return true;
            }
            ip = name;
            ip = CustomBans.getInstance().getBanManager().getIP(name);
            if(ip == null){
                p.sendMessage(Msg.get("prefix") + "§cIP адрес игрока §6" + name + " §cне найден в базе данных");
                return true;
            }
            if(!(sender instanceof Player)){
                BanIP ipban = CustomBans.getInstance().getBanManager().getIPBan(ip);
                CustomBans.getInstance().getBanManager().ipban(ip, reason, banner);
                String message = Msg.get("prefix") +Msg.get("messages.ipbanned", new String[] {"admin", "ip", "reason"}, new String[]{banner, ip, reason});
                CustomBans.getInstance().getBanManager().announce(message, silent, sender);
                return true;
            }
            if(CustomBans.getInstance().getBanManager().isWhitelisted(name)){
                p.sendMessage(Msg.get("prefix") + "§7Игрок защищён от бана.");
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
            	p.sendMessage(Msg.get("prefix") + "§4Ошибка: Ваш приоритет: §6" + playerPrior + "§c. §cПриоритет цели: §6" + targetPrior);
            	return true;
            }
            BanIP ipban = CustomBans.getInstance().getBanManager().getIPBan(ip);
            CustomBans.getInstance().getBanManager().ipban(ip, reason, banner);
            String message = Msg.get("prefix") +Msg.get("messages.ipbanned", new String[] {"admin", "ip", "reason"}, new String[]{banner, ip, reason});
            CustomBans.getInstance().getBanManager().announce(message, silent, sender);
        } else {
            if(!p.hasPermission("custombans.banip")){
                p.sendMessage(Msg.get("prefix") + "§cНет прав");
                return true;
            }
            p.sendMessage(Msg.get("prefix") + "§aИспользуйте - §7/banip [ник] [причина]");
            return true;
        }
        return true;
    }
}
