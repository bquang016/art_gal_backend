package com.example.art_gal.controller;

import com.example.art_gal.payload.CategoryDto;
import com.example.art_gal.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // API tạo Danh mục mới (Admin hoặc Nhân viên)
    @PreAuthorize("hasAnyRole('ADMIN', 'NHANVIEN')")
    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@RequestBody CategoryDto categoryDto){
        return new ResponseEntity<>(categoryService.createCategory(categoryDto), HttpStatus.CREATED);
    }

    // API lấy tất cả Danh mục
    @GetMapping
    public List<CategoryDto> getAllCategories(){
        return categoryService.getAllCategories();
    }

    // API lấy Danh mục theo ID
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable(name = "id") long id){
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    // API cập nhật Danh mục (Admin hoặc Nhân viên)
    @PreAuthorize("hasAnyRole('ADMIN', 'NHANVIEN')")
    @PutMapping("/{id}")
    public ResponseEntity<CategoryDto> updateCategory(@RequestBody CategoryDto categoryDto, @PathVariable(name = "id") long id){
        CategoryDto categoryResponse = categoryService.updateCategory(categoryDto, id);
        return new ResponseEntity<>(categoryResponse, HttpStatus.OK);
    }

    // API xóa Danh mục (Admin hoặc Nhân viên)
    @PreAuthorize("hasAnyRole('ADMIN', 'NHANVIEN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable(name = "id") long id){
        categoryService.deleteCategory(id);
        return new ResponseEntity<>("Category entity deleted successfully.", HttpStatus.OK);
    }
}