package me.itzrex.cbans;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class Maincmd implements CommandExecutor {

	public static String prefix = CustomBans.prefix;
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(args.length == 0){
		Player p = (Player) sender;
		if(!p.hasPermission("cbans.cmd")){
			sender.sendMessage(prefix + "�c� ��� �� ���������� ����");
			return false;
		}
		sender.sendMessage(prefix + "�7������: �c" + CustomBans.geInstance().getDescription().getVersion() + "�7 by �citzRex");
		sender.sendMessage("�6* �c/" + label + " �cbanlist �7- ������ ���������.");
		sender.sendMessage("�6* �c/" + label + " �cdeletebans �7- �������� �������.");
		return false;
	}
		if(args.length == 1){
			if(args[0].equalsIgnoreCase("banlist")){
				List<String> banlist = CustomBans.dconfig.getStringList("banlist");
				sender.sendMessage(prefix + "�7������ ���������:");
				for(String s : banlist){
					sender.sendMessage("�c- �7" + s + " (��������� - " + (CustomBans.dconfig.getBoolean((s) + ".permament") ? "�a��7)" : "�c���7)"));
				}
				return false;
				}
			if(args[0].equalsIgnoreCase("deletebans")){
				List<String> banlist = CustomBans.dconfig.getStringList("banlist");
				if(banlist.size() == 0){
					sender.sendMessage(prefix + "�c� �������� ���� ��������� �������.");
					return false;
				}
				banlist.clear();
				CustomBans.dconfig.set("banlist", banlist);
				try {
					CustomBans.dconfig.save(CustomBans.dataFile);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			sender.sendMessage(prefix + "�7��� ��������� ������ - ���������.");
			}
		return false;
 }
}
