package com.example.TTS_LibraryManagement.service;

import com.example.TTS_LibraryManagement.dto.request.BorrowReturnBook.BRBookCreationRequest;
import com.example.TTS_LibraryManagement.dto.request.BorrowReturnBook.BRBookUpdateRequest;
import com.example.TTS_LibraryManagement.dto.response.BorrowReturnBook.BorrowReturnBookResponse;

import java.util.List;

public interface BorrowReturnBookService {
    List<BorrowReturnBookResponse> getBorrowReturnBooksByUserId(Long userId);
    BorrowReturnBookResponse getBorrowReturnBookByUserIdAndBookId(Long userId, Long bookId);
    BorrowReturnBookResponse createBorrowReturnBook(Long userId, BRBookCreationRequest request);
    BorrowReturnBookResponse updateBorrowReturnBook(Long id, BRBookUpdateRequest request);
    void deleteBorrowReturnBook(Long id);
    void restoreBorrowReturnBook(Long id);
}
