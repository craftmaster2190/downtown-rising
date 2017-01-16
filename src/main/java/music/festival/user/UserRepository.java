package music.festival.user;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by bryce_fisher on 1/11/17.
 */
@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Long> {
    User findByEmail(String username);
}
