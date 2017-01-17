package music.festival.user;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by bryce_fisher on 1/17/17.
 */
@JsonInclude
public class ChangePasswordRequest {
    private String username;
    private String oldPassword;
    private String newPassword;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public boolean isValid() {
        return username != null &&
                oldPassword != null &&
                newPassword != null;
    }
}
