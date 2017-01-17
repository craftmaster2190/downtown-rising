package music.festival.user;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by bryce_fisher on 1/16/17.
 */
@JsonInclude
public class LoginRequest {
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
