package music.festival.user;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

/**
 * Created by bryce_fisher on 1/16/17.
 */
@Repository
public interface RoleRepository extends PagingAndSortingRepository<Role, Long> {
    @Transactional
    Role findByName(String name);
}
