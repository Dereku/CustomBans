package me.itzrex.cbans;

import java.io.IOException;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Maincmd implements CommandExecutor {

	public static String prefix = CustomBans.prefix;
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(args.length == 0){
		Player p = (Player) sender;
		if(!p.hasPermission("cbans.cmd")){
			sender.sendMessage(prefix + "§cНет прав");
			return false;
		}
		sender.sendMessage(prefix + "Версия: §c" + CustomBans.geInstance().getDescription().getVersion() + "§7 by §citzRex");
		sender.sendMessage("§6* §c/" + label + " §cbanlist §7- Список забаненых игроков.");
		sender.sendMessage("§6* §c/" + label + " §cdeletebans §7- Удалить список забаненых.");
		sender.sendMessage("§6* §c/" + label + " §cdeletemutes §7- Удалить список игроков с мутами.");
		sender.sendMessage("§6* §c/" + label + " §cmutelist §7- Список игроков с мутом.");
		return false;
	}
		if(args.length == 1){
			if(args[0].equalsIgnoreCase("banlist")){
				List<String> banlist = CustomBans.dconfig.getStringList("banlist");
				sender.sendMessage(prefix + "§7Забаненые игроки:");
				for(String s : banlist){
					sender.sendMessage("§c- §7" + s + " (Пермамент - " + (CustomBans.dconfig.getBoolean((s) + ".permament") ? "§aДа7)" : "§cНет7)"));
				}
				return false;
				}
			if(args[0].equalsIgnoreCase("mutelist")){
				List<String> mutelist = CustomBans.dconfig2.getStringList("mutelist");
				sender.sendMessage(prefix + "§7Список игроков с мутом:");
				for(String s : mutelist){
					sender.sendMessage("§c- §7" + s);
				}
				return false;
				}
			if(args[0].equalsIgnoreCase("deletebans")){
				List<String> banlist = CustomBans.dconfig.getStringList("banlist");
				if(banlist.size() == 0){
					sender.sendMessage(prefix + "§cВ банлисте нету забаненых игроков");
					return false;
				}
				banlist.clear();
				CustomBans.dconfig.set("banlist", banlist);
				try {
					CustomBans.dconfig.save(CustomBans.dataFile);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			sender.sendMessage(prefix + "§7Банлист очищен.");
			}
		if(args[0].equalsIgnoreCase("deletemutes")){
			List<String> mutelist = CustomBans.dconfig.getStringList("mutelist");
			if(mutelist.size() == 0){
				sender.sendMessage(prefix + "§7В мутлисте нету игроков");
				return false;
			}
			mutelist.clear();
			CustomBans.dconfig.set("mutelist", mutelist);
			try {
				CustomBans.dconfig.save(CustomBans.dataFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		sender.sendMessage(prefix + "§7Мутлист очищен.");
		return false;
 }
}
