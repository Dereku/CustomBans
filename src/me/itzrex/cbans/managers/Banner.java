package me.itzrex.cbans.managers;

import org.bukkit.event.player.PlayerLoginEvent;

import me.itzrex.cbans.CustomBans;
import me.itzrex.cbans.Msg;
import me.itzrex.cbans.utils.Formatter;
import me.itzrex.cbans.utils.Punishment;


public class Banner extends Punishment{
	public Banner(String user, String reason, String banner, long created){
		super(user, reason, banner, created);
	}
	public String getKickMessage(){
		String reason = getReason();
		String bannedby = getBanner();
		return "§cВы были забанены на этом сервере. \n§cПричина: §6" + reason + " §c\n§cЗабанил: §6" + bannedby;
	}
}