package music.festival.file;

import music.festival.CommonEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bryce_fisher on 1/20/17.
 */
@Entity
public class EditablePage extends CommonEntity {
    private List<EditablePageImage> images = new ArrayList<>();
    private String path;
    private String html;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    public List<EditablePageImage> getImages() {
        return images;
    }

    public void setImages(List<EditablePageImage> images) {
        this.images = images;
    }

    @Column(unique = true)
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Lob
    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }
}
