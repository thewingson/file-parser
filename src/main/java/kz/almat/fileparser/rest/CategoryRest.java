package kz.almat.fileparser.rest;

import kz.almat.fileparser.model.Category;
import kz.almat.fileparser.repo.CategoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Almat on 31.07.2020
 */

@RestController
@RequestMapping("/api/category")
public class CategoryRest {

    private final CategoryRepo categoryRepo;

    @Autowired
    public CategoryRest(CategoryRepo categoryRepo) {
        this.categoryRepo = categoryRepo;
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        try {
            return ResponseEntity.ok(categoryRepo.findAll());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> add(@RequestBody Category category) {
        try {
            categoryRepo.save(category);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
