package com.example.TTS_LibraryManagement.controller;

import com.example.TTS_LibraryManagement.dto.request.BorrowReturnBook.BRBookCreationRequest;
import com.example.TTS_LibraryManagement.dto.request.BorrowReturnBook.BRBookUpdateRequest;
import com.example.TTS_LibraryManagement.dto.response.ApiResponse;
import com.example.TTS_LibraryManagement.dto.response.BorrowReturnBook.BorrowReturnBookResponse;
import com.example.TTS_LibraryManagement.service.BorrowReturnBookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/borrow")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "BorrowReturnBook", description = "Borrow Return Book Management APIs")
public class BorrowReturnBookController {
    BorrowReturnBookService borrowReturnBookService;

    @Operation(summary = "Create a new Borrow Return Book")
    @PostMapping("/create/{userId}")
    @PreAuthorize("fileRole(#httpServletRequest)")
    ApiResponse<BorrowReturnBookResponse> createBR(HttpServletRequest httpServletRequest, @PathVariable Long userId, @RequestBody BRBookCreationRequest request) {
        ApiResponse<BorrowReturnBookResponse> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Successfully created Borrow Return Book");
        apiResponse.setResult(borrowReturnBookService.createBorrowReturnBook(userId, request));
        return apiResponse;
    }

    @Operation(summary = "Get all Borrow Return Books")
    @GetMapping("/{id}")
    @PreAuthorize("fileRole(#httpServletRequest)")
    ApiResponse<List<BorrowReturnBookResponse>> getBRByUserId(HttpServletRequest httpServletRequest, @PathVariable Long id) {
        ApiResponse<List<BorrowReturnBookResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Successfully retrieved Borrow Return Book");
        apiResponse.setResult(borrowReturnBookService.getBorrowReturnBooksByUserId(id));
        return apiResponse;
    }

    @Operation(summary = "Get all Borrow Return Books")
    @GetMapping("/detail/{id}")
    @PreAuthorize("fileRole(#httpServletRequest)")
    ApiResponse<BorrowReturnBookResponse> getBRById(HttpServletRequest httpServletRequest, @PathVariable Long id) {
        ApiResponse<BorrowReturnBookResponse> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Successfully retrieved Borrow Return Book");
        apiResponse.setResult(borrowReturnBookService.getBorrowReturnBookById(id));
        return apiResponse;
    }

    @Operation(summary = "Get all Borrow Return Books")
    @PutMapping("/update/{id}")
    @PreAuthorize("fileRole(#httpServletRequest)")
    ApiResponse<BorrowReturnBookResponse> updateBR(HttpServletRequest httpServletRequest, @PathVariable Long id, @RequestBody BRBookUpdateRequest request) {
        ApiResponse<BorrowReturnBookResponse> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Successfully updated Borrow Return Book");
        apiResponse.setResult(borrowReturnBookService.updateBorrowReturnBook(id, request));
        return apiResponse;
    }

    @Operation(summary = "Delete a Borrow Return Book")
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("fileRole(#httpServletRequest)")
    ApiResponse<BorrowReturnBookResponse> deleteBR(HttpServletRequest httpServletRequest, @PathVariable Long id) {
        ApiResponse<BorrowReturnBookResponse> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Successfully deleted Borrow Return Book");
        borrowReturnBookService.deleteBorrowReturnBook(id);
        return apiResponse;
    }

    @Operation(summary = "Restore a Borrow Return Book")
    @PatchMapping("/restore/{id}")
    @PreAuthorize("fileRole(#httpServletRequest)")
    ApiResponse<BorrowReturnBookResponse> restoreBR(HttpServletRequest httpServletRequest, @PathVariable Long id) {
        ApiResponse<BorrowReturnBookResponse> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Successfully restored Borrow Return Book");
        borrowReturnBookService.restoreBorrowReturnBook(id);
        return apiResponse;
    }
}
