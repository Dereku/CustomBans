package me.itzrex.custombans.managers;

/**
 * Класс создан itzRex. Дата: 06.04.2016.
 */
public class Ban extends Punishment {
    public Ban(String id, String reason, String banner, long created) {
        super(id, reason, banner, created);
    }

    public String getKickMessage(){

        return "§cВы были забанены. \n§cЗабанил: §e" + getBanner() + " \n§cПричина: §e" + getReason();
    }

}
