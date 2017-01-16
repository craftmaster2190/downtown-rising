package music.festival.user;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by bryce_fisher on 1/16/17.
 */
@Repository
public interface RoleRepository extends PagingAndSortingRepository<Role, Long> {
    Role findByName(String name);
}
