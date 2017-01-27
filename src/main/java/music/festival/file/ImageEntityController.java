package music.festival.file;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bryce_fisher on 1/12/17.
 */
public abstract class ImageEntityController<T extends ImageEntity> {

    @Autowired
    ServableImageRepository servableImageRepository;

    @Autowired
    ImageResizerService imageResizerService;

    private PagingAndSortingRepository<T, Long> repository;

    public ImageEntityController(PagingAndSortingRepository<T, Long> repository) {
        this.repository = repository;
    }

    @GetMapping
    public ResponseEntity<List<T>> getAll(@RequestParam(defaultValue = "1", required = false) int page,
                                          @RequestParam(defaultValue = "id", required = false) String sort) {
        List<T> tList = new ArrayList<>();
        PageRequest pageRequest = new PageRequest(page, 10, Sort.Direction.ASC, sort);
        repository.findAll(pageRequest).forEach(tList::add);
        if (tList.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(tList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<T> getOne(@PathVariable Long id) {
        T t = repository.findOne(id);
        if (t == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(t, HttpStatus.OK);
    }

    @PostMapping
    @PutMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<T> save(@RequestBody T t) {
        if (t == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        else if (t.getId() != null) {
            T currentSponsor = repository.findOne(t.getId());
            if (currentSponsor != null)
                t.setImage(currentSponsor.getImage());
        }
        t = repository.save(t);
        return new ResponseEntity<>(t, HttpStatus.CREATED);
    }

    @PostMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<T> handlePictureUpload(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        T t = repository.findOne(id);
        if (t == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        try {
            byte[] fileBytes = file.getBytes();
            byte[] scaledImageBytes = imageResizerService.scaleImage(fileBytes);

            ServableImage servableImage = new ServableImage();
            servableImage.setBytes(scaledImageBytes);

            t.setImage(servableImage);
            t = repository.save(t);
            return new ResponseEntity<>(t, HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>(t, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<T> deleteOne(@PathVariable Long id) {
        T t = repository.findOne(id);
        if (t == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        repository.delete(t);
        return new ResponseEntity<>(t, HttpStatus.ACCEPTED);

    }
}
