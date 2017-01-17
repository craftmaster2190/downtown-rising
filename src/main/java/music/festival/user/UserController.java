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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @RequestMapping(path = "/login", method = RequestMethod.POST,
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<User> login(@RequestBody LoginRequest loginRequest,
                                      HttpServletRequest request, HttpServletResponse response) {
        return login(loginRequest.getUsername(), loginRequest.getPassword(), request, response, true);
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<User> login(@RequestParam String username, @RequestParam String password,
                                      HttpServletRequest request, HttpServletResponse response) {
        return login(username, password, request, response, true);
    }

    public ResponseEntity<User> login(String username, String password,
                                      HttpServletRequest request, HttpServletResponse response, boolean yolo) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            User user = (User) (authentication.getPrincipal());
            return new ResponseEntity<>(user, HttpStatus.ALREADY_REPORTED);
        }

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
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<User> currentUser(@AuthenticationPrincipal User user) {
        if (user == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
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
    public ResponseEntity<User> register(@RequestBody User user) {
        if (userService.userExists(user.getUsername()))
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);

        user.setRoles(roleService.getDefaultRoles());

        User newUser = userService.saveOrUpdate(user);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @PostMapping("/save")
    public ResponseEntity<User> save(@RequestBody User user) {
        System.out.println("user " + user.getId());
        user = userRepository.findOne(user.getId());
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User loggedInUser = (User) (authentication.getPrincipal());
        if (authentication instanceof AnonymousAuthenticationToken || loggedInUser.getId() != user.getId()) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        user.setRoles(loggedInUser.getRoles());

        User newUser = userService.saveOrUpdate(user);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @PostMapping("/password")
    public ResponseEntity<User> password(@RequestBody ChangePasswordRequest changePasswordRequest) {
        if (!changePasswordRequest.isValid()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        User user = userRepository.findByEmail(changePasswordRequest.getUsername());
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User loggedInUser = (User) (authentication.getPrincipal());
        if (authentication instanceof AnonymousAuthenticationToken || loggedInUser.getId() != user.getId()) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        String oldPassword = bCryptPasswordEncoder.encode(changePasswordRequest.getOldPassword());
        if (user.getPassword().equals(oldPassword)) {
            String newPassword = bCryptPasswordEncoder.encode(changePasswordRequest.getNewPassword());
            user.setPassword(newPassword);
        }

        User newUser = userRepository.save(user);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

}
