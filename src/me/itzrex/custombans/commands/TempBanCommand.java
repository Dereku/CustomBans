package me.itzrex.custombans.commands;

import me.itzrex.custombans.CustomBans;
import me.itzrex.custombans.Msg;
import me.itzrex.custombans.util.Util;
import ru.tehkode.permissions.PermissionGroup;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

/**
 * Класс создан itzRex. Дата: 06.04.2016.
 */
public class TempBanCommand implements CommandExecutor {

    public static String prefix = Msg.get("prefix");

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        CommandSender p = sender;
        boolean silent = Util.isSilent(args);
        if (args.length < 3) {
            if (!p.hasPermission("custombans.tempban")) {
                p.sendMessage(prefix + "§cНет прав");
                return true;
            }
            p.sendMessage(prefix + "§aИспользуйте - §7/tempban [ник] [цифра] [day, week, year, sec] [причина]");
            return true;
        } else {
            String name = args[0];
            if (name.isEmpty()) {
                p.sendMessage(prefix + "§cВы не ввели ник.");
                return true;
            }
            long expires = Util.getTime(args);
            if (expires <= 0) {
                p.sendMessage(prefix + "§cНе правильно введено число");
                return true;
            }
            if (!(sender instanceof Player)) {
                expires += System.currentTimeMillis();
                String reason = Util.buildReason(args);
                String banner = Util.getName(sender);
                CustomBans.getInstance().getBanManager().tempban(name, reason, banner, expires);
                String message = prefix + Msg.get("messages.tempbanned", new String[]{"admin", "name", "time", "reason"}, new String[]{banner, name, Util.getTimeUntil(expires), reason});
                CustomBans.getInstance().getBanManager().announce(message, silent, sender);
                return true;
            }
            if (CustomBans.getInstance().getBanManager().isWhitelisted(args[0])) {
                p.sendMessage(prefix + "§cИгрок защищён от бана.");
                return true;
            }
            FileConfiguration limits = CustomBans.getInstance().getLimits();
            PermissionGroup group = CustomBans.getInstance().getGroup((Player) sender);
            Long playerLimit = limits.getLong("bans." + group.getName());
            if (playerLimit == 0L) {
                p.sendMessage(prefix + "§cДля Вашей группы не настроен лимит.");
                return true;
            }
            if (expires >= playerLimit * 60000) {
                p.sendMessage(prefix + "§cВы не можете банить на такой срок. Ваш лимит: §6" + Util.getTime(playerLimit * 60000));
                return true;
            }
            PermissionGroup[] targetGroup = PermissionsEx.getUser(name).getGroups();
            PermissionGroup playerGroup = CustomBans.getInstance().getGroup((Player) sender);
            int targetPrior = 0;
            PermissionGroup[] array;
            int lenght = (array = targetGroup).length;
            for (int i = 0; i < lenght; i++) {
                PermissionGroup pg = array[i];
                if (limits.getInt("priorities." + pg.getName()) >= targetPrior);
                targetPrior = limits.getInt("priorities." + pg.getName());
            }
            int playerPrior = limits.getInt("priorities." + playerGroup.getName());
            if (targetPrior >= playerPrior) {
                p.sendMessage(prefix + "§4Ошибка: Ваш приоритет: §6" + playerPrior + "§c. §cПриоритет цели: §6" + targetPrior);
                return true;
            }
            expires += System.currentTimeMillis();
            String reason = Util.buildReason(args);
            String banner = Util.getName(sender);
            CustomBans.getInstance().getBanManager().tempban(name, reason, banner, expires);
            String message = Msg.get("prefix") + Msg.get("messages.tempbanned", new String[]{"admin", "name", "time", "reason"}, new String[]{banner, name, Util.getTimeUntil(expires), reason});
            CustomBans.getInstance().getBanManager().announce(message, silent, sender);
        }
        return true;
    }
}
