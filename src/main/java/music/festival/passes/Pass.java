package music.festival.passes;

import com.fasterxml.jackson.annotation.JsonProperty;
import music.festival.CommonEntity;
import music.festival.user.Account;
import org.hashids.Hashids;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

/**
 * Created by bryce_fisher on 1/11/17.
 */
@Entity
public class Pass extends CommonEntity {
    @Transient
    private static final int BADGE_ID_LENGTH = 8;
    /**
     * Obfuscate badge ids so they are not in sequence.
     * Just using the database record id because the database will ensure uniqueness.
     */
    @Transient
    private static final Hashids HASHIDS = new Hashids("DowntownRising", BADGE_ID_LENGTH, "0123456789ABCDEF");
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
        this.cityWeeklyTicketId = cityWeeklyTicketId.toUpperCase();
    }

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column(unique = true)
    public String getWristbandBadgeId() {
        return wristbandBadgeId;
    }

    public void setWristbandBadgeId(String wristbandBadgeId) {
        this.wristbandBadgeId = wristbandBadgeId.toUpperCase();
    }

    /**
     * When Pass is saved, hibernate will generate a unique ID,
     * HashIds will generate the same hashid every time for the same ID.
     * But to people the Badge ID will be out of order and random prevent a person from
     * generating a forged pass based on a sequence id.
     * <p>
     * This is not a security scheme, this an obfuscation scheme.
     * Anyone that wanted to and knew what they were doing could reverse engineer the ID.
     * All we are doing here is making it difficult for the layman to forge a badge.
     *
     * @param id
     */
    @Override
    public void setId(Long id) {
        super.setId(id);
        if (id != null) {
            String badgeId = HASHIDS.encode(id);
            if (badgeId.length() > BADGE_ID_LENGTH) {
                badgeId = badgeId.substring(0, BADGE_ID_LENGTH - 1);
            }
            setWristbandBadgeId(badgeId);
        }
    }
}
