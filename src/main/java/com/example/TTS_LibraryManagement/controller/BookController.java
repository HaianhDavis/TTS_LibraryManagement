package com.example.TTS_LibraryManagement.controller;

import com.example.TTS_LibraryManagement.dto.request.Book.BookCreationRequest;
import com.example.TTS_LibraryManagement.dto.request.Book.BookSearchRequest;
import com.example.TTS_LibraryManagement.dto.request.Book.BookUpdateRequest;
import com.example.TTS_LibraryManagement.dto.response.ApiResponse;
import com.example.TTS_LibraryManagement.dto.response.Book.BookResponse;
import com.example.TTS_LibraryManagement.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Book", description = "Book Management APIs")
public class BookController {
    BookService bookService;

    @Operation(summary = "Create a new book")
    @PostMapping("/create")
    @PreAuthorize("fileRole(#httpServletRequest)")
    ApiResponse<BookResponse> createBook(HttpServletRequest httpServletRequest, @RequestBody BookCreationRequest request) {
        ApiResponse<BookResponse> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Successfully created Book");
        apiResponse.setResult(bookService.createBook(request));
        return apiResponse;
    }

    @Operation(summary = "Get all books")
    @GetMapping
    @PreAuthorize("fileRole(#httpServletRequest)")
    ApiResponse<List<BookResponse>> getBooks(HttpServletRequest httpServletRequest) {
        ApiResponse<List<BookResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Successfully retrieved all Books");
        apiResponse.setResult(bookService.getBooks());
        return apiResponse;
    }

    @Operation(summary = "Get book by ID")
    @GetMapping("/detail/{id}")
    @PreAuthorize("fileRole(#httpServletRequest)")
    ApiResponse<BookResponse> getBookById(HttpServletRequest httpServletRequest, @PathVariable Long id) {
        ApiResponse<BookResponse> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Successfully retrieved Book with id " + id);
        apiResponse.setResult(bookService.getBookById(id));
        return apiResponse;
    }

    @Operation(summary = "Update a book")
    @PutMapping("/update/{id}")
    @PreAuthorize("fileRole(#httpServletRequest)")
    ApiResponse<BookResponse> updateBook(HttpServletRequest httpServletRequest, @PathVariable Long id, @RequestBody BookUpdateRequest request) {
        ApiResponse<BookResponse> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Successfully updated Book with id " + id);
        apiResponse.setResult(bookService.updateBook(id, request));
        return apiResponse;
    }

    @Operation(summary = "Delete a book")
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("fileRole(#httpServletRequest)")
    ApiResponse<BookResponse> deleteBook(HttpServletRequest httpServletRequest, @PathVariable Long id) {
        ApiResponse<BookResponse> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Successfully deleted Book with id " + id);
        bookService.deleteBook(id);
        return apiResponse;
    }

    @Operation(summary = "Search books with pagination")
    @GetMapping("/search")
    @PreAuthorize("fileRole(#httpServletRequest)")
    ApiResponse<Page<BookResponse>> searchBooks(HttpServletRequest httpServletRequest, @RequestParam(name = "pageNo") int pageNo, @RequestParam(name = "pageSize") int pageSize, @RequestBody BookSearchRequest request) {
        ApiResponse<Page<BookResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Successfully retrieved all Books");
        apiResponse.setResult(bookService.getBooksByPage(pageNo, pageSize, request));
        return apiResponse;
    }

    @Operation(summary = "Restore a deleted book")
    @PatchMapping("/restore/{id}")
    @PreAuthorize("fileRole(#httpServletRequest)")
    ApiResponse<BookResponse> restoreBook(HttpServletRequest httpServletRequest, @PathVariable Long id) {
        ApiResponse<BookResponse> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Successfully restored Book with id " + id);
        bookService.restoreBook(id);
        return apiResponse;
    }

    @Operation(summary = "Import books from Excel file")
    @PostMapping("/import-excel")
    @PreAuthorize("fileRole(#httpServletRequest)")
    ApiResponse<?> importBooksFromExcel(HttpServletRequest httpServletRequest, @RequestParam("file") MultipartFile file) {
        return bookService.importBooksFromExcel(file);
    }

    @Operation(summary = "Export books to Excel file")
    @GetMapping("/export-excel")
    @PreAuthorize("fileRole(#httpServletRequest)")
    public void exportBooksToExcel(HttpServletRequest httpServletRequest, HttpServletResponse response) {
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=books.xls");
        bookService.exportBooksToExcel(response);
    }
}
