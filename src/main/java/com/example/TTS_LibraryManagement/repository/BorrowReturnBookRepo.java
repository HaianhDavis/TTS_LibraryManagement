package com.example.TTS_LibraryManagement.repository;

import com.example.TTS_LibraryManagement.entity.BorrowReturnBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BorrowReturnBookRepo extends JpaRepository<BorrowReturnBook, Long> {
    @Query(value = "select br.* from users u join borrow_return_book br on u.id = br.user_id join books b on b.id = br.book_id where br.user_id = ?1 and br.book_id = ?2 and b.is_deleted = 0 and u.is_deleted = 0 order by br.status DESC", nativeQuery = true)
    Optional<BorrowReturnBook> findByUserIdAndBookId(Long userId, Long bookId);

    @Query(value = "select br from BorrowReturnBook br where br.id = ?1 and br.isDeleted = 0")
    Optional<BorrowReturnBook> findById(Long id);

    @Query(value = "select br from User u join BorrowReturnBook br on u.id = br.user.id where br.user.id = ?1 and u.isDeleted = 0 and br.isDeleted = 0 order by br.status desc")
    List<BorrowReturnBook> findByUserId(Long userId);
}
