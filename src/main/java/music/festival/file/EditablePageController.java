package music.festival.file;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;

/**
 * Created by bryce_fisher on 1/21/17.
 */
@RestController
@RequestMapping("/page")
public class EditablePageController {

    @Autowired
    EditablePageRepository editablePageRepository;

    @GetMapping("/{path}")
    @Transactional
    public ResponseEntity<EditablePage> get(@PathVariable String path) {
        EditablePage editablePage = editablePageRepository.findByPath(path);
        if (editablePage == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(editablePage, HttpStatus.OK);
    }

    @PostMapping
    @PutMapping
    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<EditablePage> save(@RequestBody EditablePage editablePage) {
        EditablePage oldEditablePage = editablePageRepository.findByPath(editablePage.getPath());
        if (oldEditablePage != null)
            editablePage.setId(oldEditablePage.getId());
        return new ResponseEntity<>(editablePageRepository.save(editablePage), HttpStatus.OK);
    }
}
