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
    AuthenticationManager authenticationManager;

    @Autowired
    RoleService roleService;


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

        User user = userService.findByEmail(username);
        if (user != null) {
            try {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = userService.getUsernamePasswordAuthenticationToken(user, password);

                authenticationManager.authenticate(usernamePasswordAuthenticationToken);

                if (usernamePasswordAuthenticationToken.isAuthenticated()) {
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }

                request.getSession(true);
            } catch (Exception e) {
                SecurityContextHolder.getContext().setAuthentication(null);
                user = null;
            }
        }

        if (user == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<User> currentUser(@AuthenticationPrincipal User user, HttpServletRequest request, HttpServletResponse response) {
        if (user == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        User userFromDB = userService.findById(user.getId());
        if (userFromDB == null) {
            return logout(request, response);
        }
        return new ResponseEntity<>(userFromDB, HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<User> logout(HttpServletRequest request, HttpServletResponse response) {
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

        User newUser = userService.saveAndUpdatePassword(user);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @PostMapping("/save")
    public ResponseEntity<User> save(@RequestBody User userToUpdate) {
        User currentUserInDB = userService.findById(userToUpdate.getId());
        if (currentUserInDB == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User loggedInUser = (User) (authentication.getPrincipal());
        if (authentication instanceof AnonymousAuthenticationToken || loggedInUser.getId() != userToUpdate.getId()) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        userToUpdate.setRoles(loggedInUser.getRoles());

        User newUser = userService.updateWithoutChangingPassword(userToUpdate);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @PostMapping("/password")
    public ResponseEntity<User> password(@RequestBody ChangePasswordRequest changePasswordRequest) {
        if (!changePasswordRequest.isValid()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        User user = userService.findByEmail(changePasswordRequest.getUsername());
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User loggedInUser = (User) (authentication.getPrincipal());
        if (authentication instanceof AnonymousAuthenticationToken || loggedInUser.getId() != user.getId()) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        String oldPassword = changePasswordRequest.getOldPassword();
        if (user.getPassword().equals(oldPassword)) {
            String newPassword = changePasswordRequest.getNewPassword();
            user.setPassword(newPassword);
        }

        User newUser = userService.saveAndUpdatePassword(user);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

}
