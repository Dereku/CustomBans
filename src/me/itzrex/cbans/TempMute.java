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

public class TempMute implements CommandExecutor {

	public static String prefix = CustomBans.prefix;
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			Player p = (Player) sender;
			if(!p.hasPermission("cbans.tempmute")){
				p.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', CustomBans.geInstance().getConfig().getString("messages.noperm")));
				return true;
			}
			if(args.length == 0){
				sender.sendMessage(prefix + "�7�����������: �6/tempmute [���] [�����] [�������]");
				return true;
			}
			if(args.length == 2){
				try {
					Player target = Bukkit.getPlayer(args[0]);
					if(target.hasPermission("cbans.shield")){
						sender.sendMessage(prefix + "�7����� ������� �� ����.");
						return true;
					}
					for(Player pl : Bukkit.getOnlinePlayers()){
						pl.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', CustomBans.geInstance().getConfig().getString("messages.tempmuted").replace("%admin%", p.getName()).replace("%muted%", target.getName()).replace("%reason%", "�� �������")).replace("%time%", args[1]));
						List<String> mutelist = (List<String>)CustomBans.dconfig2.getStringList("mutelist");
						if(!mutelist.contains(target.getName().toLowerCase())){
							mutelist.add(target.getName().toLowerCase());
						CustomBans.dconfig2.set("mutelist", mutelist);
						CustomBans.dconfig2.set(target.getName().toLowerCase() + ".mutedby", p.getName());
						CustomBans.dconfig2.set(target.getName().toLowerCase() + ".reason", "�� �������");
						CustomBans.dconfig2.set(target.getName().toLowerCase() + ".time", getDateTime());
						CustomBans.dconfig2.set(target.getName().toLowerCase() + ".permament", false);
			            CustomBans.dconfig2.set(String.valueOf(target.getName().toLowerCase()) + ".lasts", Integer.valueOf(Integer.parseInt(args[1])));
			            Integer day_of_year = Integer.valueOf(Calendar.getInstance().get(6));
			            Integer hour = Integer.valueOf(Calendar.getInstance().get(11));
			            Integer minute = Integer.valueOf(Calendar.getInstance().get(12));
			            Integer currentMin = Integer.valueOf(day_of_year.intValue() * 1440 + hour.intValue() * 60 + minute.intValue());
			            CustomBans.dconfig2.set(String.valueOf(target.getName().toLowerCase()) + ".mutes-time", currentMin);
						try {
							CustomBans.dconfig2.save(CustomBans.dataFile);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						return true;
					}
				}
						
				} catch (NullPointerException e){
					if(CustomBans.dplayers.getBoolean(args[0])){
						sender.sendMessage(prefix + "�7����� ������� �� ����.");
						return true;
					}
					sender.sendMessage(prefix + "�7����� �� ������, ��������� ���������� � �������.");
					for(Player pl : Bukkit.getOnlinePlayers()){
						pl.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', CustomBans.geInstance().getConfig().getString("messages.tempmuted").replace("%admin%", p.getName()).replace("%muted%", args[0]).replace("%reason%", "�� �������")).replace("%time%", args[1]));
						List<String> mutelist = (List<String>)CustomBans.dconfig2.getStringList("mutelist");
						if(!mutelist.contains(args[0].toLowerCase())){
							mutelist.add(args[0].toLowerCase());
						CustomBans.dconfig2.set("mutelist", mutelist);
						CustomBans.dconfig2.set(args[0].toLowerCase() + ".mutedby", p.getName());
						CustomBans.dconfig2.set(args[0].toLowerCase() + ".reason", "�� �������");
						CustomBans.dconfig2.set(args[0].toLowerCase() + ".time", getDateTime());
						CustomBans.dconfig2.set(args[0].toLowerCase() + ".permament", false);
			            CustomBans.dconfig2.set(String.valueOf(args[0].toLowerCase()) + ".lasts", Integer.valueOf(Integer.parseInt(args[1])));
			            Integer day_of_year = Integer.valueOf(Calendar.getInstance().get(6));
			            Integer hour = Integer.valueOf(Calendar.getInstance().get(11));
			            Integer minute = Integer.valueOf(Calendar.getInstance().get(12));
			            Integer currentMin = Integer.valueOf(day_of_year.intValue() * 1440 + hour.intValue() * 60 + minute.intValue());
			            CustomBans.dconfig2.set(String.valueOf(args[0].toLowerCase()) + ".mutes-time", currentMin);
						try {
							CustomBans.dconfig2.save(CustomBans.dataFile);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				}
					
				} catch (NumberFormatException e) {
					p.sendMessage(prefix + "�c����� ���������� ������ ���� ������.");
				}
			}
			if(args.length >= 3){
			String reason = "�� �������.";
			try {
				reason = org.apache.commons.lang.StringUtils.join(args, ' ', 2, args.length);
				Player target = Bukkit.getPlayer(args[0]);
				if(target.hasPermission("cbans.shield")){
					sender.sendMessage(prefix + "�7����� ������� �� ����.");
					return true;
				}
				for(Player pl : Bukkit.getOnlinePlayers()){
					pl.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', CustomBans.geInstance().getConfig().getString("messages.tempmuted").replace("%admin%", p.getName()).replace("%muted%", target.getName()).replace("%reason%", reason)).replace("%time%", args[1]));
					List<String> mutelist = (List<String>) CustomBans.dconfig2.getStringList("mutelist");
					if(!mutelist.contains(target.getName())){
						mutelist.add(target.getName());
					CustomBans.dconfig2.set("mutelist", mutelist);
					CustomBans.dconfig2.set(target.getName().toLowerCase() + ".mutedby", p.getName());
					CustomBans.dconfig2.set(target.getName().toLowerCase() + ".reason", reason);
					CustomBans.dconfig2.set(target.getName().toLowerCase() + ".time", getDateTime());
					CustomBans.dconfig2.set(args[0].toLowerCase() + ".permament", false);
		            CustomBans.dconfig2.set(String.valueOf(args[0].toLowerCase()) + ".lasts", Integer.valueOf(Integer.parseInt(args[1])));
		            Integer day_of_year = Integer.valueOf(Calendar.getInstance().get(6));
		            Integer hour = Integer.valueOf(Calendar.getInstance().get(11));
		            Integer minute = Integer.valueOf(Calendar.getInstance().get(12));
		            Integer currentMin = Integer.valueOf(day_of_year.intValue() * 1440 + hour.intValue() * 60 + minute.intValue());
		            CustomBans.dconfig2.set(String.valueOf(args[0].toLowerCase()) + ".mutes-time", currentMin);
		            CustomBans.dconfig2.save(CustomBans.dataFile);
					return true;
				}
			}
				
			} catch (NullPointerException e2){
				if(CustomBans.dplayers.getBoolean(args[0])){
					sender.sendMessage(prefix + "�7����� ������� �� ����.");
					return true;
				}
				sender.sendMessage(prefix + "�7����� �� ������, ���������� � �������.");
				for(Player pl : Bukkit.getOnlinePlayers()){
					pl.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', CustomBans.geInstance().getConfig().getString("messages.tempmuted").replace("%admin%", p.getName()).replace("%muted%", args[0]).replace("%reason%", reason)).replace("%time%", args[1]));
					List<String> mutelist = (List<String>) CustomBans.dconfig2.getStringList("mutelist");
					if(!mutelist.contains(args[0].toLowerCase())){
						mutelist.add(args[0].toLowerCase());
					CustomBans.dconfig2.set("mutelist", mutelist);
					CustomBans.dconfig2.set(args[0].toLowerCase() + ".mutedby", p.getName());
					CustomBans.dconfig2.set(args[0].toLowerCase() + ".reason", reason);
					CustomBans.dconfig2.set(args[0].toLowerCase() + ".time", getDateTime());
					CustomBans.dconfig2.set(args[0].toLowerCase() + ".permament", false);
		            CustomBans.dconfig2.set(String.valueOf(args[0].toLowerCase()) + ".lasts", Integer.valueOf(Integer.parseInt(args[1])));
		            Integer day_of_year = Integer.valueOf(Calendar.getInstance().get(6));
		            Integer hour = Integer.valueOf(Calendar.getInstance().get(11));
		            Integer minute = Integer.valueOf(Calendar.getInstance().get(12));
		            Integer currentMin = Integer.valueOf(day_of_year.intValue() * 1440 + hour.intValue() * 60 + minute.intValue());
		            CustomBans.dconfig2.set(String.valueOf(args[0].toLowerCase()) + ".mutes-time", currentMin);
		            try {
						CustomBans.dconfig2.save(CustomBans.dataFile);
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