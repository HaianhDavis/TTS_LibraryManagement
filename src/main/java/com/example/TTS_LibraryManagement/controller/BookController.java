package com.example.TTS_LibraryManagement.controller;

import com.example.TTS_LibraryManagement.dto.request.Book.BookCreationRequest;
import com.example.TTS_LibraryManagement.dto.request.Book.BookSearchRequest;
import com.example.TTS_LibraryManagement.dto.request.Book.BookUpdateRequest;
import com.example.TTS_LibraryManagement.dto.response.ApiResponse;
import com.example.TTS_LibraryManagement.dto.response.Book.BookResponse;
import com.example.TTS_LibraryManagement.service.BookService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/book")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookController {
    BookService bookService;

    @PostMapping("/create")
    ApiResponse<BookResponse> createBook(@RequestBody BookCreationRequest request) {
        ApiResponse<BookResponse> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Successfully created Book");
        apiResponse.setResult(bookService.createBook(request));
        return apiResponse;
    }

    @GetMapping
    @PreAuthorize("fileRole(#httpServletRequest)")
    ApiResponse<List<BookResponse>> getBooks(HttpServletRequest httpServletRequest) {
        ApiResponse<List<BookResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Successfully retrieved all Books");
        apiResponse.setResult(bookService.getBooks());
        return apiResponse;
    }

    @GetMapping("/detail/{id}")
    ApiResponse<BookResponse> getBookById(@PathVariable Long id) {
        ApiResponse<BookResponse> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Successfully retrieved Book with id " + id);
        apiResponse.setResult(bookService.getBookById(id));
        return apiResponse;
    }

    @PutMapping("/update/{id}")
    ApiResponse<BookResponse> updateBook(@PathVariable Long id, @RequestBody BookUpdateRequest request) {
        ApiResponse<BookResponse> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Successfully updated Book with id " + id);
        apiResponse.setResult(bookService.updateBook(id, request));
        return apiResponse;
    }

    @DeleteMapping("/delete/{id}")
    ApiResponse<BookResponse> deleteBook(@PathVariable Long id) {
        ApiResponse<BookResponse> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Successfully deleted Book with id " + id);
        bookService.deleteBook(id);
        return apiResponse;
    }

    @GetMapping("/search")
    ApiResponse<Page<BookResponse>> searchBooks(@RequestParam(name = "pageNo") int pageNo, @RequestParam(name = "pageSize") int pageSize, @RequestBody BookSearchRequest request) {
        ApiResponse<Page<BookResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Successfully retrieved all Books");
        apiResponse.setResult(bookService.getBooksByPage(pageNo, pageSize, request));
        return apiResponse;
    }

    @PatchMapping("/restore/{id}")
    ApiResponse<BookResponse> restoreBook(@PathVariable Long id) {
        ApiResponse<BookResponse> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Successfully restored Book with id " + id);
        bookService.restoreBook(id);
        return apiResponse;
    }

    @PostMapping("/import-excel")
    ApiResponse<?> importBooksFromExcel(@RequestParam("file") MultipartFile file) {
        return bookService.importBooksFromExcel(file);
    }

    @GetMapping("/export-excel")
    public void exportBooksToExcel(HttpServletResponse response) {
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=books.xls");
        bookService.exportBooksToExcel(response);
    }
}
