package me.itzrex.cbans.managers;

import me.itzrex.cbans.utils.Punishment;

public class Mutter extends Punishment{
	
	public Mutter(String muted, String muter, String reason, long created){
		super(muted, reason, muter, created);
	}
}