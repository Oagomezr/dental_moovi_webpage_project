package com.dentalmoovi.website.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.dentalmoovi.website.models.dtos.MessageDTO;
import com.dentalmoovi.website.models.responses.CategoriesResponse;
import com.dentalmoovi.website.services.CategoriesSer;

@RestController
@RequestMapping
public class CategoriesController {
    private final CategoriesSer categoriesSer;

    public CategoriesController(CategoriesSer categoriesSer) {
        this.categoriesSer = categoriesSer;
    }

    @GetMapping("/public/categories")
    public ResponseEntity<CategoriesResponse> getAllCategories() {
        try{
            return ResponseEntity.ok(categoriesSer.getAllCategories());
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/api/public/categories")
    public ResponseEntity<CategoriesResponse> getAllCategories2() {
        try{
            return ResponseEntity.ok(categoriesSer.getAllCategories());
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/api/admin/categories/updateName/{categoryName}")
    public ResponseEntity<MessageDTO> updateCategoryName(@PathVariable("categoryName") String categoryName, @RequestBody String newName) {
        try{
            return ResponseEntity.ok(categoriesSer.updateCategoryName(categoryName, newName));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PutMapping("/api/admin/categories/updateLocation/{categoryName}")
    public ResponseEntity<MessageDTO> updateCategoryLocation(@PathVariable("categoryName") String categoryName, @RequestBody String newName) {
        try{
            return ResponseEntity.ok(categoriesSer.updateCategoryPosition(categoryName, newName));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/api/admin/categories/create/{parentCategory}")
    public ResponseEntity<MessageDTO> createCategory(@PathVariable("parentCategory") String parentCategory, @RequestBody String newCategoryName) {
        try{
            return ResponseEntity.ok(categoriesSer.addCategory(parentCategory, newCategoryName));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/api/admin/categories/delete/{category}")
    public ResponseEntity<MessageDTO> deleteCategory(@PathVariable("category") String category) {
        try{
            return ResponseEntity.ok(categoriesSer.deleteCategory(category));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
