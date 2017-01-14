package music.festival.file;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import music.festival.CommonEntity;

import javax.persistence.*;

/**
 * Created by bryce_fisher on 1/13/17.
 */
@MappedSuperclass
public abstract class ImageEntity extends CommonEntity {
    ServableImage image;
    private String name;
    private String text;
    private String link;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonIgnore
    public ServableImage getImage() {
        return image;
    }

    public void setImage(ServableImage image) {
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

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
