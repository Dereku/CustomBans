package me.itzrex.cbans;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Kick implements CommandExecutor {

	public static String prefix = CustomBans.prefix;
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			Player p = (Player) sender;
			if(!p.hasPermission("cbans.kick")){
				p.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', CustomBans.geInstance().getConfig().getString("messages.noperm")));
				return true;
			}
			if(args.length == 0){
				sender.sendMessage(prefix + "§7Используйте: §6/kick [ник] [причина]");
				return true;
			}
			if(args.length == 1){
				try {
					Player target = Bukkit.getPlayer(args[0]);
					if(target.hasPermission("cbans.shield")){
						sender.sendMessage(prefix + "§7Игрок защищён от кика.");
						return true;
					}
					for(Player pl : Bukkit.getOnlinePlayers()){
						pl.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', CustomBans.geInstance().getConfig().getString("messages.kicked").replace("%admin%", p.getName()).replace("%kicked%", target.getName()).replace("%reason%", "Не указана")));
						target.kickPlayer(ChatColor.translateAlternateColorCodes('&', CustomBans.geInstance().getConfig().getString("messages.targetkmsg").replace("%admin%", p.getName()).replace("%reason%", "Не указана")));
				}
						
				} catch (NullPointerException e){
					sender.sendMessage(prefix + "§7Игрок не найден.");
					
				} 
			}
			if(args.length < 2 ){
				return true;
			}
			String reason = "Не указана.";
			try {
				reason = org.apache.commons.lang.StringUtils.join(args, ' ', 1, args.length);
				Player target = Bukkit.getPlayer(args[0]);
				if(target.hasPermission("cbans.shield")){
					sender.sendMessage(prefix + "§7Игрок защищён от кика.");
					return true;
				}
				for(Player pl : Bukkit.getOnlinePlayers()){
					pl.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', CustomBans.geInstance().getConfig().getString("messages.kicked").replace("%admin%", p.getName()).replace("%kicked%", target.getName()).replace("%reason%", reason)));
					target.kickPlayer(ChatColor.translateAlternateColorCodes('&', CustomBans.geInstance().getConfig().getString("messages.targetkmsg").replace("%admin%", p.getName()).replace("%reason%", reason)));
					return true;
			}
				
			} catch (NullPointerException e2){
				sender.sendMessage(prefix + "§7Игрок не найден.");
			}
		return true;
}
}
