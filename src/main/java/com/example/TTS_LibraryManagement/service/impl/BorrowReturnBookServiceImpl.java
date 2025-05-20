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

    @Transactional
    public List<BorrowReturnBookResponse> getBorrowReturnBooksByUserId(Long userId) {
        List<BorrowReturnBook> brs = borrowReturnBookRepo.findByUserId(userId);
        if (brs.isEmpty()) {
            throw new AppException(ErrorCode.BORROW_RETURN_BOOK_NOT_FOUND);
        }
        return brs.stream().map(borrowReturnBook -> {
            BorrowReturnBookResponse response = borrowReturnBookMapper.toBorrowReturnBookResponse(borrowReturnBook);
            User user = userRepo.findUserByBRIdAndIsDeletedFalse(borrowReturnBook.getId()).orElseThrow();
            Book book = bookRepo.findBookByBRIdAndIsDeletedFalse(borrowReturnBook.getId()).orElseThrow();
            response.setUser(borrowReturnBookMapper.toBRUserResponse(user));
            response.setBook(borrowReturnBookMapper.toBRBookResponse(book));
            if(borrowReturnBook.getStatus() == 0) {
                response.setStatus("Borrowed");
            } else {
                response.setStatus("Returned");
            }
            return response;
        }).collect(Collectors.toList());
    }

    @Transactional
    public BorrowReturnBookResponse getBorrowReturnBookById(Long id) {
        BorrowReturnBook borrowReturnBook = borrowReturnBookRepo.findByBRId(id)
                .orElseThrow(() -> new AppException(ErrorCode.BORROW_RETURN_BOOK_NOT_FOUND));
        BorrowReturnBookResponse borrowReturnBookResponse = borrowReturnBookMapper.toBorrowReturnBookResponse(borrowReturnBook);
        borrowReturnBookResponse.setUser(borrowReturnBookMapper.toBRUserResponse(userRepo.findUserByBRIdAndIsDeletedFalse(borrowReturnBook.getId()).orElseThrow()));
        borrowReturnBookResponse.setBook(borrowReturnBookMapper.toBRBookResponse(bookRepo.findBookByBRIdAndIsDeletedFalse(borrowReturnBook.getId()).orElseThrow()));
        if (borrowReturnBook.getStatus() == 0) {
            borrowReturnBookResponse.setStatus("Borrowed");
        } else {
            borrowReturnBookResponse.setStatus("Returned");
        }
        return borrowReturnBookResponse;
    }

    @Transactional
    public BorrowReturnBookResponse createBorrowReturnBook(Long userId, BRBookCreationRequest request) {
        User user = userRepo.findUserByIdAndIsDeletedFalse(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        List<BorrowReturnBook> borrowReturnBooks = borrowReturnBookRepo.findByUserId(userId);
        for (BorrowReturnBook borrowReturnBook : borrowReturnBooks) {
            if (borrowReturnBook.getBook().getId().equals(request.getBookId())) {
                throw new AppException(ErrorCode.BOOK_ALREADY_BORROWED);
            }
        }

        Book book = bookRepo.findBookByIdAndIsDeletedFalse(request.getBookId()).orElseThrow(() -> new AppException(ErrorCode.BOOK_NOT_FOUND));
        if (book.getBorrowedQuantity() >= book.getQuantity()) {
            throw new AppException(ErrorCode.BOOK_NOT_AVAILABLE);
        }

        Timestamp now = Timestamp.valueOf(LocalDateTime.now());
        if (request.getReturnAt().before(now)) {
            throw new AppException(ErrorCode.RETURN_DATE_NOT_VALID);
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
        response.setStatus("Borrowed");
        return response;
    }

    @Transactional
    public BorrowReturnBookResponse updateBorrowReturnBook(Long id, BRBookUpdateRequest request) {
        BorrowReturnBook borrowReturnBook = borrowReturnBookRepo.findByBRId(id).orElseThrow(() -> new AppException(ErrorCode.BORROW_RETURN_BOOK_NOT_FOUND));
        Book oldBook = borrowReturnBook.getBook();
        Book newBook = bookRepo.findBookByIdAndIsDeletedFalse(request.getBookId()).orElseThrow(() -> new AppException(ErrorCode.BOOK_NOT_FOUND));

        if (!oldBook.getId().equals(newBook.getId()) && request.getStatus() != 1) {
            List<BorrowReturnBook> borrowReturnBooks = borrowReturnBookRepo.findByUserId(borrowReturnBook.getUser().getId());
            for (BorrowReturnBook borrowReturnBook1 : borrowReturnBooks) {
                if (!borrowReturnBook1.getId().equals(id) && borrowReturnBook1.getBook().getId().equals(request.getBookId()) && borrowReturnBook1.getStatus() == 0) {
                    throw new AppException(ErrorCode.BOOK_ALREADY_BORROWED);
                }
            }
        }
        if (borrowReturnBook.getBorrowedAt().after(request.getReturnAt())) {
            throw new AppException(ErrorCode.RETURN_DATE_NOT_VALID);
        }
        if (request.getStatus() != 0 && request.getStatus() != 1) {
            throw new AppException(ErrorCode.BORROW_RETURN_BOOK_STATUS_NOT_VALID);
        }
        if (request.getReturnAt().equals(borrowReturnBook.getReturnAt()) && newBook.getId().equals(oldBook.getId()) && request.getStatus() == borrowReturnBook.getStatus()) {
            throw new AppException(ErrorCode.BORROW_RETURN_BOOK_NOT_CHANGED);
        }
        if (!oldBook.getId().equals(newBook.getId())) {
            if (borrowReturnBook.getStatus() == 0) {
                oldBook.setBorrowedQuantity(oldBook.getBorrowedQuantity() - 1);
            }

            if (request.getStatus() == 0) {
                newBook.setBorrowedQuantity(newBook.getBorrowedQuantity() + 1);
            }
        }
        else if (borrowReturnBook.getStatus() == 0 && request.getStatus() == 1) {
            newBook.setBorrowedQuantity(newBook.getBorrowedQuantity() - 1);
        }
        else if (borrowReturnBook.getStatus() == 1 && request.getStatus() == 0) {
            newBook.setBorrowedQuantity(newBook.getBorrowedQuantity() + 1);
        }
        borrowReturnBookMapper.toBorrowReturnBookUpdate(borrowReturnBook, request);
        borrowReturnBook.setBook(newBook);
        borrowReturnBookRepo.save(borrowReturnBook);
        BorrowReturnBookResponse response = borrowReturnBookMapper.toBorrowReturnBookResponse(borrowReturnBook);
        response.setUser(borrowReturnBookMapper.toBRUserResponse(borrowReturnBook.getUser()));
        response.setBook(borrowReturnBookMapper.toBRBookResponse(newBook));
        if (request.getStatus() == 1) {
            response.setStatus("Returned");
        } else {
            response.setStatus("Borrowed");
        }
        return response;
    }

    public void deleteBorrowReturnBook(Long id) {
        BorrowReturnBook borrowReturnBook = borrowReturnBookRepo.findByBRId(id).orElseThrow(() -> new AppException(ErrorCode.BORROW_RETURN_BOOK_NOT_FOUND));
        borrowReturnBook.setIsDeleted(1);
        borrowReturnBook.setDeletedAt(Timestamp.valueOf(LocalDateTime.now()));
        borrowReturnBookRepo.save(borrowReturnBook);
    }

    public void restoreBorrowReturnBook(Long id) {
        BorrowReturnBook borrowReturnBook = borrowReturnBookRepo.findByBRId(id).orElseThrow(() -> new AppException(ErrorCode.BORROW_RETURN_BOOK_NOT_FOUND));
        if (borrowReturnBook.getIsDeleted() == 0) {
            throw new AppException(ErrorCode.BORROW_RETURN_BOOK_NOT_DELETED);
        }
        borrowReturnBook.setIsDeleted(0);
        borrowReturnBook.setDeletedAt(null);
        borrowReturnBook.setDeletedBy(null);
        borrowReturnBookRepo.save(borrowReturnBook);
    }

}
