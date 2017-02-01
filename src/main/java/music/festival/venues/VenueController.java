package music.festival.venues;

import music.festival.file.ImageEntityController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by bryce_fisher on 1/13/17.
 */
@RestController
@RequestMapping("/venue")
public class VenueController extends ImageEntityController<Venue> {
    public VenueController(@Autowired VenueRepository venueRepository) {
        super(venueRepository);
    }

    @GetMapping("/search/{search}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<Venue>> search(@PathVariable String search) {
        List<Venue> venues = ((VenueRepository) repository).findFirst8ByNameContainingIgnoreCase(search);
        if (venues == null || venues.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(venues, HttpStatus.OK);
    }
}
