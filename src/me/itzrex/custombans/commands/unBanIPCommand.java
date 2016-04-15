package me.itzrex.custombans.commands;

import me.itzrex.custombans.CustomBans;
import me.itzrex.custombans.Msg;
import me.itzrex.custombans.managers.BanIP;
import me.itzrex.custombans.util.Util;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Класс создан itzRex. Дата: 07.04.2016.
 */
public class unBanIPCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        CommandSender p = sender;
        if(args.length > 0){
            if(!p.hasPermission("custombans.unbanip")){
                p.sendMessage(Msg.get("prefix") + "§cНет прав");
                return true;
            }
            boolean silent = Util.isSilent(args);
            String banner = Util.getName(sender);
            String name = args[0];
            if(name.isEmpty()){
                p.sendMessage(Msg.get("prefix") + "§cНе введён ник.");
                return true;
            }
            if(Util.isIP(name)){
                String ip = name;
                BanIP banip = CustomBans.getInstance().getBanManager().getIPBan(ip);
                if(banip != null){
                    CustomBans.getInstance().getBanManager().unbanip(ip);
                    String msg = Msg.get("prefix") + Msg.get("messages.unbannedip", new String[]{"admin", "name"}, new String[]{banner, name});
                    CustomBans.getInstance().getBanManager().announce(msg, silent, sender);
                } else {
                    p.sendMessage(Msg.get("prefix") + "§cИгрок не был забанен.");
                    return true;
                }
            } else {
                name = CustomBans.getInstance().getBanManager().match(name, true);
                String ip = CustomBans.getInstance().getBanManager().getIP(name);
                BanIP banip = CustomBans.getInstance().getBanManager().getIPBan(ip);
                if(banip == null){
                    p.sendMessage(Msg.get("prefix") + "§cИгрок не был забанен.");
                    return true;
                }
                
                CustomBans.getInstance().getBanManager().unbanip(ip);

                String msg = Msg.get("prefix") + Msg.get("messages.unbannedip", new String[]{"admin", "name"}, new String[]{banner, name});
                CustomBans.getInstance().getBanManager().announce(msg, silent, sender);
            }
        } else{
            if(!p.hasPermission("custombans.unbanip")){
                p.sendMessage(Msg.get("prefix") + "§cНет прав");
                return true;
            }
            p.sendMessage(Msg.get("prefix") + "§aИспользуйте - §7/unbanip [ник]");
            return true;
        }
        return true;
    }
}
