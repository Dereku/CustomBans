package me.itzrex.cbans;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.itzrex.cbans.Utils;
public class Ban implements CommandExecutor {
    //TODO send.sendMessage("Игрок был забанен, замучен, и тд(Для отображения в консоли)"
	/*
	 * Класс, отвечающий за бан.
	 */
	public static String prefix = CustomBans.prefix;
	static String reason;
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			CommandSender p = sender;
			if(!p.hasPermission("cbans.ban")){
				p.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', CustomBans.geInstance().getConfig().getString("messages.noperm")));
				return true;
			}
			if(args.length == 0){
				p.sendMessage(prefix + "§7Используйте: §6/ban [ник] [причина]");
				return true;
			}
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
								sender.sendMessage(prefix + "§7Игрок защищён от бана.");
								return true;
							}
					}
						List<String> banlist = (List<String>)CustomBans.dconfig.getStringList("banlist");
						//Проверяем, есть-ли игрок в банлисте.
						if(banlist.contains(target.getName().toLowerCase())){
							sender.sendMessage(ChatColor.RED + "Игрок уже забанен.");
							return true;
							//Если его там нет, заносим в банлист.
						} else {
							banlist.add(target.getName().toLowerCase());
						target.kickPlayer(ChatColor.translateAlternateColorCodes('&', CustomBans.geInstance().getConfig().getString("messages.targetmsg").replace("%admin%", p.getName()).replace("%reason%", reason)));
						CustomBans.dconfig.set("banlist", banlist);
						CustomBans.dconfig.set(target.getName().toLowerCase() + ".bannedby", p.getName());
						CustomBans.dconfig.set(target.getName().toLowerCase() + ".reason", reason);
						CustomBans.dconfig.set(target.getName().toLowerCase() + ".time", getDateTime());
						CustomBans.dconfig.set(target.getName().toLowerCase() + ".permament", true);
						CustomBans.dconfig.save(CustomBans.dataFile);
						//Отсылаем игрокам сообщение.
						for(Player pl : Utils.getOnlinePlayers()){
							pl.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', CustomBans.geInstance().getConfig().getString("messages.banned").replace("%admin%", sender.getName()).replace("%banned%", target.getName()).replace("%reason%", reason)));
						}
						    CustomBans.geInstance().getLogger().info("Player " + target.getName() + " banned.");
						return true;
					} 
				} catch (NullPointerException e){
					if(CustomBans.dplayers.getBoolean(args[0].toLowerCase())){
						if (sender.hasPermission("cbans.shieldbypass")) {
							sender.sendMessage(" ");
								} else {
									sender.sendMessage(prefix + "§7Игрок защищён от бана.");
									return true;
								}
						}
						List<String> banlist = (List<String>)CustomBans.dconfig.getStringList("banlist");
						if(!banlist.contains(args[0].toLowerCase())){
							banlist.add(args[0].toLowerCase());
						CustomBans.dconfig.set("banlist", banlist);
						CustomBans.dconfig.set(args[0].toLowerCase() + ".bannedby", p.getName());
						CustomBans.dconfig.set(args[0].toLowerCase() + ".reason", reason);
						CustomBans.dconfig.set(args[0].toLowerCase() + ".time", getDateTime());
						CustomBans.dconfig.set(args[0].toLowerCase() + ".permament", true);
						try {
							CustomBans.dconfig.save(CustomBans.dataFile);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
						} else {
							sender.sendMessage(ChatColor.RED + "Игрок уже забанен.");
							return true;
						}
						for(Player pl : Utils.getOnlinePlayers()){
							pl.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', CustomBans.geInstance().getConfig().getString("messages.banned").replace("%admin%", sender.getName())).replace("%banned%", args[0]).replace("%reason%", reason));
						}
						CustomBans.geInstance().getLogger().info("Player " + args[0] + " banned.");
				} catch (Exception e) {
					e.printStackTrace();
				}
			
		return true;
}
	  private static String getDateTime()
	  {
	    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	    Date date = new Date();
	    return dateFormat.format(date);
	  }
}
