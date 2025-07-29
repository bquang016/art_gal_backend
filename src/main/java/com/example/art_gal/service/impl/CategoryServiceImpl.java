package com.example.art_gal.service.impl;

import com.example.art_gal.entity.Category;
import com.example.art_gal.entity.User;
import com.example.art_gal.exception.ResourceNotFoundException;
import com.example.art_gal.payload.CategoryDto;
import com.example.art_gal.repository.CategoryRepository;
import com.example.art_gal.repository.UserRepository;
import com.example.art_gal.service.ActivityLogService;
import com.example.art_gal.service.CategoryService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final ActivityLogService activityLogService;

    public CategoryServiceImpl(CategoryRepository categoryRepository, UserRepository userRepository, ActivityLogService activityLogService) {
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.activityLogService = activityLogService;
    }

    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", 0));
    }

    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        Category category = mapToEntity(categoryDto);
        Category newCategory = categoryRepository.save(category);
        
        activityLogService.logActivity(getCurrentUser(), "TẠO DANH MỤC", "Đã tạo danh mục mới: " + newCategory.getName());

        return mapToDTO(newCategory);
    }

    @Override
    public List<CategoryDto> getAllCategories() {
        return categoryRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public CategoryDto getCategoryById(long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
        return mapToDTO(category);
    }

    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto, long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));

        category.setName(categoryDto.getName());
        category.setDescription(categoryDto.getDescription());

        Category updatedCategory = categoryRepository.save(category);
        
        activityLogService.logActivity(getCurrentUser(), "CẬP NHẬT DANH MỤC", "Đã cập nhật danh mục: " + updatedCategory.getName());

        return mapToDTO(updatedCategory);
    }

    @Override
    public void deleteCategory(long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
        
        activityLogService.logActivity(getCurrentUser(), "XÓA DANH MỤC", "Đã xóa danh mục: " + category.getName());
        
        categoryRepository.delete(category);
    }

    private CategoryDto mapToDTO(Category category){
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(category.getId());
        categoryDto.setName(category.getName());
        categoryDto.setDescription(category.getDescription());
        return categoryDto;
    }

    private Category mapToEntity(CategoryDto categoryDto){
        Category category = new Category();
        category.setName(categoryDto.getName());
        category.setDescription(categoryDto.getDescription());
        return category;
    }
}