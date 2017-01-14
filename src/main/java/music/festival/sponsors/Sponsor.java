package music.festival.sponsors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import music.festival.CommonEntity;
import music.festival.file.ServableFile;

import javax.persistence.*;

/**
 * Created by bryce_fisher on 1/11/17.
 */
@Entity
public class Sponsor extends CommonEntity {
    private String name;
    private ServableFile image;
    private String text;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonIgnore
    public ServableFile getImage() {
        return image;
    }

    public void setImage(ServableFile image) {
        this.image = image;
    }

    @Transient
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public Long getImageId() {
        if (getImage() != null) {
            return getImage().getId();
        }
        return null;
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
