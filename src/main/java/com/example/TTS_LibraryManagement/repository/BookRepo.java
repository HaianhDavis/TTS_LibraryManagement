package com.example.TTS_LibraryManagement.repository;

import com.example.TTS_LibraryManagement.entity.Book;
import com.example.TTS_LibraryManagement.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepo extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {
    @Query(value = "select case when count(b) > 0 then true else false end from Book b where b.code = ?1 and b.isDeleted = 0")
    boolean existsByCode(String functionCode);

    @Query(value = "select b from Book b where b.id = ?1 and b.isDeleted = 0")
    Optional<Book> findBookByIdAndIsDeletedFalse(Long id);

    @Query(value = "select b from Book b where b.isDeleted = 0")
    List<Book> findAllByIsDeletedFalse();

    @Query(value = "select b from User u join BorrowReturnBook br on u.id = br.user.id join Book b on b.id = br.book.id where br.id = ?1 and u.isDeleted = 0 and b.isDeleted = 0 and br.isDeleted = 0 order by br.status DESC")
    Optional<Book> findBookByBRIdAndIsDeletedFalse(Long id);

    @Query(value = "select count(b)>0 from Book b where b.id = ?1 and b.isDeleted = 0")
    boolean existsById(Long id);

    @Query(value = "select b from User u join Post p on u.id = p.user.id join Book b on b.id = p.book.id where p.id = ?1 and u.isDeleted = 0 and b.isDeleted = 0 and p.isDeleted = 0")
    Optional<Book> findBookByPostIdAndIsDeletedFalse(Long id);

}
