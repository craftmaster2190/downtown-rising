package music.festival.file;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by bryce_fisher on 1/21/17.
 */
@Repository
public interface EditablePageImageRepository extends PagingAndSortingRepository<EditablePageImage, Long> {
}
