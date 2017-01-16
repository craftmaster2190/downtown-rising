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
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    RoleService roleService;

    @RequestMapping(path = "/login", method = RequestMethod.POST,
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<User> login(Login login,
                                      HttpServletRequest request, HttpServletResponse response) {
        return login(login.getUsername(), login.getPassword(), request, response);
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<User> login(@RequestParam String username, @RequestParam String password,
                                      HttpServletRequest request, HttpServletResponse response) {
        User user = userRepository.findByEmail(username);
        if (user != null) {
            try {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(user, password, user.getAuthorities());

                authenticationManager.authenticate(usernamePasswordAuthenticationToken);

                if (usernamePasswordAuthenticationToken.isAuthenticated()) {
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }

                request.getSession(true);
            } catch (Exception e) {
                SecurityContextHolder.getContext().setAuthentication(null);
                e.printStackTrace();
                user = null;
            }
        }

        if (user == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(user, HttpStatus.ACCEPTED);
    }

    @GetMapping("/")
    public ResponseEntity<User> currentUser(@AuthenticationPrincipal User user) {
        if (user == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        }
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        SecurityContextHolder.getContext().setAuthentication(null);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        if (userService.userExists(user.getUsername()))
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);

        user.setRoles(roleService.getDefaultRoles());

        User newUser = userService.saveOrUpdate(user);
        return new ResponseEntity<>(newUser, HttpStatus.ACCEPTED);
    }

}
