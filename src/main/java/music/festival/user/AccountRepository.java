package music.festival.user;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

/**
 * Created by bryce_fisher on 1/11/17.
 */
@Repository
public interface AccountRepository extends PagingAndSortingRepository<Account, Long> {
    @Transactional
    Account findByEmail(String email);
}
