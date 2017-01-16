package music.festival.file;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;


/**
 * Created by bryce_fisher on 1/12/17.
 */
@RestController
@RequestMapping("/files")
public class ServableImageController {

    /**
     * Images are probably safe to be cached for up to an hour. This is probably overkill. We could do one day.
     */
    private static final CacheControl CACHE_CONTROL = CacheControl.maxAge(1, TimeUnit.HOURS).cachePublic().mustRevalidate();

    @Autowired
    ServableImageRepository servableImageRepository;


    @GetMapping("/{id}")
    public ResponseEntity<Resource> serveFile(@PathVariable Long id) {
        ServableImage servableImage = servableImageRepository.findOne(id);
        if (servableImage == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        Resource file = servableImage.getAsResource();
        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + servableImage.getId() + ".png\"")
                .cacheControl(CACHE_CONTROL)
                .body(file);
    }
}
