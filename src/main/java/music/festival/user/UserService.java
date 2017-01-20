package music.festival.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
    RoleService roleService;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    public User findById(Long id) {
        return userRepository.findOne(id);
    }

    public User findByEmail(String username) {
        return userRepository.findByEmail(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);
        if (user == null)
            throw new UsernameNotFoundException(username);
        return user;
    }

    public boolean userExists(String username) {
        return userRepository.findByEmail(username) != null;
    }

    public User updateWithoutChangingPassword(User user) {
        User existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser == null)
            throw new UsernameNotFoundException(user.getEmail());

        //If user exists, do not allow changing passwords
        if (existingUser.getPassword() == null) {
            throw new IllegalStateException("User must have password");
        }
        user.setPassword(existingUser.getPassword());
        return userRepository.save(user);
    }

    public User saveAndUpdatePassword(User user) {
        String passwordHash = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(passwordHash);
        return userRepository.save(user);
    }

    public UsernamePasswordAuthenticationToken getUsernamePasswordAuthenticationToken(User user, String password) {
        User existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser == null)
            throw new UsernameNotFoundException(user.getEmail());

        if (bCryptPasswordEncoder.matches(password, existingUser.getPassword())) {
            return new UsernamePasswordAuthenticationToken(user, existingUser.getPassword(), user.getAuthorities());
        }
        throw new BadCredentialsException("Password did not match");
    }

    @PostConstruct
    private void configureDefaultUser() {
        if (!userExists("downtown")) {
            User adminUser = new User();
            adminUser.setName("admin");
            adminUser.setPassword("rising");
            adminUser.setEmail("downtown");
            adminUser.setRoles(roleService.getAdminRoles());
            saveAndUpdatePassword(adminUser);
        }
    }
}
