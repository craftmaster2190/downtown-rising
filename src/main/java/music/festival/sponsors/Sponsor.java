package music.festival.sponsors;

import music.festival.CommonEntity;
import music.festival.file.ServableFile;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;

/**
 * Created by bryce_fisher on 1/11/17.
 */
@Entity
public class Sponsor extends CommonEntity {
    private String name;
    private ServableFile image;
    private String text;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    public ServableFile getImage() {
        return image;
    }

    public void setImage(ServableFile image) {
        this.image = image;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
