package com.example.TTS_LibraryManagement.controller;

import com.example.TTS_LibraryManagement.dto.request.BorrowReturnBook.BRBookCreationRequest;
import com.example.TTS_LibraryManagement.dto.request.BorrowReturnBook.BRBookUpdateRequest;
import com.example.TTS_LibraryManagement.dto.response.ApiResponse;
import com.example.TTS_LibraryManagement.dto.response.BorrowReturnBook.BorrowReturnBookResponse;
import com.example.TTS_LibraryManagement.service.BorrowReturnBookService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/borrow")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BorrowReturnBookController {
    BorrowReturnBookService borrowReturnBookService;

    @PostMapping("/create/{userId}")
    ApiResponse<BorrowReturnBookResponse> createBR(@PathVariable Long userId, @RequestBody BRBookCreationRequest request) {
        ApiResponse<BorrowReturnBookResponse> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Successfully created Borrow Return Book");
        apiResponse.setResult(borrowReturnBookService.createBorrowReturnBook(userId,request));
        return apiResponse;
    }

    @GetMapping("/{id}")
    ApiResponse<List<BorrowReturnBookResponse>> getBRByUserId(@PathVariable Long id) {
        ApiResponse<List<BorrowReturnBookResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Successfully retrieved Borrow Return Book");
        apiResponse.setResult(borrowReturnBookService.getBorrowReturnBooksByUserId(id));
        return apiResponse;
    }

    @GetMapping("/detail/{id}")
    ApiResponse<BorrowReturnBookResponse> getBRById(@PathVariable Long id) {
        ApiResponse<BorrowReturnBookResponse> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Successfully retrieved Borrow Return Book");
        apiResponse.setResult(borrowReturnBookService.getBorrowReturnBookById(id));
        return apiResponse;
    }

    @PutMapping("/update/{id}")
    ApiResponse<BorrowReturnBookResponse> updateBR(@PathVariable Long id, @RequestBody BRBookUpdateRequest request) {
        ApiResponse<BorrowReturnBookResponse> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Successfully updated Borrow Return Book");
        apiResponse.setResult(borrowReturnBookService.updateBorrowReturnBook(id, request));
        return apiResponse;
    }

    @DeleteMapping("/delete/{id}")
    ApiResponse<BorrowReturnBookResponse> deleteBR(@PathVariable Long id) {
        ApiResponse<BorrowReturnBookResponse> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Successfully deleted Borrow Return Book");
        borrowReturnBookService.deleteBorrowReturnBook(id);
        return apiResponse;
    }

    @PatchMapping("/restore/{id}")
    ApiResponse<BorrowReturnBookResponse> restoreBR(@PathVariable Long id) {
        ApiResponse<BorrowReturnBookResponse> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Successfully restored Borrow Return Book");
        borrowReturnBookService.restoreBorrowReturnBook(id);
        return apiResponse;
    }
}
