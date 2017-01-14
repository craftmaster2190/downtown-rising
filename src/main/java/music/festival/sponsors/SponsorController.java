package music.festival.sponsors;

import music.festival.file.ImageEntityController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by bryce_fisher on 1/12/17.
 */
@RestController
@RequestMapping("/sponsor")
public class SponsorController extends ImageEntityController<Sponsor> {
    public SponsorController(@Autowired SponsorRepository sponsorRepository) {
        super(sponsorRepository);
    }
}
