package me.itzrex.custombans.commands;

import java.util.logging.Level;
import me.itzrex.custombans.CustomBans;
import me.itzrex.custombans.Msg;
import me.itzrex.custombans.managers.Mute;
import me.itzrex.custombans.util.Util;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Класс создан itzRex. Дата: 06.04.2016.
 */
public class unMuteCommand implements CommandExecutor {
    public static String prefix = Msg.get("prefix");
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        CommandSender p = sender;
        boolean silent = Util.isSilent(args);
        if(args.length == 0){
            if(!p.hasPermission("custombans.unmute")){
                p.sendMessage(prefix + "§cНет прав.");
                return  true;
            }
            p.sendMessage(prefix + "§aИспользуйте - §7/unmute [ник]");
            return true;
        }
        if(args.length == 1){
            String name = args[0];
            Mute mute = CustomBans.getInstance().getBanManager().getMute(args[0]);
            if(mute == null){
                p.sendMessage(prefix + "§cИгрок не был забанен.");
                return true;
            }
            String admin = Util.getName(sender);
            CustomBans.getInstance().getBanManager().unmute(args[0]);
            String message = Msg.get("prefix") + Msg.get("messages.unmuted", new String[] {"admin", "name"}, new String[] {admin, name});
            CustomBans.getInstance().getBanManager().announce(message, silent, sender);
            CustomBans.getInstance().getLogger().log(Level.INFO, "Player {0} unmuted", name);
            return true;
        }
        return true;
    }
}
