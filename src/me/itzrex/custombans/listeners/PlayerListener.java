package me.itzrex.custombans.listeners;

import me.itzrex.custombans.CustomBans;
import me.itzrex.custombans.Msg;
import me.itzrex.custombans.managers.Ban;
import me.itzrex.custombans.managers.Mute;
import me.itzrex.custombans.managers.TempMute;
import me.itzrex.custombans.util.Util;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import me.itzrex.custombans.managers.BanIP;

/**
 * Класс создан itzRex. Дата: 07.04.2016.
 */
public class PlayerListener implements Listener {

    @EventHandler
    public  void onJoin(final PlayerLoginEvent e){
        final Player p = e.getPlayer();
        CustomBans.getInstance().getBanManager().logIP(p.getName(), e.getAddress().getHostAddress());
        if(p.hasPermission("custombans.shield")){
            CustomBans.getInstance().getBanManager().setWhitelisted(p.getName().toLowerCase(), true);
        } else {
            CustomBans.getInstance().getBanManager().setWhitelisted(p.getName().toLowerCase(), false);
        }
        String address = CustomBans.getInstance().getBanManager().getIP(p.getName());
        BanIP banip = CustomBans.getInstance().getBanManager().getIPBan(address);
        if(banip != null){
            e.setResult(PlayerLoginEvent.Result.KICK_OTHER);
            e.setKickMessage(banip.getKickMessage());
            return;
        }
        Ban ban = CustomBans.getInstance().getBanManager().getBan(p.getName());
        if(ban == null){
            return;
        }
        e.setResult(PlayerLoginEvent.Result.KICK_OTHER);
        e.setKickMessage(ban.getKickMessage());
    }
    @EventHandler(priority = EventPriority.LOWEST)
    public void onChat(final AsyncPlayerChatEvent e){
        final Player p = e.getPlayer();
        Mute mute = CustomBans.getInstance().getBanManager().getMute(p.getName());
        if(mute == null){
            return;
        }
        if(mute instanceof TempMute){
            TempMute tmute = (TempMute) mute;
            p.sendMessage(Msg.get("prefix") + "§cВы были заглушены на §6" + Util.getTimeUntil(tmute.getExpires()));
            e.setCancelled(true);
        } else  {
            p.sendMessage(Msg.get("prefix")  +" §cВы были заглушены навсегда.");
            e.setCancelled(true);
        }

    }
}
