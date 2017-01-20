package music.festival.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by bryce_fisher on 1/16/17.
 */
@Service
public class RoleService {

    @Autowired
    RoleRepository roleRepository;

    public List<Role> getDefaultRoles() {
        Role userRole = roleRepository.findByName("ROLE_USER");
        if (userRole == null) {
            userRole = new Role();
            userRole.setName("ROLE_USER");
            userRole = roleRepository.save(userRole);
        }
        return Arrays.asList(userRole);
    }

    public List<Role> getAdminRoles() {
        Role adminRole = roleRepository.findByName("ROLE_ADMIN");
        if (adminRole == null) {
            adminRole = new Role();
            adminRole.setName("ROLE_ADMIN");
            adminRole = roleRepository.save(adminRole);
        }
        List<Role> roleList = getDefaultRoles();
        roleList = new ArrayList<>(roleList);
        roleList.add(adminRole);
        return roleList;
    }
}
