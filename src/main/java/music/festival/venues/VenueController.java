package music.festival.venues;

import music.festival.file.ImageEntityController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by bryce_fisher on 1/13/17.
 */
@RestController
@RequestMapping("/venue")
public class VenueController extends ImageEntityController<Venue> {
    public VenueController(@Autowired VenueRepository venueRepository) {
        super(venueRepository);
    }
}
