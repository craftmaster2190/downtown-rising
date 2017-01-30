package music.festival.user;

import music.festival.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Created by bryce_fisher on 1/15/17.
 */
@Service
public class AccountService implements UserDetailsService {

    @Autowired
    AccountRepository userRepository;
    @Autowired
    RoleService roleService;
    @Autowired
    ConfigurationService configurationService;

    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    public Account findById(Long id) {
        return userRepository.findOne(id);
    }

    public Account findByEmail(String username) {
        return userRepository.findByEmail(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = userRepository.findByEmail(username);
        if (account == null)
            throw new UsernameNotFoundException(username);
        return account;
    }

    public boolean userExists(String username) {
        return userRepository.findByEmail(username) != null;
    }

    public Account updateWithoutChangingPassword(Account account) {
        Account existingAccount = userRepository.findOne(account.getId());

        if (existingAccount == null) {
            existingAccount = userRepository.findByEmail(account.getEmail());
            if (existingAccount == null)
                throw new UsernameNotFoundException(account.getEmail());
        }

        //If account exists, do not allow changing passwords
        if (existingAccount.getPassword() == null) {
            throw new IllegalStateException("Account must have password");
        }
        account.setPassword(existingAccount.getPassword());
        return userRepository.save(account);
    }

    public Account saveAndUpdatePassword(Account account) {
        String passwordHash = bCryptPasswordEncoder.encode(account.getPassword());
        account.setPassword(passwordHash);
        return userRepository.save(account);
    }

    public UsernamePasswordAuthenticationToken getUsernamePasswordAuthenticationToken(Account account, String password) {
        Account existingAccount = userRepository.findByEmail(account.getEmail());
        if (existingAccount == null)
            throw new UsernameNotFoundException(account.getEmail());

        if (bCryptPasswordEncoder.matches(password, existingAccount.getPassword())) {
            return new UsernamePasswordAuthenticationToken(account, existingAccount.getPassword(), account.getAuthorities());
        }
        throw new BadCredentialsException("Password did not match");
    }
}
