package com.example.TTS_LibraryManagement.mapper;

import com.example.TTS_LibraryManagement.dto.request.Category.CategoryCreationRequest;
import com.example.TTS_LibraryManagement.dto.request.Category.CategoryUpdateRequest;
import com.example.TTS_LibraryManagement.dto.response.Category.CategoryResponse;
import com.example.TTS_LibraryManagement.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Category toCategoryCreate(CategoryCreationRequest request);

    CategoryResponse toCategoryResponse(Category category);

    void toCategoryUpdate(@MappingTarget Category category, CategoryUpdateRequest request);

    CategoryUpdateRequest toCategoryUpdateRequest(Category category);
}
