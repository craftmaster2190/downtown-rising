package music.festival.user;

import music.festival.CommonEntity;

import javax.persistence.Entity;

/**
 * Created by bryce_fisher on 1/15/17.
 */
@Entity
public class Role extends CommonEntity {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
