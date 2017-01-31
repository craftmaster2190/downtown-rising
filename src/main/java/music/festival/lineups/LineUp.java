package music.festival.lineups;

import music.festival.file.ImageEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by bryce_fisher on 1/11/17.
 */
@Entity
public class LineUp extends ImageEntity {
    private List<Date> dates = new ArrayList<>();
    private String genre;

    @ElementCollection(fetch = FetchType.EAGER)
    @Temporal(TemporalType.DATE)
    public List<Date> getDates() {
        return dates;
    }

    public void setDates(List<Date> dates) {
        this.dates = dates;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }
}
