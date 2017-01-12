package music.festival.file;

import com.fasterxml.jackson.annotation.JsonIgnore;
import music.festival.CommonEntity;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Entity;
import javax.persistence.Transient;
import java.io.IOException;

/**
 * Created by bryce_fisher on 1/12/17.
 */
@Entity
public class ServableFile extends CommonEntity {
    private String name;
    private byte[] bytes;

    public ServableFile() {
    }

    public ServableFile(MultipartFile file) throws IOException {
        setName(file.getName());
        setBytes(file.getBytes());
    }

    @Transient
    public Resource getAsResource() {
        return new ByteArrayResource(getBytes(), getName());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonIgnore
    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }
}
