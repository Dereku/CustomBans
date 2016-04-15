package me.itzrex.custombans.managers;

public class Mute extends Punishment{
    public Mute(String muted, String muter, String reason, long created){
        super(muted, reason, muter, created);
    }
}