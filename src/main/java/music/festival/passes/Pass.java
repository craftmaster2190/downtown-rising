package music.festival.passes;

import music.festival.file.ImageEntity;
import music.festival.user.User;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

/**
 * Created by bryce_fisher on 1/11/17.
 */
@Entity
public class Pass extends ImageEntity {
    private User user;
    private String passBarcode;

    @OneToOne
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getPassBarcode() {
        return passBarcode;
    }

    public void setPassBarcode(String passBarcode) {
        this.passBarcode = passBarcode;
    }
}
