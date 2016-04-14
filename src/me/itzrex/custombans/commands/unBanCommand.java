package me.itzrex.custombans.commands;

import me.itzrex.custombans.CustomBans;
import me.itzrex.custombans.Msg;
import me.itzrex.custombans.managers.Ban;
import me.itzrex.custombans.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Класс создан itzRex. Дата: 06.04.2016.
 */
public class unBanCommand implements CommandExecutor {
    public static String prefix = Msg.get("prefix");
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        CommandSender p = sender;
        boolean silent = Util.isSilent(args);
        if(args.length == 0){
            if(!p.hasPermission("custombans.unban")){
                p.sendMessage(prefix + "§cНет прав");
                return true;
            }
            p.sendMessage(prefix + "Используйте - §7/unban [ник]");
            return true;
        }
        if(args.length == 1){
            Ban ban = CustomBans.getInstance().getBanManager().getBan(args[0]);
            if(ban == null){
                p.sendMessage(prefix + "§cИгрок не был забанен.");
                return true;
            }
            CustomBans.getInstance().getBanManager().unban(args[0]);
            String message = Msg.get("prefix") + Msg.get("messages.unbanned", new String[] {"admin", "unbanned"}, new String[] {Util.getName(sender), args[0]});
            CustomBans.getInstance().getBanManager().announce(message, silent, sender);
            Bukkit.getLogger().info("Player " + args[0] + " unbanned.");
            return true;
        }
        return true;
    }
}
