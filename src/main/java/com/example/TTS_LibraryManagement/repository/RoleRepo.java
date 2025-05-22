package com.example.TTS_LibraryManagement.repository;

import com.example.TTS_LibraryManagement.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepo extends JpaRepository<Role,Long> {
    @Query(value = "select case when count(r) > 0 then true else false end from Role r where r.roleGroupCode = ?1 and r.isDeleted = 0")
    boolean existsByRoleGroupCode(String roleGroupCode);
    @Query(value = "select case when count(r) > 0 then true else false end from Role r where r.roleGroupName = ?1 and r.isDeleted = 0")
    boolean existsByRoleGroupName(String roleGroupName);

    @Query(value = "select r from Role r where r.isDeleted = 0")
    List<Role> findAllByIsDeletedFalse();

    @Query(value = "SELECT r FROM Role r where r.isDeleted = 0 order by r.roleGroupName")
    Page<Role> searchByIsDeletedFalseAndPageable(Pageable pageable);

    @Query(value = "select r from Role r where r.id = ?1 and r.isDeleted = 0")
    Optional<Role> findRoleByIdAndIsDeletedFalse(Long id);

    @Query(value = "select r from Role r where r.roleGroupCode = ?1 and r.isDeleted = 0")
    Optional<Role> findByRoleGroupCodeAndIsDeletedFalse(String roleGroupCode);

    @Query(value = "select r.role_group_code from roles r" +
            " left join user_roles ur on ur.role_id = r.id" +
            " left join users u on ur.user_id = u.id " +
            " where u.id = ?1 and r.is_deleted = 0 and u.is_deleted = 0", nativeQuery = true)
    List<String> getAllRoleOfUser(Long userId);
}
