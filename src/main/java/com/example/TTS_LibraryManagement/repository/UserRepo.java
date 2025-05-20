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

    @Query(value = "select u.* from users u join borrow_return_book br on u.id = br.user_id join books b on b.id = br.book_id where u.id = ? and u.is_deleted = 0 and b.is_deleted = 0 order by br.status DESC", nativeQuery = true)
    Optional<User> findUserByUserIdAndIsDeletedFalse(Long id);

    @Query(value = "select count(u)>0 from User u where u.id = ?1 and u.isDeleted = 0")
    boolean existsById(Long id);
}
