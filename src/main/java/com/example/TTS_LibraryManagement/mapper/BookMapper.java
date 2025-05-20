package com.example.TTS_LibraryManagement.mapper;

import com.example.TTS_LibraryManagement.dto.request.Book.BookCreationRequest;
import com.example.TTS_LibraryManagement.dto.request.Book.BookUpdateRequest;
import com.example.TTS_LibraryManagement.dto.request.Permission.PermissionCreationRequest;
import com.example.TTS_LibraryManagement.dto.request.Permission.PermissionUpdateRequest;
import com.example.TTS_LibraryManagement.dto.response.Book.BookCategoryResponse;
import com.example.TTS_LibraryManagement.dto.response.Book.BookResponse;
import com.example.TTS_LibraryManagement.dto.response.Permission.PermissionResponse;
import com.example.TTS_LibraryManagement.entity.Book;
import com.example.TTS_LibraryManagement.entity.Category;
import com.example.TTS_LibraryManagement.entity.Permission;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface BookMapper {
    Book toBookCreate(BookCreationRequest request);

    BookResponse toBookResponse(Book book);

    void toBookUpdate(@MappingTarget Book book, BookUpdateRequest request);

    BookUpdateRequest toBookUpdateRequest(Book book);

    BookCategoryResponse toBookCategoryResponse(Category category);
}
