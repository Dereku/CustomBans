package me.itzrex.custombans.commands;

import me.itzrex.custombans.CustomBans;
import me.itzrex.custombans.Msg;
import me.itzrex.custombans.managers.Ban;
import me.itzrex.custombans.managers.TempBan;
import me.itzrex.custombans.util.Util;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Класс создан itzRex. Дата: 07.04.2016.
 */
public class BanInfoCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        CommandSender p = sender;
        if(args.length == 0){
            if(!p.hasPermission("custombans.baninfo")){
                p.sendMessage(Msg.get("prefix") + "§cНет прав");
                return true;
            }
            p.sendMessage(Msg.get("prefix") + "§aИспользуйте - §7/baninfo [ник]");
            return true;
        }
        if(args.length == 1){
            String name = args[0];
            Ban ban = CustomBans.getInstance().getBanManager().getBan(name);
            if(ban == null){
                p.sendMessage(Msg.get("prefix") + "§cИгрок не был забанен");
                return true;
            }
            if(ban instanceof TempBan){
                TempBan tban = (TempBan) ban;
                p.sendMessage("§7Статус: §aВременно забанен");
                p.sendMessage("§7Забанил: §a" + tban.getBanner());
                p.sendMessage("§7Причина: §a" + tban.getReason());
                p.sendMessage("§7Осталось времени: §a" + Util.getTimeUntil(tban.getExpires()));
                return true;
            } else {
                p.sendMessage("§7Статус: §cЗабанен навсегда");
                p.sendMessage("§7Забанил: §a" + ban.getBanner());
                p.sendMessage("§7Причина: §a" + ban.getReason());
                return true;
            }
        }
        return true;
    }
}
