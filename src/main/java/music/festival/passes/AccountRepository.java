package music.festival.passes;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by bryce_fisher on 1/11/17.
 */
@Repository
public interface AccountRepository extends PagingAndSortingRepository<Account, Long> {
    @Transactional
    Account findByCityWeeklyTicketId(Long ticketId);

    @Transactional
    List<Account> findByWristbandBadgeIdIsNotNull();
}
