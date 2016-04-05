package me.itzrex.cbans;

import java.io.IOException;
import java.util.List;

import me.itzrex.cbans.managers.BanManager;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Maincmd implements CommandExecutor {

	/*
	 * Класс, отвечающий за дополнительный команды (иначе не объяснить).
	 */
	public static String prefix = CustomBans.prefix;
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(args.length == 0){
			CommandSender p = sender;
		if(!p.hasPermission("cbans.cmd")){
			sender.sendMessage(prefix + "§cНет прав");
			return false;
		}
		//Вывод команд
		sender.sendMessage(prefix + "Версия: §c" + CustomBans.geInstance().getDescription().getVersion() + "§7 by §citzRex");
		sender.sendMessage("§6* §7Режим MySQL - " + String.valueOf((CustomBans.geInstance().isMySQL) ? "§aВключён" : "§cВыключен"));
		sender.sendMessage("§6* §c/" + label + " §cbanlist §7- Список забаненых игроков.");
		sender.sendMessage("§6* §c/" + label + " §cmutelist §7- Список игроков с мутом.");
		return false;
	}
		if(args.length == 1){
			if(args[0].equalsIgnoreCase("banlist")){
				if(CustomBans.geInstance().isMySQL){
					sender.sendMessage(prefix + "§7Забаненые игроки:");
					for(String s : BanManager.bans.keySet()){
						sender.sendMessage("§c- §7" + s);
						return true;
					}
				} else {
				List<String> banlist = CustomBans.dconfig.getStringList("banlist");
				sender.sendMessage(prefix + "§7Забаненые игроки:");
				//Отсылаем игроку список всех забаненых игроков через for.
				for(String s : banlist){
					sender.sendMessage("§c- §7" + s + " (Пермамент - " + (CustomBans.dconfig.getBoolean((s) + ".permament") ? "§aДа§7)" : "§cНет§7)"));
				}
				return true;
				}
				}
			if(args[0].equalsIgnoreCase("mutelist")){
				//Делаем тоже самое, что и с банлистом
				if(CustomBans.geInstance().isMySQL){
					sender.sendMessage(prefix + "§7Список игроков с мутом:");
					for(String s : BanManager.mutes.keySet()){
						sender.sendMessage("§c- §7" + s);
						return true;
					}
				} else {
					List<String> mutelist = CustomBans.dconfig2.getStringList("mutelist");
					sender.sendMessage(prefix + "§7Список игроков с мутом:");
					for(String s : mutelist){
						sender.sendMessage("§c- §7" + s);
					}
					return true;
					}
				}
		}
		return false;
 }
}
