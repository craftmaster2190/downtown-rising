package music.festival.venues;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by bryce_fisher on 1/12/17.
 */
@Repository
public interface VenueRepository extends PagingAndSortingRepository<Venue, Long> {
    List<Venue> findFirst8ByNameContainingIgnoreCase(String search);
}
