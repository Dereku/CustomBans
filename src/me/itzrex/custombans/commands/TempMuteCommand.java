package me.itzrex.custombans.commands;

import me.itzrex.custombans.CustomBans;
import me.itzrex.custombans.Msg;
import me.itzrex.custombans.util.Util;
import ru.tehkode.permissions.PermissionGroup;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import java.io.File;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

/**
 * Класс создан itzRex. Дата: 06.04.2016.
 */
public class TempMuteCommand implements CommandExecutor {
    public static String prefix = Msg.get("prefix");

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        CommandSender p = sender;
        boolean silent = Util.isSilent(args);
        if (args.length < 3) {
            if(!p.hasPermission("custombans.tempmute")){
                p.sendMessage(prefix + "§cНет прав");
                return true;
            }
            p.sendMessage(prefix + "§aИспользуйте - §7/tempmute [ник] [цифра] [day, week, year, sec] [причина]");
            return true;
        } else {
            String name = args[0];
            if (name.isEmpty()) {
                p.sendMessage(prefix + "§cВы не ввели ник.");
                return true;
            }
            long time = Util.getTime(args);
            if (time <= 0) {
                p.sendMessage(prefix + "§cНе правильно введено число");
                return true;
            }
            if(!(sender instanceof Player)){
                time += System.currentTimeMillis();
                String reason = Util.buildReason(args);
                String banner = Util.getName(sender);
                CustomBans.getInstance().getBanManager().tempmute(name, banner, reason, time);
                String message = Msg.get("prefix") + Msg.get("messages.tempmuted", new String[] {"admin", "name", "time", "reason"}, new String[] {banner, name, Util.getTimeUntil(time), reason});
                CustomBans.getInstance().getBanManager().announce(message, silent, sender);
                return true;
            }
            if(CustomBans.getInstance().getBanManager().isWhitelisted(args[0])){
                p.sendMessage(prefix + "§cИгрок защищён от мута.");
                return true;
            }
            File configFile2 = new File(CustomBans.getInstance().getDataFolder(), "limits.yml");
            YamlConfiguration configuration2 = YamlConfiguration.loadConfiguration(configFile2);
            PermissionGroup group = CustomBans.getInstance().getGroup((Player)sender);
            Long playerLimit = Long.parseLong(((configuration2.getString("mutes." + group.getName()))));
            if(time >= playerLimit * 60000){
            	p.sendMessage(prefix + "§cВы не можете выдавать мут на такой срок. Ваш лимит: §6" + Util.getTime(playerLimit * 60000));
            	return true;
            }
            PermissionGroup[] targetGroup = PermissionsEx.getUser(name).getGroups();
            PermissionGroup playerGroup = CustomBans.getInstance().getGroup((Player)sender);
            int targetPrior = 0;
            int playerPrior = 0;
            PermissionGroup[] array;
            int lenght = (array = targetGroup).length;
            for(int i = 0; i < lenght; i++){
            	PermissionGroup pg = array[i];
            	if(configuration2.getInt("priorities." + pg.getName()) >= targetPrior);
            	targetPrior = configuration2.getInt("priorities." + pg.getName());
            }
            playerPrior = configuration2.getInt("priorities." + playerGroup.getName());
            if(targetPrior >= playerPrior){
            	p.sendMessage(prefix + "§4Ошибка: Ваш приоритет: §6" + playerPrior + "§c. §cПриоритет цели: §6" + targetPrior);
            	return true;
            }
            time += System.currentTimeMillis();
            String reason = Util.buildReason(args);
            String banner = Util.getName(sender);
            CustomBans.getInstance().getBanManager().tempmute(name, banner, reason, time);
            String message = Msg.get("prefix") + Msg.get("messages.tempmuted", new String[] {"admin", "name", "time", "reason"}, new String[] {banner, name, Util.getTimeUntil(time), reason});
            CustomBans.getInstance().getBanManager().announce(message, silent, sender);
        }
        return true;
    }
}
