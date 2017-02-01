package music.festival.lineups;

import music.festival.CommonEntity;
import music.festival.venues.Venue;

import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * Created by bryce_fisher on 1/31/17.
 */
@Entity
public class LineUpVenueDate extends CommonEntity {
    private Venue venue;
    private Date date;

    public Venue getVenue() {
        return venue;
    }

    public void setVenue(Venue venue) {
        this.venue = venue;
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
