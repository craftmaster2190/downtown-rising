package music.festival.user;

import music.festival.CommonEntity;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

/**
 * Created by bryce_fisher on 1/15/17.
 */
@Entity
public class Session extends CommonEntity {
    private User user;

    @OneToOne
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
