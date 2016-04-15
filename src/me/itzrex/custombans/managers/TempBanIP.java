package me.itzrex.custombans.managers;


import me.itzrex.custombans.util.Util;

public class TempBanIP extends BanIP implements Temporary{
    private final long expires;

    public TempBanIP(String ip, String reason, String banner, long created, long expires){
        super(ip, reason, banner, created);
        this.expires = expires;
    }

    /**
     * Returns the time that the ban expires.
     * @return the time that the ban expires.
     */
    @Override
    public long getExpires() {
        return expires;
    }

    @Override
    public boolean hasExpired(){
        return System.currentTimeMillis() > expires;
    }

    @Override
    public String getKickMessage(){
		/*
		StringBuilder sb = new StringBuilder(50);
		sb.append(Formatter.message + "You're temporarily IP Banned!" + Formatter.regular + "\n Reason: '");
		sb.append(Formatter.reason + reason);
		sb.append(Formatter.regular + "'\n By ");
		sb.append(Formatter.banner + banner + Formatter.regular + ". ");

    	sb.append("Expires in " + Formatter.time + Util.getTimeUntil(expires));

		String appeal = MaxBans.instance.getBanManager().getAppealMessage();
        if(appeal != null && appeal.isEmpty() == false){
        	sb.append("\n" + Formatter.regular + appeal);
        }
        return sb.toString();*/
        return "§cВаш IP адрес был временно забанен. \n§cЗабанил: §6" + getBanner() + "\n§cПричина: §6" + getReason() + "\n§cОсталось: §6" + Util.getTimeUntil(expires);
    }
}