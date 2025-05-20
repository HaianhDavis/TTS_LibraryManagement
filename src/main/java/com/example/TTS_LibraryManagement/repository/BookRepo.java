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

    @Query(value = "select b.* from users u join borrow_return_book br on u.id = br.user_id join books b on b.id = br.book_id where u.id = ? and u.is_deleted = 0 and b.is_deleted = 0 order by br.status DESC", nativeQuery = true)
    Optional<Book> findBookByUserIdAndIsDeletedFalse(Long id);

    @Query(value = "select count(b)>0 from Book b where b.id = ?1 and b.isDeleted = 0")
    boolean existsById(Long id);


}
