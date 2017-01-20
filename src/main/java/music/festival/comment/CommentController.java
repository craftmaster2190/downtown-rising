package music.festival.comment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by bryce_fisher on 1/15/17.
 */
@RestController
@RequestMapping("comment")
public class CommentController {
    @Autowired
    CommentRepository commentRepository;

    @PutMapping
    @PostMapping
    public ResponseEntity<Comment> add(@RequestBody Comment comment) {
        comment = commentRepository.save(comment);
        return new ResponseEntity<>(comment, HttpStatus.ACCEPTED);
    }
}
