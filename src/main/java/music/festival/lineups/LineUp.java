package music.festival.lineups;

import music.festival.file.ImageEntity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bryce_fisher on 1/11/17.
 */
@Entity
public class LineUp extends ImageEntity {
    private List<LineUpVenueDate> venueDates = new ArrayList<>();
    private String genre;

    @OneToMany(cascade = CascadeType.ALL)
    public List<LineUpVenueDate> getVenueDates() {
        return venueDates;
    }

    public void setVenueDates(List<LineUpVenueDate> venueDates) {
        this.venueDates = venueDates;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }
}
