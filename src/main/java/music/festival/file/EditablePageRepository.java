package music.festival.file;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

/**
 * Created by bryce_fisher on 1/21/17.
 */
@Repository
public interface EditablePageRepository extends PagingAndSortingRepository<EditablePage, Long> {
    @Transactional
    EditablePage findByPath(String path);
}
