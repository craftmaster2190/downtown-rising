package music.festival.passes;

import music.festival.user.Account;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by bryce_fisher on 1/11/17.
 */
@Repository
public interface PassRepository extends PagingAndSortingRepository<Pass, Long> {
    @Transactional
    Pass findByCityWeeklyTicketId(String cityWeeklyTicketId);

    @Transactional
    Pass findByWristbandBadgeId(String wristbandBadgeId);

    @Transactional
    List<Pass> findByAccount(Account account);

    @Transactional
    List<Pass> findByAccountAndTicketTypeNotNull(Account account);
}
