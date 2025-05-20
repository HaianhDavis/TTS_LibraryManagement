package com.example.TTS_LibraryManagement.service;

import com.example.TTS_LibraryManagement.dto.request.Category.CategoryCreationRequest;
import com.example.TTS_LibraryManagement.dto.request.Category.CategoryUpdateRequest;
import com.example.TTS_LibraryManagement.dto.response.Category.CategoryResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CategoryService {
    CategoryResponse createCategory(CategoryCreationRequest request);
    CategoryResponse updateCategory(Long CategoryId, CategoryUpdateRequest request);
    void deleteCategory(Long CategoryId);
    void restoreCategory(Long CategoryId);
    List<CategoryResponse> getCategories();
    CategoryResponse getCategoryById(Long id);
    Page<CategoryResponse> getCategoriesByPage(int page, int size);
}
