package music.festival.file;

import music.festival.CommonEntity;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Transient;

/**
 * Created by bryce_fisher on 1/12/17.
 */
@Entity
public class ServableImage extends CommonEntity {
    private static final String[] ACCEPTED_FILE_EXTENSIONS = new String[]{"jpg", "jpeg", "png", "gif", "bmp"};
    private byte[] bytes;

    @Transient
    public Resource getAsResource() {
        return new ByteArrayResource(getBytes(), getId() + ".png");
    }

    @Lob
    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }
}
