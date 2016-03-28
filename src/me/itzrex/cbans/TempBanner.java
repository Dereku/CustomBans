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

	public static String prefix = CustomBans.prefix;
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			Player p = (Player) sender;
			if(!p.hasPermission("cbans.tempban")){
				p.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', CustomBans.geInstance().getConfig().getString("messages.noperm")));
				return true;
			}
			if(args.length == 0){
				sender.sendMessage(prefix + "§7Используйте: §6/tempban [ник] [время] [причина]");
				return true;
			}
			if(args.length == 2){
				try {
					Player target = Bukkit.getPlayer(args[0]);
					if(target.hasPermission("cbans.shield")){
						sender.sendMessage(prefix + "§7Игрок защищён от бана.");
						return true;
					}
					for(Player pl : Bukkit.getOnlinePlayers()){
						pl.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', CustomBans.geInstance().getConfig().getString("messages.tempbanned").replace("%admin%", p.getName()).replace("%banned%", target.getName()).replace("%reason%", "Не указана")).replace("%time%", args[1]));
						target.kickPlayer(ChatColor.translateAlternateColorCodes('&', CustomBans.geInstance().getConfig().getString("messages.targettempmsg").replace("%admin%", p.getName()).replace("%reason%", "Не указана").replace("%time%", args[1])));
						List<String> banlist = (List<String>)CustomBans.dconfig.getStringList("banlist");
						if(!banlist.contains(target.getName().toLowerCase())){
							banlist.add(target.getName().toLowerCase());
						CustomBans.dconfig.set("banlist", banlist);
						CustomBans.dconfig.set(target.getName().toLowerCase() + ".bannedby", p.getName());
						CustomBans.dconfig.set(target.getName().toLowerCase() + ".reason", "Не указана");
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
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						return true;
					}
				}
						
				} catch (NullPointerException e){
					if(CustomBans.dplayers.getBoolean(args[0])){
						sender.sendMessage(prefix + "§7Игрок защищён от бана.");
						return true;
					}
					for(Player pl : Bukkit.getOnlinePlayers()){
						pl.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', CustomBans.geInstance().getConfig().getString("messages.tempbanned").replace("%admin%", p.getName()).replace("%banned%", args[0]).replace("%reason%", "Не указана")).replace("%time%", args[1]));
						List<String> banlist = (List<String>)CustomBans.dconfig.getStringList("banlist");
						if(!banlist.contains(args[0].toLowerCase())){
							banlist.add(args[0].toLowerCase());
						CustomBans.dconfig.set("banlist", banlist);
						CustomBans.dconfig.set(args[0].toLowerCase() + ".bannedby", p.getName());
						CustomBans.dconfig.set(args[0].toLowerCase() + ".reason", "Не указана");
						CustomBans.dconfig.set(args[0].toLowerCase() + ".time", getDateTime());
						CustomBans.dconfig.set(args[0].toLowerCase() + ".permament", false);
			            CustomBans.dconfig.set(String.valueOf(args[0].toLowerCase()) + ".lasts", Integer.valueOf(Integer.parseInt(args[1])));
			            Integer day_of_year = Integer.valueOf(Calendar.getInstance().get(6));
			            Integer hour = Integer.valueOf(Calendar.getInstance().get(11));
			            Integer minute = Integer.valueOf(Calendar.getInstance().get(12));
			            Integer currentMin = Integer.valueOf(day_of_year.intValue() * 1440 + hour.intValue() * 60 + minute.intValue());
			            CustomBans.dconfig.set(String.valueOf(args[0].toLowerCase()) + ".bans-time", currentMin);
						try {
							CustomBans.dconfig.save(CustomBans.dataFile);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				}
					
				} catch (NumberFormatException e) {
					p.sendMessage(prefix + "§cНеверно введено время бана.");
				}
			}
			if(args.length >= 3){
			String reason = "Не указана.";
			try {
				reason = org.apache.commons.lang.StringUtils.join(args, ' ', 2, args.length);
				Player target = Bukkit.getPlayer(args[0]);
				if(target.hasPermission("cbans.shield")){
					sender.sendMessage(prefix + "§7Игрок защищен от бана.");
					return true;
				}
				for(Player pl : Bukkit.getOnlinePlayers()){
					pl.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', CustomBans.geInstance().getConfig().getString("messages.tempbanned").replace("%admin%", p.getName()).replace("%banned%", target.getName()).replace("%reason%", reason)).replace("%time%", args[1]));
					target.kickPlayer(ChatColor.translateAlternateColorCodes('&', CustomBans.geInstance().getConfig().getString("messages.targettempmsg").replace("%admin%", p.getName()).replace("%reason%", reason).replace("%time%", args[1])));
					List<String> banlist = (List<String>) CustomBans.dconfig.getStringList("banlist");
					if(!banlist.contains(target.getName())){
						banlist.add(target.getName());
					CustomBans.dconfig.set("banlist", banlist);
					CustomBans.dconfig.set(target.getName().toLowerCase() + ".bannedby", p.getName());
					CustomBans.dconfig.set(target.getName().toLowerCase() + ".reason", reason);
					CustomBans.dconfig.set(target.getName().toLowerCase() + ".time", getDateTime());
					CustomBans.dconfig.set(args[0].toLowerCase() + ".permament", false);
		            CustomBans.dconfig.set(String.valueOf(args[0].toLowerCase()) + ".lasts", Integer.valueOf(Integer.parseInt(args[1])));
		            Integer day_of_year = Integer.valueOf(Calendar.getInstance().get(6));
		            Integer hour = Integer.valueOf(Calendar.getInstance().get(11));
		            Integer minute = Integer.valueOf(Calendar.getInstance().get(12));
		            Integer currentMin = Integer.valueOf(day_of_year.intValue() * 1440 + hour.intValue() * 60 + minute.intValue());
		            CustomBans.dconfig.set(String.valueOf(args[0].toLowerCase()) + ".bans-time", currentMin);
		            CustomBans.dconfig.save(CustomBans.dataFile);
					return true;
				}
			}
				
			} catch (NullPointerException e2){
				if(CustomBans.dplayers.getBoolean(args[0])){
					sender.sendMessage(prefix + "§7Игрок защищен от бана.");
					return true;
				}
				for(Player pl : Bukkit.getOnlinePlayers()){
					pl.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', CustomBans.geInstance().getConfig().getString("messages.tempbanned").replace("%admin%", p.getName()).replace("%banned%", args[0]).replace("%reason%", reason)).replace("%time%", args[1]));
					List<String> banlist = (List<String>) CustomBans.dconfig.getStringList("banlist");
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
		            try {
						CustomBans.dconfig.save(CustomBans.dataFile);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
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