package com.example.TTS_LibraryManagement.service.impl;

import com.example.TTS_LibraryManagement.dto.request.Permission.PermissionCreationRequest;
import com.example.TTS_LibraryManagement.dto.request.Permission.PermissionSearchByUserRequest;
import com.example.TTS_LibraryManagement.dto.request.Permission.PermissionUpdateRequest;
import com.example.TTS_LibraryManagement.dto.response.Permission.PermissionResponse;
import com.example.TTS_LibraryManagement.entity.Permission;
import com.example.TTS_LibraryManagement.exception.AppException;
import com.example.TTS_LibraryManagement.enums.ErrorCode;
import com.example.TTS_LibraryManagement.mapper.PermissionMapper;
import com.example.TTS_LibraryManagement.repository.PermissionRepo;
import com.example.TTS_LibraryManagement.service.PermissionService;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionServiceImpl implements PermissionService {
    PermissionRepo permissionRepo;
    PermissionMapper permissionMapper;

    @Transactional
    public PermissionResponse createPermission(PermissionCreationRequest request){
        if(permissionRepo.existsByFunctionCode(request.getFunctionCode())){
            throw new AppException(ErrorCode.PERMISSION_EXISTED);
        }
        Permission permission = permissionMapper.toPermissionCreate(request);
        permission.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        permission.setCreatedBy(SecurityContextHolder.getContext().getAuthentication().getName());
        return permissionMapper.toPermissionResponse(permissionRepo.save(permission));
    }

    @Transactional
    public PermissionResponse updatePermission(Long id, PermissionUpdateRequest request){
        Permission permission = permissionRepo
                .findPermissionByIdAndIsDeletedFalse(id).orElseThrow(() -> new AppException(ErrorCode.PERMISSION_NOT_FOUND));
        PermissionUpdateRequest currentRequest = permissionMapper.toPermissionUpdateRequest(permission);
        if(currentRequest.equals(request)){
            throw new AppException(ErrorCode.PERMISSION_NOT_CHANGED);
        }
        permissionMapper.toPermissionUpdate(permission,request);
        permission.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
        permission.setUpdatedBy(SecurityContextHolder.getContext().getAuthentication().getName());
        return permissionMapper.toPermissionResponse(permissionRepo.save(permission));
    }

    @Transactional
    public void deletePermission(Long permissionId) {
        Permission permission = permissionRepo
                .findPermissionByIdAndIsDeletedFalse(permissionId)
                .orElseThrow(() -> new AppException(ErrorCode.PERMISSION_NOT_FOUND));
        permission.setIsDeleted(1);
        permission.setDeletedAt(Timestamp.valueOf(LocalDateTime.now()));
        permission.setDeletedBy(SecurityContextHolder.getContext().getAuthentication().getName());
        permissionRepo.save(permission);
    }

    @Transactional
    public void restorePermission(Long permissionId) {
        Permission permission = permissionRepo
                .findById(permissionId)
                .orElseThrow(() -> new AppException(ErrorCode.PERMISSION_NOT_FOUND));
        if (permission.getIsDeleted() == 0) {
            throw new AppException(ErrorCode.PERMISSION_NOT_DELETED);
        }
        permission.setIsDeleted(0);
        permission.setDeletedAt(null);
        permission.setDeletedBy(null);
        permissionRepo.save(permission);
    }

    @Transactional
    public List<PermissionResponse> getPermissions() {
        List<Permission> permissions = permissionRepo.findAllByIsDeletedFalse();
        if (permissions.isEmpty()) {
            throw new AppException(ErrorCode.PERMISSION_NOT_FOUND);
        }
        return permissions.stream().map(permissionMapper::toPermissionResponse).collect(Collectors.toList());
    }

    @Transactional
    public PermissionResponse getPermissionById(Long id) {
        Permission permission = permissionRepo
                .findPermissionByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new AppException(ErrorCode.PERMISSION_NOT_FOUND));
        return permissionMapper.toPermissionResponse(permission);
    }

    @Transactional
    public Page<PermissionResponse> getPermissionsByPage(int page, int size, PermissionSearchByUserRequest request) {
        if (page < 1) {
            throw new AppException(ErrorCode.PAGE_NO_ERROR);
        }
        if (size < 1) {
            throw new AppException(ErrorCode.PAGE_SIZE_ERROR);
        }
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "functionCode"));
        Specification<Permission> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("isDeleted"), 0));
            if (request != null) {
                if (request.getFullname() != null && !request.getFullname().isEmpty()) {
                    predicates.add(cb.like(
                            cb.lower(root.join("roles").join("users").get("fullname")),
                            "%" + request.getFullname().toLowerCase() + "%"
                    ));
                    predicates.add(cb.equal(root.join("roles").join("users").get("isDeleted"), 0));
                }
            }
            predicates.add(cb.equal(root.join("roles").get("isDeleted"), 0));
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        Page<Permission> permissions = permissionRepo.findAll(spec,pageable);
        return permissions.map(permissionMapper::toPermissionResponse);
    }
}
