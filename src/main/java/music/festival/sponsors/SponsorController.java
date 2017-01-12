package music.festival.sponsors;

import music.festival.file.ServableFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Created by bryce_fisher on 1/12/17.
 */
@RestController
@RequestMapping("/sponsor")
public class SponsorController {

    private static final Logger logger = LoggerFactory.getLogger(SponsorController.class);

    @Autowired
    SponsorRepository sponsorRepository;


    @PostMapping("/{id}")
    public ResponseEntity<?> handlePictureUpload(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        Sponsor sponsor = sponsorRepository.findOne(id);
        if (sponsor == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        try {
            sponsor.setImage(new ServableFile(file));
            return new ResponseEntity<>(sponsor, HttpStatus.ACCEPTED);
        } catch (IOException e) {
            return new ResponseEntity<>(sponsor, HttpStatus.BAD_REQUEST);
        }
    }
}
