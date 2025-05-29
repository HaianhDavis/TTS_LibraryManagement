package com.example.TTS_LibraryManagement.controller;

import com.example.TTS_LibraryManagement.dto.request.Category.CategoryCreationRequest;
import com.example.TTS_LibraryManagement.dto.request.Category.CategoryUpdateRequest;
import com.example.TTS_LibraryManagement.dto.response.ApiResponse;
import com.example.TTS_LibraryManagement.dto.response.Dashboard.DashboardBookResponse;
import com.example.TTS_LibraryManagement.dto.response.Role.RoleResponse;
import com.example.TTS_LibraryManagement.dto.response.Category.CategoryResponse;
import com.example.TTS_LibraryManagement.service.CategoryService;
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
public class CategoryController {
    CategoryService categoryService;

    @PostMapping("/create")
    @PreAuthorize("fileRole(#httpServletRequest)")
    ApiResponse<CategoryResponse> createCategory(@RequestBody @Valid CategoryCreationRequest request) {
        ApiResponse<CategoryResponse> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Successfully created Category");
        apiResponse.setResult(categoryService.createCategory(request));
        return apiResponse;
    }

    @GetMapping
    @PreAuthorize("fileRole(#httpServletRequest)")
    ApiResponse<List<CategoryResponse>> getCategories() {
        ApiResponse<List<CategoryResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Successfully retrieved categories");
        apiResponse.setResult(categoryService.getCategories());
        return apiResponse;
    }

    @GetMapping("/detail/{id}")
    @PreAuthorize("fileRole(#httpServletRequest)")
    ApiResponse<CategoryResponse> getCategoryById(@PathVariable("id") Long CategoryId) {
        ApiResponse<CategoryResponse> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Successfully retrieved Category with ID " + CategoryId);
        apiResponse.setResult(categoryService.getCategoryById(CategoryId));
        return apiResponse;
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("fileRole(#httpServletRequest)")
    ApiResponse<CategoryResponse> updateCategory(@PathVariable Long id, @RequestBody CategoryUpdateRequest request) {
        ApiResponse<CategoryResponse> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Successfully updated Category");
        apiResponse.setResult(categoryService.updateCategory(id, request));
        return apiResponse;
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("fileRole(#httpServletRequest)")
    ApiResponse<CategoryResponse> deleteCategory(@PathVariable Long id) {
        ApiResponse<CategoryResponse> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Successfully deleted Category with ID: " + id);
        categoryService.deleteCategory(id);
        return apiResponse;
    }

    @GetMapping("/search")
    @PreAuthorize("fileRole(#httpServletRequest)")
    ApiResponse<Page<CategoryResponse>> searchCategory(@RequestParam(name = "pageNo") int pageNo, @RequestParam(name = "pageSize") int pageSize) {
        ApiResponse<Page<CategoryResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Successfully retrieved categories");
        apiResponse.setResult(categoryService.getCategoriesByPage(pageNo, pageSize));
        return apiResponse;
    }

    @PatchMapping("/restore/{id}")
    @PreAuthorize("fileRole(#httpServletRequest)")
    ApiResponse<RoleResponse> restoreCategory(@PathVariable Long id) {
        ApiResponse<RoleResponse> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Successfully restored Category with id " + id);
        categoryService.restoreCategory(id);
        return apiResponse;
    }

    @GetMapping("/statistics")
    @PreAuthorize("fileRole(#httpServletRequest)")
    ApiResponse<List<DashboardBookResponse>> getStatistics() {
        ApiResponse<List<DashboardBookResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Successfully created comment!");
        apiResponse.setResult(categoryService.getStatisticsByCategory());
        return apiResponse;
    }
}
