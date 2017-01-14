package music.festival.lineups;

import music.festival.file.ImageEntityController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by bryce_fisher on 1/13/17.
 */
@RestController
@RequestMapping("/lineup")
public class LineUpController extends ImageEntityController<LineUp> {
    public LineUpController(@Autowired LineUpRepository lineUpRepository) {
        super(lineUpRepository);
    }
}
