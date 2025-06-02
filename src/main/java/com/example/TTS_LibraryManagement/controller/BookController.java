package com.example.TTS_LibraryManagement.controller;

import com.example.TTS_LibraryManagement.dto.request.Book.BookCreationRequest;
import com.example.TTS_LibraryManagement.dto.request.Book.BookSearchRequest;
import com.example.TTS_LibraryManagement.dto.request.Book.BookUpdateRequest;
import com.example.TTS_LibraryManagement.dto.response.ApiResponse;
import com.example.TTS_LibraryManagement.dto.response.Book.BookResponse;
import com.example.TTS_LibraryManagement.dto.response.Book.ImportBooksResult;
import com.example.TTS_LibraryManagement.exception.AppException;
import com.example.TTS_LibraryManagement.service.BookService;
import com.example.TTS_LibraryManagement.utils.ApiUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
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
    ApiResponse<BookResponse> createBook(HttpServletRequest httpServletRequest, @RequestBody @Valid BookCreationRequest request) {
        return ApiUtils.success(bookService.createBook(request));
    }

    @Operation(summary = "Get all books")
    @GetMapping
    @PreAuthorize("fileRole(#httpServletRequest)")
    ApiResponse<List<BookResponse>> getBooks(HttpServletRequest httpServletRequest) {
        return ApiUtils.success(bookService.getBooks());
    }

    @Operation(summary = "Get book by ID")
    @GetMapping("/detail/{id}")
    @PreAuthorize("fileRole(#httpServletRequest)")
    ApiResponse<BookResponse> getBookById(HttpServletRequest httpServletRequest, @PathVariable Long id) {
        return ApiUtils.success(bookService.getBookById(id));
    }

    @Operation(summary = "Update a book")
    @PutMapping("/update/{id}")
    @PreAuthorize("fileRole(#httpServletRequest)")
    ApiResponse<BookResponse> updateBook(HttpServletRequest httpServletRequest, @PathVariable Long id, @RequestBody @Valid BookUpdateRequest request) {
        return ApiUtils.success(bookService.updateBook(id, request));
    }

    @Operation(summary = "Delete a book")
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("fileRole(#httpServletRequest)")
    ApiResponse<BookResponse> deleteBook(HttpServletRequest httpServletRequest, @PathVariable Long id) {
        bookService.deleteBook(id);
        return ApiUtils.successDeleteOrRestore("Successfully deleted Book with id " + id);
    }

    @Operation(summary = "Search books with pagination")
    @GetMapping("/search")
    @PreAuthorize("fileRole(#httpServletRequest)")
    ApiResponse<Page<BookResponse>> searchBooks(HttpServletRequest httpServletRequest, @RequestParam(name = "pageNo") int pageNo, @RequestParam(name = "pageSize") int pageSize, @RequestBody BookSearchRequest request) {
        return ApiUtils.success(bookService.getBooksByPage(pageNo, pageSize, request));
    }

    @Operation(summary = "Restore a deleted book")
    @PatchMapping("/restore/{id}")
    @PreAuthorize("fileRole(#httpServletRequest)")
    ApiResponse<BookResponse> restoreBook(HttpServletRequest httpServletRequest, @PathVariable Long id) {
        bookService.restoreBook(id);
        return ApiUtils.successDeleteOrRestore("Successfully restored Book with id " + id);
    }

    @Operation(summary = "Import books from Excel file")
    @PostMapping("/import-excel")
    @PreAuthorize("fileRole(#httpServletRequest)")
    ApiResponse<ImportBooksResult> importBooksFromExcel(HttpServletRequest httpServletRequest, @RequestParam("file") MultipartFile file) {
        return ApiUtils.success(bookService.importBooksFromExcel(file));
    }

    @Operation(summary = "Export books to Excel file")
    @GetMapping("/export-excel")
    @PreAuthorize("fileRole(#httpServletRequest)")
    ApiResponse<BookResponse> exportBooksToExcel(HttpServletRequest httpServletRequest, HttpServletResponse response) {
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=books.xls");
        bookService.exportBooksToExcel(response);
        return ApiUtils.successDeleteOrRestore("Books exported successfully to Excel file.");
    }
}
