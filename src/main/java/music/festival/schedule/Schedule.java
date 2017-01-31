package music.festival.schedule;

import music.festival.CommonEntity;
import music.festival.lineups.LineUp;
import music.festival.user.Account;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * Created by bryce_fisher on 1/12/17.
 */
@Entity
public class Schedule extends CommonEntity {
    private Account account;
    private LineUp lineup;

    @ManyToOne(optional = false)
    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    @ManyToOne(optional = false)
    public LineUp getLineup() {
        return lineup;
    }

    public void setLineup(LineUp lineup) {
        this.lineup = lineup;
    }
}
