package music.festival.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * Created by bryce_fisher on 1/15/17.
 */
@Service
public class UserService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleService roleRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username);
    }

    public boolean userExists(String username) {
        return userRepository.findByEmail(username) != null;
    }

    public User saveOrUpdate(User user) {
        User existingUser = userRepository.findByEmail(user.getEmail());
        //If user exists, do not allow changing passwords
        if (existingUser != null && user.getPassword() == null) {
            user.setPassword(existingUser.getPassword());
        }
        return userRepository.save(user);
    }

    @PostConstruct
    private void configureDefaultUser() {
        if (!userExists("admin")) {
            User adminUser = new User();
            adminUser.setName("admin");
            adminUser.setPassword("admin");
            adminUser.setEmail("admin");
            adminUser.setRoles(roleRepository.getAdminRoles());
            saveOrUpdate(adminUser);
        }
    }
}
