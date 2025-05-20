package com.example.TTS_LibraryManagement.service.impl;

import com.example.TTS_LibraryManagement.dto.request.BorrowReturnBook.BRBookCreationRequest;
import com.example.TTS_LibraryManagement.dto.request.BorrowReturnBook.BRBookUpdateRequest;
import com.example.TTS_LibraryManagement.dto.response.BorrowReturnBook.BorrowReturnBookResponse;
import com.example.TTS_LibraryManagement.entity.Book;
import com.example.TTS_LibraryManagement.entity.BorrowReturnBook;
import com.example.TTS_LibraryManagement.entity.User;
import com.example.TTS_LibraryManagement.exception.AppException;
import com.example.TTS_LibraryManagement.exception.ErrorCode;
import com.example.TTS_LibraryManagement.mapper.BorrowReturnBookMapper;
import com.example.TTS_LibraryManagement.repository.BookRepo;
import com.example.TTS_LibraryManagement.repository.BorrowReturnBookRepo;
import com.example.TTS_LibraryManagement.repository.UserRepo;
import com.example.TTS_LibraryManagement.service.BorrowReturnBookService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BorrowReturnBookServiceImpl implements BorrowReturnBookService {
    BorrowReturnBookRepo borrowReturnBookRepo;

    BorrowReturnBookMapper borrowReturnBookMapper;

    UserRepo userRepo;

    BookRepo bookRepo;

    public List<BorrowReturnBookResponse> getBorrowReturnBooksByUserId(Long userId) {
        List<BorrowReturnBook> brs = borrowReturnBookRepo.findByUserId(userId);
        if(brs.isEmpty()){
            throw new AppException(ErrorCode.BORROW_RETURN_BOOK_NOT_FOUND);
        }
        return brs.stream().map(borrowReturnBook -> {
            BorrowReturnBookResponse response = borrowReturnBookMapper.toBorrowReturnBookResponse(borrowReturnBook);
            User user = userRepo.findUserByUserIdAndIsDeletedFalse(userId).orElseThrow();
            Book book = bookRepo.findBookByUserIdAndIsDeletedFalse(borrowReturnBook.getBook().getId()).orElseThrow();
            response.setUser(borrowReturnBookMapper.toBRUserResponse(user));
            response.setBook(borrowReturnBookMapper.toBRBookResponse(book));
            return response;
        }).collect(Collectors.toList());
    }

    public BorrowReturnBookResponse getBorrowReturnBookByUserIdAndBookId(Long userId, Long bookId) {
        BorrowReturnBook borrowReturnBook = borrowReturnBookRepo.findByUserIdAndBookId(userId, bookId)
                .orElseThrow(() -> new AppException(ErrorCode.BORROW_RETURN_BOOK_NOT_FOUND));
        BorrowReturnBookResponse borrowReturnBookResponse = borrowReturnBookMapper.toBorrowReturnBookResponse(borrowReturnBook);
        borrowReturnBookResponse.setUser(borrowReturnBookMapper.toBRUserResponse(userRepo.findUserByUserIdAndIsDeletedFalse(userId).orElseThrow()));
        borrowReturnBookResponse.setBook(borrowReturnBookMapper.toBRBookResponse(bookRepo.findBookByUserIdAndIsDeletedFalse(bookId).orElseThrow()));
        return borrowReturnBookResponse;
    }

    @Transactional
    public BorrowReturnBookResponse createBorrowReturnBook(Long userId, BRBookCreationRequest request) {
        Timestamp now = Timestamp.valueOf(LocalDateTime.now());
        if (request.getReturnAt().before(now)) {
            throw new AppException(ErrorCode.RETURN_DATE_NOT_VALID);
        }
        List<BorrowReturnBook> borrowReturnBooks = borrowReturnBookRepo.findByUserId(userId);
        for(BorrowReturnBook borrowReturnBook : borrowReturnBooks) {
            if (borrowReturnBook.getBook().getId().equals(request.getBookId())) {
                throw new AppException(ErrorCode.BOOK_ALREADY_BORROWED);
            }
        }
        User user = userRepo.findUserByIdAndIsDeletedFalse(request.getUserId()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        Book book = bookRepo.findBookByIdAndIsDeletedFalse(request.getBookId()).orElseThrow(() -> new AppException(ErrorCode.BOOK_NOT_FOUND));
        if (book.getBorrowedQuantity() >= book.getQuantity()) {
            throw new AppException(ErrorCode.BOOK_NOT_AVAILABLE);
        }
        BorrowReturnBook borrowReturnBook = borrowReturnBookMapper.toBorrowReturnBookCreate(request);
        borrowReturnBook.setUser(user);
        borrowReturnBook.setBook(book);
        borrowReturnBook.setBorrowedAt(now);
        borrowReturnBook.setCreatedAt(now);
        book.setBorrowedQuantity(book.getBorrowedQuantity() + 1);
        borrowReturnBookRepo.save(borrowReturnBook);
        BorrowReturnBookResponse response = borrowReturnBookMapper.toBorrowReturnBookResponse(borrowReturnBook);
        response.setUser(borrowReturnBookMapper.toBRUserResponse(user));
        response.setBook(borrowReturnBookMapper.toBRBookResponse(book));
        return response;
    }

    @Transactional
    public BorrowReturnBookResponse updateBorrowReturnBook(Long id, BRBookUpdateRequest request) {
        BorrowReturnBook borrowReturnBook = borrowReturnBookRepo.findById(id).orElseThrow(() -> new AppException(ErrorCode.BORROW_RETURN_BOOK_NOT_FOUND));
        BRBookUpdateRequest currentDTO = borrowReturnBookMapper.toBorrowReturnBookUpdateRequest(borrowReturnBook);
        User user = userRepo.findUserByIdAndIsDeletedFalse(request.getUserId()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        Book book = bookRepo.findBookByIdAndIsDeletedFalse(request.getBookId()).orElseThrow(() -> new AppException(ErrorCode.BOOK_NOT_FOUND));
        if (currentDTO.equals(request) && borrowReturnBook.getUser().equals(user) && borrowReturnBook.getBook().equals(book)) {
            throw new AppException(ErrorCode.BORROW_RETURN_BOOK_NOT_CHANGED);
        }
        borrowReturnBookMapper.toBorrowReturnBookUpdate(borrowReturnBook, request);
        return borrowReturnBookMapper.toBorrowReturnBookResponse(borrowReturnBook);
    }

    public void deleteBorrowReturnBook(Long id) {
        BorrowReturnBook borrowReturnBook = borrowReturnBookRepo.findById(id).orElseThrow(() -> new AppException(ErrorCode.BORROW_RETURN_BOOK_NOT_FOUND));
        borrowReturnBook.setIsDeleted(1);
        borrowReturnBook.setDeletedAt(Timestamp.valueOf(LocalDateTime.now()));
        borrowReturnBookRepo.save(borrowReturnBook);
    }

    public void restoreBorrowReturnBook(Long id) {
        BorrowReturnBook borrowReturnBook = borrowReturnBookRepo.findById(id).orElseThrow(() -> new AppException(ErrorCode.BORROW_RETURN_BOOK_NOT_FOUND));
        if (borrowReturnBook.getIsDeleted() == 0) {
            throw new AppException(ErrorCode.BORROW_RETURN_BOOK_NOT_DELETED);
        }
        borrowReturnBook.setIsDeleted(0);
        borrowReturnBook.setDeletedAt(null);
        borrowReturnBook.setDeletedBy(null);
        borrowReturnBookRepo.save(borrowReturnBook);
    }

}
