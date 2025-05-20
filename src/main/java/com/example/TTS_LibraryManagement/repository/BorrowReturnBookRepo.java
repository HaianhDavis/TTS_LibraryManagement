package com.example.TTS_LibraryManagement.repository;

import com.example.TTS_LibraryManagement.entity.BorrowReturnBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BorrowReturnBookRepo extends JpaRepository<BorrowReturnBook, Long> {
    @Query(value = "select br from User u join BorrowReturnBook br on u.id = br.user.id join Book b on b.id = br.book.id where br.id = ?1 and b.isDeleted = 0 and u.isDeleted = 0 and br.isDeleted = 0 order by br.status DESC")
    Optional<BorrowReturnBook> findByBRId(Long id);

    @Query(value = "select br from User u join BorrowReturnBook br on u.id = br.user.id join Book b on b.id = br.book.id where br.user.id = ?1 and u.isDeleted = 0 and b.isDeleted = 0 and br.isDeleted = 0 and br.status = 0 order by br.status desc")
    List<BorrowReturnBook> findByUserId(Long userId);
}
