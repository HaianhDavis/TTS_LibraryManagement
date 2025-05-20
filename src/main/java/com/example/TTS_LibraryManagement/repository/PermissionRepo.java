package com.example.TTS_LibraryManagement.repository;

import com.example.TTS_LibraryManagement.entity.Permission;
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
public interface PermissionRepo extends JpaRepository<Permission,Long> , JpaSpecificationExecutor<Permission> {
    @Query(value = "select case when count(p) > 0 then true else false end from Permission p where p.functionCode = ?1 and p.isDeleted = 0")
    boolean existsByFunctionCode(String functionCode);

    @Query(value = "SELECT p.* FROM permissions p " +
            "JOIN role_permissions rp ON p.id = rp.permission_id " +
            "WHERE rp.role_id = ?1 AND p.is_deleted = 0", nativeQuery = true)
    List<Permission> findPermissionsByRoleIdAndIsDeletedFalse(Long roleId);

    @Query(value = "select p from Permission p where p.isDeleted = 0")
    List<Permission> findAllByIsDeletedFalse();

    @Query(value = "select p from Permission p where p.id = ?1 and p.isDeleted = 0")
    Optional<Permission> findPermissionByIdAndIsDeletedFalse(Long id);

    @Query("SELECT p FROM Permission p WHERE p.id IN :ids AND p.isDeleted = 0")
    List<Permission> findAllByIdInAndIsDeletedFalse(Set<Long> ids);
}
