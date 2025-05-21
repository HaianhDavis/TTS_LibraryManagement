package com.example.TTS_LibraryManagement.repository;

import com.example.TTS_LibraryManagement.dto.response.Dashboard.DashboardBookResponse;
import com.example.TTS_LibraryManagement.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Repository
public interface CategoryRepo extends JpaRepository<Category, Long> {
    @Query(value = "select case when count(c) > 0 then true else false end from Category c where c.categoryCode = ?1 and c.isDeleted = 0")
    boolean existsByCode(String functionCode);

    @Query("SELECT c FROM Category c WHERE c.id IN :ids AND c.isDeleted = 0")
    List<Category> findAllByIdInAndIsDeletedFalse(Set<Long> ids);

    @Query(value = "SELECT c.* FROM categories c JOIN book_categories bc ON c.id = bc.category_id WHERE bc.book_id = ? AND c.is_deleted = 0", nativeQuery = true)
    List<Category> findCategoriesByBookIdAndIsDeletedFalse(Long bookId);

    @Query(value = "select c from Category c where c.id = ?1 and c.isDeleted = 0")
    Optional<Category> findCategoryByIdAndIsDeletedFalse(Long id);

    @Query(value = "select c from Category c where c.isDeleted = 0")
    List<Category> findAllByIsDeletedFalse();

    @Query(value = "SELECT c FROM Category c where c.isDeleted = 0 order by c.categoryCode")
    Page<Category> searchByIsDeletedFalseAndPageable(Pageable pageable);

    @Query(value = "SELECT c.id FROM Category c where c.isDeleted = 0")
    List<Long> findAllIdByIsDeletedFalse();

    @Query(value = "SELECT c.id AS categoryId, c.category_name as categoryName, COUNT(b.id) AS totalBooks " +
            "FROM categories c " +
            "LEFT JOIN book_categories bc ON c.id = bc.category_id " +
            "LEFT JOIN books b ON bc.book_id = b.id " +
            "WHERE c.is_deleted = 0 AND b.is_deleted = 0 " +
            "GROUP BY c.id, c.category_name", nativeQuery = true)
    List<Object[]> getBookStatsByCategory();
}
