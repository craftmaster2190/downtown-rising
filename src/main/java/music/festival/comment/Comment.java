package music.festival.comment;

import music.festival.CommonEntity;
import music.festival.user.User;

import javax.persistence.Entity;

/**
 * Created by bryce_fisher on 1/15/17.
 */
@Entity
public class Comment extends CommonEntity {
    private User loggedInUser;
    private String name;
    private String phone;
    private String email;
    private String text;
}
