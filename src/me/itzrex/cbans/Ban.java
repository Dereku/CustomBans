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

public class Ban implements CommandExecutor {

	/*
	 * Класс, отвечающий за бан.
	 */
	public static String prefix = CustomBans.prefix;
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			Player p = (Player) sender;
			if(!p.hasPermission("cbans.ban")){
				p.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', CustomBans.geInstance().getConfig().getString("messages.noperm")));
				return true;
			}
			if(args.length == 0){
				sender.sendMessage(prefix + "§7Используйте: §6/ban [ник] [причина]");
				return true;
			}
			//Бан без причины
			if(args.length == 1){
				try {
					Player target = Bukkit.getPlayer(args[0]);
					if(target.hasPermission("cbans.shield")){
						sender.sendMessage(prefix + "§7Защищён от бана.");
						return true;
					}
					//Отсылаем игрокам сообщение.
					for(Player pl : Bukkit.getOnlinePlayers()){
						pl.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', CustomBans.geInstance().getConfig().getString("messages.banned").replace("%admin%", p.getName()).replace("%banned%", target.getName()).replace("%reason%", "Не указана")));
						target.kickPlayer(ChatColor.translateAlternateColorCodes('&', CustomBans.geInstance().getConfig().getString("messages.targetmsg").replace("%admin%", p.getName()).replace("%reason%", "Не указана")));
						List<String> banlist = (List<String>)CustomBans.dconfig.getStringList("banlist");
						//Проверяем, есть-ли игрок в банлисте.
						if(!banlist.contains(target.getName().toLowerCase())){
							banlist.add(target.getName().toLowerCase());
						//Если его там нет, заносим в банлист.
						CustomBans.dconfig.set("banlist", banlist);
						CustomBans.dconfig.set(target.getName().toLowerCase() + ".bannedby", p.getName());
						CustomBans.dconfig.set(target.getName().toLowerCase() + ".reason", "Не указана");
						CustomBans.dconfig.set(target.getName().toLowerCase() + ".time", getDateTime());
						CustomBans.dconfig.set(target.getName().toLowerCase() + ".permament", true);
						CustomBans.dconfig.save(CustomBans.dataFile);
						return true;
					}
				}
						
				} catch (NullPointerException e){
					if(CustomBans.dplayers.getBoolean(args[0])){
						sender.sendMessage(prefix + "§7Защищён от бана.");
						return true;
					}
					for(Player pl : Bukkit.getOnlinePlayers()){
						pl.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', CustomBans.geInstance().getConfig().getString("messages.banned").replace("%admin%", p.getName()).replace("%banned%", args[0]).replace("%reason%", "Не указана")));
						List<String> banlist = (List<String>)CustomBans.dconfig.getStringList("banlist");
						if(!banlist.contains(args[0].toLowerCase())){
							banlist.add(args[0].toLowerCase());
						CustomBans.dconfig.set("banlist", banlist);
						CustomBans.dconfig.set(args[0].toLowerCase() + ".bannedby", p.getName());
						CustomBans.dconfig.set(args[0].toLowerCase() + ".reason", "Не указана");
						CustomBans.dconfig.set(args[0].toLowerCase() + ".time", getDateTime());
						CustomBans.dconfig.set(args[0].toLowerCase() + ".permament", true);
						try {
							CustomBans.dconfig.save(CustomBans.dataFile);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if(args.length < 2 ){
				return true;
			}
			//Бан с причиной
			String reason = "Не указана.";
			try {
				reason = org.apache.commons.lang.StringUtils.join(args, ' ', 1, args.length);
				Player target = Bukkit.getPlayer(args[0]);
				if(target.hasPermission("cbans.shield")){
					sender.sendMessage(prefix + "§7Игрок защищён от бана.");
					return true;
				}
				for(Player pl : Bukkit.getOnlinePlayers()){
					pl.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', CustomBans.geInstance().getConfig().getString("messages.banned").replace("%admin%", p.getName()).replace("%banned%", target.getName()).replace("%reason%", reason)));
					target.kickPlayer(ChatColor.translateAlternateColorCodes('&', CustomBans.geInstance().getConfig().getString("messages.targetmsg").replace("%admin%", p.getName()).replace("%reason%", reason)));
					List<String> banlist = (List<String>) CustomBans.dconfig.getStringList("banlist");
					if(!banlist.contains(target.getName().toLowerCase())){
						banlist.add(target.getName().toLowerCase());
					CustomBans.dconfig.set("banlist", banlist);
					CustomBans.dconfig.set(target.getName().toLowerCase() + ".bannedby", p.getName());
					CustomBans.dconfig.set(target.getName().toLowerCase() + ".reason", reason);
					CustomBans.dconfig.set(target.getName().toLowerCase() + ".time", getDateTime());
					CustomBans.dconfig.set(target.getName().toLowerCase() + ".permament", true);
					CustomBans.dconfig.save(CustomBans.dataFile);
					return true;
				}
			}
				
			} catch (NullPointerException e2){
				if(CustomBans.dplayers.getBoolean(args[0])){
					sender.sendMessage(prefix + "§7Защищён от бана.");
					return true;
				}
				for(Player pl : Bukkit.getOnlinePlayers()){
					pl.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', CustomBans.geInstance().getConfig().getString("messages.banned").replace("%admin%", p.getName()).replace("%banned%", args[0]).replace("%reason%", reason)));
					List<String> banlist = (List<String>) CustomBans.dconfig.getStringList("banlist");
					if(!banlist.contains(args[0].toLowerCase())){
						banlist.add(args[0].toLowerCase());
					CustomBans.dconfig.set("banlist", banlist);
					CustomBans.dconfig.set(args[0].toLowerCase() + ".bannedby", p.getName());
					CustomBans.dconfig.set(args[0].toLowerCase() + ".reason", reason);
					CustomBans.dconfig.set(args[0].toLowerCase() + ".time", getDateTime());
					CustomBans.dconfig.set(args[0].toLowerCase() + ".permament", true);
					try {
						CustomBans.dconfig.save(CustomBans.dataFile);
					} catch (IOException e) {
						e.printStackTrace();
					}
					}
				}
			} catch (IOException e) {
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
