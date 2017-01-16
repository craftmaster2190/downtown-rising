package music.festival.comment;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by bryce_fisher on 1/15/17.
 */
@Repository
public interface CommentRepository extends PagingAndSortingRepository<Comment, Long> {
}
