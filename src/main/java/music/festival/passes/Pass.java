package music.festival.passes;

import music.festival.CommonEntity;
import music.festival.user.User;

import javax.persistence.Entity;

/**
 * Created by bryce_fisher on 1/11/17.
 */
@Entity
public class Pass extends CommonEntity {
    private User user;
    private String passBarcode;
}
