package com.example.TTS_LibraryManagement.repository;

import com.example.TTS_LibraryManagement.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepo extends JpaRepository<User,Long> , JpaSpecificationExecutor<User> {
    @Query(value = "select case when count(u) > 0 then true else false end from User u where u.username = ?1 and u.isDeleted = 0")
    boolean existsByUsername(String username);

    @Query(value = "select case when count(u) > 0 then true else false end from User u where u.identityNumber = ?1 and u.isDeleted = 0")
    boolean existsByIdentityNumber(String identityNumber);

    @Query(value = "SELECT u.* FROM users u JOIN user_roles ur ON u.id = ur.user_id WHERE ur.role_id = ? AND u.is_deleted = 0", nativeQuery = true)
    List<User> findUsersByRoleIdAndIsDeletedFalse(Long roleId);

    @Query(value = "select u from User u where u.id = ?1 and u.isDeleted = 0")
    Optional<User> findUserByIdAndIsDeletedFalse(Long id);

    @Query(value = "select u from User u where u.isDeleted = 0")
    List<User> findUsersByIsDeletedFalse();

    @Query("SELECT u FROM User u WHERE u.id IN :ids AND u.isDeleted = 0")
    List<User> findAllByIdInAndIsDeletedFalse(Set<Long> ids);

    @Query(value = "select u from User u join BorrowReturnBook br on u.id = br.user.id join Book b on b.id = br.book.id where br.id = ?1 and u.isDeleted = 0 and b.isDeleted = 0 and br.isDeleted = 0 order by br.status DESC")
    Optional<User> findUserByBRIdAndIsDeletedFalse(Long id);

    @Query(value = "select count(u)>0 from User u where u.id = ?1 and u.isDeleted = 0")
    boolean existsById(Long id);

    @Query(value = "select u from User u join Post p on u.id = p.user.id join Book b on b.id = p.book.id where p.id = ?1 and u.isDeleted = 0 and b.isDeleted = 0 and p.isDeleted = 0")
    Optional<User> findUserByPostIdAndIsDeletedFalse(Long id);

    @Query(value = "select u from User u join Comment c on u.id = c.user.id join Post p on p.id = c.post.id where c.id = ?1 and u.isDeleted = 0 and c.isDeleted = 0 and p.isDeleted = 0")
    Optional<User> findUserByCommentIdAndIsDeletedFalse(Long id);

    Optional<User> findByUsername(String username);
}
