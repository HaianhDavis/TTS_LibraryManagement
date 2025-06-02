package com.example.TTS_LibraryManagement.service;

import com.example.TTS_LibraryManagement.dto.request.Book.BookCreationRequest;
import com.example.TTS_LibraryManagement.dto.request.Book.BookSearchRequest;
import com.example.TTS_LibraryManagement.dto.request.Book.BookUpdateRequest;
import com.example.TTS_LibraryManagement.dto.request.Book.ErrorRecordBook;
import com.example.TTS_LibraryManagement.dto.response.ApiResponse;
import com.example.TTS_LibraryManagement.dto.response.Book.BookResponse;
import com.example.TTS_LibraryManagement.dto.response.Book.ImportBooksResult;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface BookService {
    BookResponse createBook(BookCreationRequest request);
    List<BookResponse> getBooks();
    BookResponse updateBook(Long id, BookUpdateRequest request);
    void deleteBook(Long bookId);
    void restoreBook(Long bookId);
    BookResponse getBookById(Long id);
    Page<BookResponse> getBooksByPage(int page, int size, BookSearchRequest request);
    ImportBooksResult importBooksFromExcel(MultipartFile file);
    void exportBooksToExcel(HttpServletResponse response);
}
