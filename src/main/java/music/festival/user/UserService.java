package music.festival.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Created by bryce_fisher on 1/15/17.
 */
@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    public User login(String username, String password, boolean isPasswordHashed) {
        String passwordHash = password;
        if (!isPasswordHashed)
            passwordHash = hashPassword(password);
        return userRepository.findByUsernameAndPasswordHash(username, passwordHash);
    }

    public User login(String username, String passwordHash) {
        return userRepository.findByUsernameAndPasswordHash(username, passwordHash);
    }

    public String hashPassword(String password) {
        return bCryptPasswordEncoder.encode(password);
    }
}
