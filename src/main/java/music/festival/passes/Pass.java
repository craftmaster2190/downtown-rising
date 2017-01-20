package music.festival.passes;

import music.festival.file.ImageEntity;
import music.festival.user.Account;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

/**
 * Created by bryce_fisher on 1/11/17.
 */
@Entity
public class Pass extends ImageEntity {
    private Account account;
    private String passBarcode;

    @OneToOne
    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public String getPassBarcode() {
        return passBarcode;
    }

    public void setPassBarcode(String passBarcode) {
        this.passBarcode = passBarcode;
    }
}
