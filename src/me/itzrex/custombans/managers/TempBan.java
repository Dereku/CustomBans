package me.itzrex.custombans.managers;


import me.itzrex.custombans.util.Util;

public class TempBan extends Ban implements Temporary{
    private final long expires;

    /**
     * Creates a new tempban. Does not store it in memory or the DB.
     * @param reason The reason for the ban
     * @param banner The admin who banned him
     * @param created The time the ban was created
     * @param expires The time the ban will expires
     */
    public TempBan(String user, String reason, String banner, long created, long expires){
        super(user, reason, banner, created);
        this.expires = expires;
    }

    /**
     * Returns the time the ban expires
     * @return The time the ban expires
     */
    @Override
    public long getExpires() {
        return expires;
    }
    /**
     * Returns true if this tempban has expired.
     * @return true if this tempban has expired.
     */
    @Override
    public boolean hasExpired(){
        return System.currentTimeMillis() > expires;
    }

    /**
     * You're banned!<br/>
     * Reason: 'Misconduct'<br/>
     * By Console.
     */
    @Override
    public String getKickMessage(){
		/*
		StringBuilder sb = new StringBuilder(50);
		sb.append(Formatter.message + "You're temporarily banned!" + Formatter.regular + "\n Reason: '");
		sb.append(Formatter.reason + reason);
		sb.append(Formatter.regular + "'\n By ");
		sb.append(Formatter.banner + banner + Formatter.regular + ". ");

    	sb.append("Expires in " + Formatter.time + Util.getTimeUntil(expires));

		String appeal = MaxBans.instance.getBanManager().getAppealMessage();
        if(appeal != null && appeal.isEmpty() == false){
        	sb.append("\n" + Formatter.regular + appeal);
        }
        return sb.toString();*/

        return "§cВы были времмено забанены. \n§cЗабанил: §e" + getBanner() + " \n§cПричина: §e" + getReason() + " \n§cОсталось: §e" + Util.getTimeUntil(getExpires());
    }
}