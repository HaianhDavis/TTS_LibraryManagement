package com.example.TTS_LibraryManagement.controller;

import com.example.TTS_LibraryManagement.dto.request.Category.CategoryCreationRequest;
import com.example.TTS_LibraryManagement.dto.request.Category.CategoryUpdateRequest;
import com.example.TTS_LibraryManagement.dto.response.ApiResponse;
import com.example.TTS_LibraryManagement.dto.response.Dashboard.DashboardBookResponse;
import com.example.TTS_LibraryManagement.dto.response.Role.RoleResponse;
import com.example.TTS_LibraryManagement.dto.response.Category.CategoryResponse;
import com.example.TTS_LibraryManagement.service.CategoryService;
import com.example.TTS_LibraryManagement.utils.ApiUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Category", description = "Category Management APIs")
public class CategoryController {
    CategoryService categoryService;

    @Operation(summary = "Create a new category")
    @PostMapping("/create")
    @PreAuthorize("fileRole(#httpServletRequest)")
    ApiResponse<CategoryResponse> createCategory(HttpServletRequest httpServletRequest, @RequestBody @Valid CategoryCreationRequest request) {
       return ApiUtils.success(categoryService.createCategory(request));
    }

    @Operation(summary = "Get all categories")
    @GetMapping
    @PreAuthorize("fileRole(#httpServletRequest)")
    ApiResponse<List<CategoryResponse>> getCategories(HttpServletRequest httpServletRequest) {
        return ApiUtils.success(categoryService.getCategories());
    }

    @Operation(summary = "Get category by ID")
    @GetMapping("/detail/{id}")
    @PreAuthorize("fileRole(#httpServletRequest)")
    ApiResponse<CategoryResponse> getCategoryById(HttpServletRequest httpServletRequest, @PathVariable("id") Long CategoryId) {
        return ApiUtils.success(categoryService.getCategoryById(CategoryId));
    }

    @Operation(summary = "Update category by ID")
    @PutMapping("/update/{id}")
    @PreAuthorize("fileRole(#httpServletRequest)")
    ApiResponse<CategoryResponse> updateCategory(HttpServletRequest httpServletRequest, @PathVariable Long id, @RequestBody @Valid CategoryUpdateRequest request) {
        return ApiUtils.success(categoryService.updateCategory(id, request));
    }

    @Operation(summary = "Delete category by ID")
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("fileRole(#httpServletRequest)")
    ApiResponse<CategoryResponse> deleteCategory(HttpServletRequest httpServletRequest, @PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ApiUtils.successDeleteOrRestore("Successfully deleted Category with ID: " + id);
    }

    @Operation(summary = "Search categories with pagination")
    @GetMapping("/search")
    @PreAuthorize("fileRole(#httpServletRequest)")
    ApiResponse<Page<CategoryResponse>> searchCategory(HttpServletRequest httpServletRequest, @RequestParam(name = "pageNo") int pageNo, @RequestParam(name = "pageSize") int pageSize) {
        return ApiUtils.success(categoryService.getCategoriesByPage(pageNo, pageSize));
    }

    @Operation(summary = "Restore category by ID")
    @PatchMapping("/restore/{id}")
    @PreAuthorize("fileRole(#httpServletRequest)")
    ApiResponse<RoleResponse> restoreCategory(HttpServletRequest httpServletRequest, @PathVariable Long id) {
        categoryService.restoreCategory(id);
        return ApiUtils.successDeleteOrRestore("Successfully restored Category with id " + id);
    }

    @Operation(summary = "Get statistics by category")
    @GetMapping("/statistics")
    @PreAuthorize("fileRole(#httpServletRequest)")
    ApiResponse<List<DashboardBookResponse>> getStatistics(HttpServletRequest httpServletRequest) {
        return ApiUtils.success(categoryService.getStatisticsByCategory());
    }
}
