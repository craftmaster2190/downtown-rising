package music.festival.file;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * Created by bryce_fisher on 1/12/17.
 */
@RestController
@RequestMapping("/files")
public class ServableFileController {

    @Autowired
    ServableFileRepository servableFileRepository;

    @GetMapping("/{id}")
    public ResponseEntity<Resource> serveFile(@PathVariable Long id) {
        ServableFile servableFile = servableFileRepository.findOne(id);
        if (servableFile == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        Resource file = servableFile.getAsResource();
        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }
}
