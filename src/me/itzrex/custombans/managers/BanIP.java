package me.itzrex.custombans.managers;

public class BanIP extends Ban { // I think its just luck that you can do this.
    public BanIP(String ip, String reason, String banner, long created){
        super(ip, reason, banner, created);
    }

    @Override
    public String getKickMessage(){
		/*
	}
        StringBuilder sb = new StringBuilder(50); //kickmessage
        sb.append(Formatter.message + "You're IP Banned!" + Formatter.regular + "\n Reason: '");
		sb.append(Formatter.reason + reason);
		sb.append(Formatter.regular + "'\n By ");
		sb.append(Formatter.banner + banner + Formatter.regular + ". ");

		String appeal = MaxBans.instance.getBanManager().getAppealMessage();
        if(appeal != null && appeal.isEmpty() == false){
        	sb.append("\n" + Formatter.regular + appeal);
        }
        return sb.toString();*/

        return "§cВаш IP адрес забанен. \n§cЗабанил §6" + getBanner() + "\n§cПричина: §6" + getReason();
    }
}