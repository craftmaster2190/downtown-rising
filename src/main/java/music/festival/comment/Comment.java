package music.festival.comment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import music.festival.CommonEntity;
import music.festival.user.Account;

import javax.persistence.Entity;

/**
 * Created by bryce_fisher on 1/15/17.
 */
@Entity
public class Comment extends CommonEntity {
    private Account loggedInAccount;
    private String name;
    private String phone;
    private String email;
    private String text;

    @JsonIgnore
    public Account getLoggedInAccount() {
        return loggedInAccount;
    }

    public void setLoggedInAccount(Account loggedInAccount) {
        this.loggedInAccount = loggedInAccount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
