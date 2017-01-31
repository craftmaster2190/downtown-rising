package music.festival.comment;

import music.festival.user.Account;
import music.festival.user.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by bryce_fisher on 1/15/17.
 */
@RestController
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    CommentRepository commentRepository;

    @Autowired
    AccountService accountService;
    
    @PostMapping
    public ResponseEntity<Comment> add(@RequestBody Comment comment, @AuthenticationPrincipal Account account) {
        if (comment != null) {
            if (account != null) {
                account = accountService.findById(account.getId());
                comment.setLoggedInAccount(account);
            }
            comment = commentRepository.save(comment);
            return new ResponseEntity<>(comment, HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
