package music.festival.user;

import music.festival.CommonEntity;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Entity;
import javax.persistence.Transient;

/**
 * Created by bryce_fisher on 1/15/17.
 */
@Entity
public class Role extends CommonEntity implements GrantedAuthority {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Transient
    @Override
    public String getAuthority() {
        return getName();
    }
}
