package music.festival.file;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Created by bryce_fisher on 1/21/17.
 */
@RestController
@RequestMapping("/page")
public class EditablePageController {

    @Autowired
    EditablePageRepository editablePageRepository;

    @Autowired
    ImageResizerService imageResizerService;

    @Autowired
    EditablePageImageRepository editablePageImageRepository;

    @Autowired
    ServableImageRepository servableImageRepository;

    @GetMapping("/{path}")
    public ResponseEntity<EditablePage> get(@PathVariable String path) {
        EditablePage editablePage = editablePageRepository.findByPath(path);
        if (editablePage == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(editablePage, HttpStatus.OK);
    }

    @PostMapping
    @PutMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<EditablePage> save(@RequestBody EditablePage editablePage) {
        EditablePage oldEditablePage = editablePageRepository.findByPath(editablePage.getPath());
        if (oldEditablePage != null) {
            editablePage.setId(oldEditablePage.getId());
            editablePage.setImages(oldEditablePage.getImages());
        }
        return new ResponseEntity<>(editablePageRepository.save(editablePage), HttpStatus.OK);
    }

    @PostMapping("/{id}/image/add")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<EditablePage> addImage(@PathVariable Long id) {
        EditablePage editablePage = editablePageRepository.findOne(id);
        if (editablePage == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        editablePage.getImages().add(new EditablePageImage());
        return new ResponseEntity<>(editablePageRepository.save(editablePage), HttpStatus.OK);
    }

    @PostMapping("/image/{imageId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<EditablePageImage> handlePictureUpload(@PathVariable Long imageId,
                                                                 @RequestParam("file") MultipartFile file) {
        EditablePageImage editablePageImage = editablePageImageRepository.findOne(imageId);
        if (editablePageImage == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        try {
            byte[] scaledImageBytes = imageResizerService.scaleImage(file.getBytes());
            ServableImage servableImage = editablePageImage.getImage();
            if (servableImage != null && servableImage.getId() != null)
                servableImageRepository.delete(servableImage.getId());
            servableImage = new ServableImage();

            servableImage.setBytes(scaledImageBytes);
            editablePageImage.setImage(servableImage);

            editablePageImage = editablePageImageRepository.save(editablePageImage);
            return new ResponseEntity<>(editablePageImage, HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>(editablePageImage, HttpStatus.BAD_REQUEST);
        }
    }
}
