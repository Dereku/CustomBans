package me.itzrex.cbans;

import me.itzrex.cbans.utils.Utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Kick implements CommandExecutor {

	/*
	 * Класс, отвечающий за кик.
	 */
	public static String prefix = CustomBans.prefix;
	public static String reason;
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			CommandSender p = sender;
			if(!p.hasPermission("cbans.kick")){
				p.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', CustomBans.geInstance().getConfig().getString("messages.noperm")));
				return true;
			}
			if(args.length == 0){
				sender.sendMessage(prefix + "§7Используйте: §6/kick [ник] [причина]");
				return true;
			}
			//Кик без причины.
			if(args.length == 1){
				reason = "Не указана";
			} else if(args.length < 2 ){
				return true;
			} else {
				reason = org.apache.commons.lang.StringUtils.join(args, ' ', 1, args.length);
			}
				try {
					Player target = Bukkit.getPlayer(args[0]);
					if(target.hasPermission("cbans.shield")){
						if (sender.hasPermission("cbans.shieldbypass")) {
							sender.sendMessage(" ");
								} else {
									sender.sendMessage(prefix + "§7Игрок защищён от кика.");
									return true;
								}
						}
					//Отправляем сообщение игрокам.
					for(Player pl : Utils.getOnlinePlayers()){
						pl.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', CustomBans.geInstance().getConfig().getString("messages.kicked").replace("%admin%", p.getName()).replace("%kicked%", target.getName()).replace("%reason%", "Не указана")));
						target.kickPlayer(ChatColor.translateAlternateColorCodes('&', CustomBans.geInstance().getConfig().getString("messages.targetkmsg").replace("%admin%", p.getName()).replace("%reason%", "Не указана")));
				}
					CustomBans.geInstance().getLogger().info("Player " + target.getName() + " kicked.");
						
				} catch (NullPointerException e){
					sender.sendMessage(prefix + "§7Игрок не онлайн.");
					
				} 
		return true;
}
}
