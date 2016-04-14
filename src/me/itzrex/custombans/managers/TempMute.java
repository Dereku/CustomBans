package me.itzrex.custombans.managers;

public class TempMute extends Mute implements Temporary{
    private long expires;
    public TempMute(String muted, String muter, String reason, long created, long expires){
        super(muted, muter, reason, created);
        this.expires = expires;
    }

    /**
     * Returns the time that the mute expires
     * @return the time that the mute expires
     */
    public long getExpires() {
        return expires;
    }

    public boolean hasExpired(){
        return System.currentTimeMillis() > expires;
    }
}