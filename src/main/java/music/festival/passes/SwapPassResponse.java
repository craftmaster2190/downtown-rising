package music.festival.passes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by bryce_fisher on 1/26/17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_EMPTY)
public class SwapPassResponse {
    private Integer status;
    private Integer success;
    private String ticketType;
    private String message;
    private String badgeNumber;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(final Integer status) {
        this.status = status;
    }

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(final Integer success) {
        this.success = success;
    }

    public String getTicketType() {
        return ticketType;
    }

    public void setTicketType(final String ticketType) {
        this.ticketType = ticketType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public String getBadgeNumber() {
        return badgeNumber;
    }

    public void setBadgeNumber(final String badgeNumber) {
        this.badgeNumber = badgeNumber;
    }

    @Override
    public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (final JsonProcessingException e) {
            return super.toString();
        }
    }
}
