package music.festival.file;

import music.festival.CommonEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bryce_fisher on 1/20/17.
 */
@Entity
public class EditablePage extends CommonEntity {
    private List<EditablePageImage> images = new ArrayList<>();
    private String path;
    private String title;
    private String html;

    @ManyToMany
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Lob
    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }
}
