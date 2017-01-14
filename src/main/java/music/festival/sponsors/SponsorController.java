package music.festival.sponsors;

import music.festival.file.ServableFile;
import music.festival.file.ServableFileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bryce_fisher on 1/12/17.
 */
@RestController
@RequestMapping("/sponsor")
public class SponsorController {

    private static final Logger logger = LoggerFactory.getLogger(SponsorController.class);

    @Autowired
    SponsorRepository sponsorRepository;

    @Autowired
    ServableFileRepository servableFileRepository;

    @GetMapping
    public ResponseEntity<List<Sponsor>> getAll() {
        List<Sponsor> sponsors = new ArrayList<>();
        sponsorRepository.findAll().forEach(sponsors::add);
        if (sponsors.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(sponsors, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sponsor> getOne(@PathVariable Long id) {
        Sponsor sponsor = sponsorRepository.findOne(id);
        if (sponsor == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(sponsor, HttpStatus.OK);
    }

    @PostMapping
    @PutMapping
    public ResponseEntity<Sponsor> save(@RequestBody Sponsor sponsor) {
        if (sponsor == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        else if (sponsor.getId() != null) {
            Sponsor currentSponsor = sponsorRepository.findOne(sponsor.getId());
            if (currentSponsor != null)
                sponsor.setImage(currentSponsor.getImage());
        }
        sponsor = sponsorRepository.save(sponsor);
        return new ResponseEntity<>(sponsor, HttpStatus.ACCEPTED);
    }

    @PostMapping("/{id}")
    public ResponseEntity<Sponsor> handlePictureUpload(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        Sponsor sponsor = sponsorRepository.findOne(id);
        if (sponsor == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        try {
            sponsor.setImage(new ServableFile(file));
            sponsor = sponsorRepository.save(sponsor);
            return new ResponseEntity<>(sponsor, HttpStatus.ACCEPTED);
        } catch (IOException e) {
            return new ResponseEntity<>(sponsor, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Sponsor> deleteOne(@PathVariable Long id) {
        Sponsor sponsor = sponsorRepository.findOne(id);
        if (sponsor == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        sponsorRepository.delete(sponsor);
        return new ResponseEntity<>(sponsor, HttpStatus.ACCEPTED);

    }
}
