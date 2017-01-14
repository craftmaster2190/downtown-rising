package music.festival.file;

import music.festival.CommonEntity;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Transient;
import java.io.IOException;

/**
 * Created by bryce_fisher on 1/12/17.
 */
@Entity
public class ServableImage extends CommonEntity {
    private static final String[] ACCEPTED_FILE_EXTENSIONS = new String[]{"jpg", "jpeg", "png", "gif", "bmp"};
    private String name;
    private byte[] bytes;

    @SuppressWarnings("unused")
    public ServableImage() {
    }

    public ServableImage(MultipartFile file) throws IOException {
        setName(file.getOriginalFilename());
        setBytes(file.getBytes());
    }

    private static boolean validateExtension(String filename) {
        for (String ext : ACCEPTED_FILE_EXTENSIONS) {
            if (filename.toLowerCase().endsWith('.' + ext.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    @Transient
    public Resource getAsResource() {
        return new ByteArrayResource(getBytes(), getName());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (!validateExtension(name))
            throw new IllegalArgumentException("Illegal File Extension: " + name);
        this.name = name;
    }

    @Lob
    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }
}
