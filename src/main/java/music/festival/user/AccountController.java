package music.festival.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by bryce_fisher on 1/15/17.
 */
@RestController
@RequestMapping("/auth")
public class AccountController {
    @Autowired
    AccountService accountService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    RoleService roleService;


    @RequestMapping(path = "/login", method = RequestMethod.POST,
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Account> login(@RequestBody LoginRequest loginRequest,
                                         HttpServletRequest request, HttpServletResponse response) {
        return login(loginRequest.getUsername(), loginRequest.getPassword(), request, response, true);
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Account> login(@RequestParam String username, @RequestParam String password,
                                         HttpServletRequest request, HttpServletResponse response) {
        return login(username, password, request, response, true);
    }

    public ResponseEntity<Account> login(String username, String password,
                                         HttpServletRequest request, HttpServletResponse response, boolean yolo) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            Account account = (Account) (authentication.getPrincipal());
            return new ResponseEntity<>(account, HttpStatus.ALREADY_REPORTED);
        }

        Account account = accountService.findByEmail(username);
        if (account != null) {
            try {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = accountService.getUsernamePasswordAuthenticationToken(account, password);

                authenticationManager.authenticate(usernamePasswordAuthenticationToken);

                if (usernamePasswordAuthenticationToken.isAuthenticated()) {
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }

                request.getSession(true);
            } catch (Exception e) {
                SecurityContextHolder.getContext().setAuthentication(null);
                account = null;
            }
        }

        if (account == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(account, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Account> currentUser(@AuthenticationPrincipal Account account, HttpServletRequest request, HttpServletResponse response) {
        if (account == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        Account accountFromDB = accountService.findById(account.getId());
        if (accountFromDB == null) {
            return logout(request, response);
        }
        return new ResponseEntity<>(accountFromDB, HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<Account> logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) {
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        }
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        SecurityContextHolder.getContext().setAuthentication(null);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PostMapping("/register")
    public ResponseEntity<Account> register(@RequestBody Account account) {
        if (accountService.userExists(account.getUsername()))
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);

        account.setRoles(roleService.getDefaultRoles());

        Account newAccount = accountService.saveAndUpdatePassword(account);
        return new ResponseEntity<>(newAccount, HttpStatus.CREATED);
    }

    @PostMapping("/save")
    public ResponseEntity<Account> save(@RequestBody Account accountToUpdate) {
        Account currentAccountInDB = accountService.findById(accountToUpdate.getId());
        if (currentAccountInDB == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account loggedInAccount = (Account) (authentication.getPrincipal());
        if (authentication instanceof AnonymousAuthenticationToken || loggedInAccount.getId() != accountToUpdate.getId()) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        accountToUpdate.setRoles(loggedInAccount.getRoles());

        Account newAccount = accountService.updateWithoutChangingPassword(accountToUpdate);
        return new ResponseEntity<>(newAccount, HttpStatus.CREATED);
    }

    @PostMapping("/password")
    public ResponseEntity<Account> password(@RequestBody ChangePasswordRequest changePasswordRequest) {
        if (!changePasswordRequest.isValid()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Account account = accountService.findByEmail(changePasswordRequest.getUsername());
        if (account == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account loggedInAccount = (Account) (authentication.getPrincipal());
        if (authentication instanceof AnonymousAuthenticationToken || loggedInAccount.getId() != account.getId()) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        String oldPassword = changePasswordRequest.getOldPassword();
        if (account.getPassword().equals(oldPassword)) {
            String newPassword = changePasswordRequest.getNewPassword();
            account.setPassword(newPassword);
        }

        Account newAccount = accountService.saveAndUpdatePassword(account);
        return new ResponseEntity<>(newAccount, HttpStatus.CREATED);
    }

}
