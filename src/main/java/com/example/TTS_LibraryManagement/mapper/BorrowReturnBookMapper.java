package com.example.TTS_LibraryManagement.mapper;

import com.example.TTS_LibraryManagement.dto.request.Book.BookCreationRequest;
import com.example.TTS_LibraryManagement.dto.request.Book.BookUpdateRequest;
import com.example.TTS_LibraryManagement.dto.request.BorrowReturnBook.BRBookCreationRequest;
import com.example.TTS_LibraryManagement.dto.request.BorrowReturnBook.BRBookUpdateRequest;
import com.example.TTS_LibraryManagement.dto.response.Book.BookCategoryResponse;
import com.example.TTS_LibraryManagement.dto.response.Book.BookResponse;
import com.example.TTS_LibraryManagement.dto.response.BorrowReturnBook.BRBookResponse;
import com.example.TTS_LibraryManagement.dto.response.BorrowReturnBook.BRUserResponse;
import com.example.TTS_LibraryManagement.dto.response.BorrowReturnBook.BorrowReturnBookResponse;
import com.example.TTS_LibraryManagement.entity.Book;
import com.example.TTS_LibraryManagement.entity.BorrowReturnBook;
import com.example.TTS_LibraryManagement.entity.Category;
import com.example.TTS_LibraryManagement.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface BorrowReturnBookMapper {
    BorrowReturnBook toBorrowReturnBookCreate(BRBookCreationRequest request);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "book", ignore = true)
    BorrowReturnBookResponse toBorrowReturnBookResponse(BorrowReturnBook borrowReturnBook);

    @Mapping(target = "status", source = "status") // Thêm dòng này
    void toBorrowReturnBookUpdate(@MappingTarget BorrowReturnBook borrowReturnBook, BRBookUpdateRequest request);

    BRUserResponse toBRUserResponse(User user);

    BRBookResponse toBRBookResponse(Book book);
}
