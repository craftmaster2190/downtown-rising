package music.festival;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by bryce_fisher on 1/4/17.
 */
@MappedSuperclass
public abstract class CommonEntity implements Serializable {
    private Long id;
    private Date createdOn = new Date();
    private String createdBy = "System";
    private Date updatedOn = new Date();
    private String updatedBy = "System";

    protected CommonEntity() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            final Object loggedInAccount = authentication.getPrincipal();
            if (loggedInAccount != null) {
                createdBy = loggedInAccount.toString();
                updatedBy = loggedInAccount.toString();
            }
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    @Column(updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(final Date createdOn) {
        this.createdOn = createdOn;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public Date getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(final Date updatedOn) {
        this.updatedOn = updatedOn;
    }

    @Column(updatable = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(final String createdBy) {
        this.createdBy = createdBy;
    }

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(final String updatedBy) {
        this.updatedBy = updatedBy;
    }
}
