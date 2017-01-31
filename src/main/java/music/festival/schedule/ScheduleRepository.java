package music.festival.schedule;

import music.festival.user.Account;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by bryce_fisher on 1/12/17.
 */
@Repository
public interface ScheduleRepository extends PagingAndSortingRepository<Schedule, Long> {
    @Transactional
    List<Schedule> findByAccount(Account account);
}
