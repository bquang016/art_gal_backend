package com.example.art_gal.service;

import com.example.art_gal.payload.CategoryDto;
import java.util.List;

public interface CategoryService {
    CategoryDto createCategory(CategoryDto categoryDto);
    List<CategoryDto> getAllCategories();
    CategoryDto getCategoryById(long id);
    CategoryDto updateCategory(CategoryDto categoryDto, long id);
    void deleteCategory(long id);
}