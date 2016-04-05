package me.itzrex.cbans;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WarnCommand implements CommandExecutor {

	public static String prefix = CustomBans.prefix;
	static String message;
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		CommandSender p = sender;
		message = org.apache.commons.lang.StringUtils.join(args, ' ', 1, args.length);
		if(!p.hasPermission("cbans.warn")){
			p.sendMessage(prefix + CustomBans.noperm);
			return true;
		}
		if(args.length == 0){
			p.sendMessage(prefix + "§7Используйте: §6/warn [ник] [сообщение]");
			return true;
		}
		if(args.length < 2){
			return true;
		}
		try {
			Player target = Bukkit.getPlayer(args[0]);
			target.sendMessage(ChatColor.translateAlternateColorCodes('&', CustomBans.geInstance().getConfig().getString("messages.warnmsg")));
		} catch(NullPointerException e){
			p.sendMessage(prefix + "§cИгрок не онлайн");
			return true;
		}
		return false;
	}

}
