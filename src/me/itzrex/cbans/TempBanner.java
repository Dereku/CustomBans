package me.itzrex.cbans;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TempBanner implements CommandExecutor {
	static String reason;
	/*
	 * Класс, отвечающий за временный бан.
	 */
	public static String prefix = CustomBans.prefix;
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		CommandSender p = sender;
			if(!p.hasPermission("cbans.tempban")){
				p.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', CustomBans.geInstance().getConfig().getString("messages.noperm")));
				return true;
			}
			if(args.length == 0 || args.length == 1){
				sender.sendMessage(prefix + "§7Используйте: §6/tempban [ник] [время] [причина]");
				return true;
			}
			
			if(args.length == 2){
				reason = "Не указана";
			} else if(args.length >= 3 ){
				reason = org.apache.commons.lang.StringUtils.join(args, ' ', 1, args.length);
			} 
				try {
					if (!isInteger(args[1])){
                        sender.sendMessage(ChatColor.RED + "Время указано неверно.");
						return true;
					}
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
					if(banlist.contains(target.getName().toLowerCase())){
						sender.sendMessage(ChatColor.RED + "Игрок уже забанен.");
						return true;
					} else {
							banlist.add(target.getName().toLowerCase());
						CustomBans.dconfig.set("banlist", banlist);
						CustomBans.dconfig.set(target.getName().toLowerCase() + ".bannedby", p.getName());
						CustomBans.dconfig.set(target.getName().toLowerCase() + ".reason", reason);
						CustomBans.dconfig.set(target.getName().toLowerCase() + ".time", getDateTime());
						CustomBans.dconfig.set(target.getName().toLowerCase() + ".permament", false);
			            CustomBans.dconfig.set(String.valueOf(target.getName().toLowerCase()) + ".lasts", Integer.valueOf(Integer.parseInt(args[1])));
			            Integer day_of_year = Integer.valueOf(Calendar.getInstance().get(6));
			            Integer hour = Integer.valueOf(Calendar.getInstance().get(11));
			            Integer minute = Integer.valueOf(Calendar.getInstance().get(12));
			            Integer currentMin = Integer.valueOf(day_of_year.intValue() * 1440 + hour.intValue() * 60 + minute.intValue());
			            CustomBans.dconfig.set(String.valueOf(target.getName().toLowerCase()) + ".bans-time", currentMin);
						try {
							CustomBans.dconfig.save(CustomBans.dataFile);
						} catch (IOException e) {
							e.printStackTrace();
						}
						for(Player pl : Utils.getOnlinePlayers()){
							pl.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', CustomBans.geInstance().getConfig().getString("messages.tempbanned").replace("%admin%", p.getName()).replace("%banned%", args[0]).replace("%reason%", reason)).replace("%time%", args[1]));
						}
						CustomBans.geInstance().getLogger().info("Player " + args[0] + " tempbanned for " + args[1] + " minutes.");
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
						CustomBans.dconfig.set(args[0].toLowerCase() + ".permament", false);
			            CustomBans.dconfig.set(String.valueOf(args[0].toLowerCase()) + ".lasts", Integer.valueOf(Integer.parseInt(args[1])));
			            Integer day_of_year = Integer.valueOf(Calendar.getInstance().get(6));
			            Integer hour = Integer.valueOf(Calendar.getInstance().get(11));
			            Integer minute = Integer.valueOf(Calendar.getInstance().get(12));
			            Integer currentMin = Integer.valueOf(day_of_year.intValue() * 1440 + hour.intValue() * 60 + minute.intValue());
			            CustomBans.dconfig.set(String.valueOf(args[0].toLowerCase()) + ".bans-time", currentMin);
						for(Player pl : Utils.getOnlinePlayers()){
							pl.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', CustomBans.geInstance().getConfig().getString("messages.tempbanned").replace("%admin%", p.getName()).replace("%banned%", args[0]).replace("%reason%", reason)).replace("%time%", args[1]));
						}
						CustomBans.geInstance().getLogger().info("Player " + args[0] + " tempbanned for " + args[1] + " minutes.");
						try {
							CustomBans.dconfig.save(CustomBans.dataFile);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					} else {
						sender.sendMessage(ChatColor.RED + "Игрок уже забанен.");
						return true;
					}
					
				} catch (NumberFormatException e) {
					sender.sendMessage(prefix + "§cНеверно введено время бана.");
			}
		return true;
}
	  private static String getDateTime()
	  {
	    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	    Date date = new Date();
	    return dateFormat.format(date);
	  }
	  public boolean isInteger(String aString) {
		    try {
		        Integer.parseInt(aString);
		        return true;
		    } catch (Exception ex) {
		        return false;
		    }
		}
}