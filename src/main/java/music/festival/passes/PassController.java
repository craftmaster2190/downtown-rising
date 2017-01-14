package music.festival.passes;

import music.festival.file.ImageEntityController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by bryce_fisher on 1/13/17.
 */
@RestController
@RequestMapping("/pass")
public class PassController extends ImageEntityController<Pass> {
    public PassController(@Autowired PassRepository passRepository) {
        super(passRepository);
    }
}
