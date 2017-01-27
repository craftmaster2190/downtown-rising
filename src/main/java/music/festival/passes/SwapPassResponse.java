package music.festival.passes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by bryce_fisher on 1/26/17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SwapPassResponse {
    private Integer status;
    private Integer success;
    private String ticketType;
    private String message;
    private String badgeNumber;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public String getTicketType() {
        return ticketType;
    }

    public void setTicketType(String ticketType) {
        this.ticketType = ticketType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getBadgeNumber() {
        return badgeNumber;
    }

    public void setBadgeNumber(String badgeNumber) {
        this.badgeNumber = badgeNumber;
    }
}
