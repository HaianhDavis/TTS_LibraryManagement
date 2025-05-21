package com.example.TTS_LibraryManagement.service.impl;

import com.example.TTS_LibraryManagement.dto.request.Category.CategoryCreationRequest;
import com.example.TTS_LibraryManagement.dto.request.Category.CategoryUpdateRequest;
import com.example.TTS_LibraryManagement.dto.response.Category.CategoryResponse;
import com.example.TTS_LibraryManagement.dto.response.Dashboard.DashboardBookResponse;
import com.example.TTS_LibraryManagement.entity.Category;
import com.example.TTS_LibraryManagement.exception.AppException;
import com.example.TTS_LibraryManagement.exception.ErrorCode;
import com.example.TTS_LibraryManagement.mapper.CategoryMapper;
import com.example.TTS_LibraryManagement.repository.CategoryRepo;
import com.example.TTS_LibraryManagement.service.CategoryService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryServiceImpl implements CategoryService {
    CategoryRepo categoryRepo;
    CategoryMapper categoryMapper;

    @Transactional
    public CategoryResponse createCategory(CategoryCreationRequest request) {
        if (categoryRepo.existsByCode(request.getCategoryCode()))
            throw new AppException(ErrorCode.CATEGORY_EXISTED);
        Category category = categoryMapper.toCategoryCreate(request);
        category.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        return categoryMapper.toCategoryResponse(categoryRepo.save(category));
    }

    @Transactional
    public CategoryResponse updateCategory(Long CategoryId, CategoryUpdateRequest request) {
        Category category = categoryRepo.findCategoryByIdAndIsDeletedFalse(CategoryId)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
        CategoryUpdateRequest currentCategoryDto = categoryMapper.toCategoryUpdateRequest(category);
        if(request.equals(currentCategoryDto)){
            throw new AppException(ErrorCode.CATEGORY_NOT_CHANGED);
        }
        categoryMapper.toCategoryUpdate(category, request);
        category.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
        return categoryMapper.toCategoryResponse(categoryRepo.save(category));
    }

    @Transactional
    public void deleteCategory(Long CategoryId) {
        Category category = categoryRepo
                .findCategoryByIdAndIsDeletedFalse(CategoryId)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
        category.setIsDeleted(1);
        category.setDeletedAt(Timestamp.valueOf(LocalDateTime.now()));
        categoryRepo.save(category);
    }

    @Transactional
    public void restoreCategory(Long CategoryId) {
        Category category = categoryRepo
                .findById(CategoryId)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
        if (category.getIsDeleted() == 0) {
            throw new AppException(ErrorCode.CATEGORY_NOT_DELETED);
        }
        category.setIsDeleted(0);
        category.setDeletedAt(null);
        category.setDeletedBy(null);
        categoryRepo.save(category);
    }

    @Transactional
    public List<CategoryResponse> getCategories() {
        List<Category> categories = categoryRepo.findAllByIsDeletedFalse();
        if (categories.isEmpty()) {
            throw new AppException(ErrorCode.CATEGORY_NOT_FOUND);
        }
        return categories.stream().map(categoryMapper::toCategoryResponse).collect(Collectors.toList());
    }

    @Transactional
    public CategoryResponse getCategoryById(Long id) {
        return categoryMapper.toCategoryResponse(categoryRepo
                .findCategoryByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND)));
    }

    @Transactional
    public Page<CategoryResponse> getCategoriesByPage(int page, int size) {
        if (page < 1) {
            throw new AppException(ErrorCode.PAGE_NO_ERROR);
        }
        if (size < 1) {
            throw new AppException(ErrorCode.PAGE_SIZE_ERROR);
        }
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Category> categories = categoryRepo.searchByIsDeletedFalseAndPageable(pageable);
        if (categories.isEmpty()) {
            throw new AppException(ErrorCode.ROLE_NOT_FOUND);
        }
        return categories.map(categoryMapper::toCategoryResponse);
    }

    public List<DashboardBookResponse> getStatisticsByCategory() {
        List<Object[]> categoryStats = categoryRepo.getBookStatsByCategory();
        return categoryStats.stream().map(objects -> {
            DashboardBookResponse dashboardBookResponse = new DashboardBookResponse();
            dashboardBookResponse.setCategoryId(Long.valueOf(objects[0].toString()));
            dashboardBookResponse.setCategoryName(objects[1].toString());
            dashboardBookResponse.setTotalBooks(Integer.parseInt(objects[2].toString()));
            return dashboardBookResponse;
        }).collect(Collectors.toList());
    }
}
