package music.festival.sponsors;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by bryce_fisher on 1/11/17.
 */
@Repository
public interface SponsorRepository extends PagingAndSortingRepository<Sponsor, Long> {
}
