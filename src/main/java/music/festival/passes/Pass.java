package music.festival.passes;

import com.fasterxml.jackson.annotation.JsonProperty;
import music.festival.CommonEntity;
import music.festival.user.Account;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

/**
 * Created by bryce_fisher on 1/11/17.
 */
@Entity
public class Pass extends CommonEntity {
    private Account account;
    private String cityWeeklyTicketId;
    private String wristbandBadgeId;

    @OneToOne(optional = false)
    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column(unique = true)
    public String getCityWeeklyTicketId() {
        return cityWeeklyTicketId;
    }

    public void setCityWeeklyTicketId(String cityWeeklyTicketId) {
        this.cityWeeklyTicketId = cityWeeklyTicketId;
    }

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column(unique = true)
    public String getWristbandBadgeId() {
        return wristbandBadgeId;
    }

    public void setWristbandBadgeId(String wristbandBadgeId) {
        this.wristbandBadgeId = wristbandBadgeId;
    }
}
