package music.festival.passes;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by bryce_fisher on 1/11/17.
 */
@Repository
public interface PassRepository extends PagingAndSortingRepository<Pass, Long> {
    Pass findByCityWeeklyTicketId(String cityWeeklyTicketId);

    Pass findByWristbandBadgeId(String wristbandBadgeId);

    List<Pass> findByAccount(Long accountId);
}
