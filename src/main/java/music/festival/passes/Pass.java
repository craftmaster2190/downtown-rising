package music.festival.passes;

import music.festival.file.ImageEntity;
import music.festival.user.User;

import javax.persistence.Entity;

/**
 * Created by bryce_fisher on 1/11/17.
 */
@Entity
public class Pass extends ImageEntity {
    private User user;
    private String passBarcode;
}
