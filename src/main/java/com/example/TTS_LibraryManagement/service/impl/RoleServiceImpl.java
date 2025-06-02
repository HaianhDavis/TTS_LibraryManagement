package com.example.TTS_LibraryManagement.service.impl;

import com.example.TTS_LibraryManagement.dto.request.Role.RoleCreationRequest;
import com.example.TTS_LibraryManagement.dto.request.Role.RoleUpdateRequest;
import com.example.TTS_LibraryManagement.dto.response.Role.RolePermissionResponse;
import com.example.TTS_LibraryManagement.dto.response.Role.RoleResponse;
import com.example.TTS_LibraryManagement.dto.response.Role.RoleUserResponse;
import com.example.TTS_LibraryManagement.entity.*;
import com.example.TTS_LibraryManagement.exception.AppException;
import com.example.TTS_LibraryManagement.enums.ErrorCode;
import com.example.TTS_LibraryManagement.mapper.RoleMapper;
import com.example.TTS_LibraryManagement.repository.*;
import com.example.TTS_LibraryManagement.service.RoleService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleServiceImpl implements RoleService {
    RoleRepo roleRepo;

    RoleMapper roleMapper;

    UserRepo userRepo;

    PermissionRepo permissionRepo;

    @Transactional
    public RoleResponse createRole(RoleCreationRequest request) {
        if (roleRepo.existsByRoleGroupCode(request.getRoleGroupCode())
                || roleRepo.existsByRoleGroupName(request.getRoleGroupName())) {
            throw new AppException(ErrorCode.ROLE_EXISTED);
        }
        Role role = roleMapper.toRoleCreate(request);

        if (request.getUserIds() != null && !request.getUserIds().isEmpty()) {
            List<User> users = userRepo.findAllByIdInAndIsDeletedFalse(request.getUserIds());
            if (users.size() != request.getUserIds().size()) {
                throw new AppException(ErrorCode.USER_NOT_FOUND);
            }
            role.setUsers(new HashSet<>(users));
        }
        if (request.getPermissionIds() != null && !request.getPermissionIds().isEmpty()) {
            List<Permission> permissions = permissionRepo.findAllByIdInAndIsDeletedFalse(request.getPermissionIds());
            if (permissions.size() != request.getPermissionIds().size()) {
                throw new AppException(ErrorCode.PERMISSION_NOT_FOUND);
            }
            role.setPermissions(new HashSet<>(permissions));
        }
        role.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        roleRepo.save(role);
        RoleResponse roleResponse = roleMapper.toRoleResponse(role);
        roleResponse.setUsers(role.getUsers().stream().map(roleMapper::toRoleUserResponse).collect(Collectors.toList()));
        roleResponse.setPermissions(role.getPermissions().stream().map(roleMapper::toRolePermissionResponse).collect(Collectors.toList()));
        return roleResponse;
    }

    @Transactional
    public List<RoleResponse> getRoles() {
        List<Role> roles = roleRepo.findAllByIsDeletedFalse();
        if (roles.isEmpty()) {
            throw new AppException(ErrorCode.ROLE_NOT_FOUND);
        }
        return roles.stream().map(role -> {
            RoleResponse roleResponse = roleMapper.toRoleResponse(role);
            List<User> users = userRepo.findUsersByRoleIdAndIsDeletedFalse(role.getId());
            List<RoleUserResponse> userResponses = users.stream()
                    .map(roleMapper::toRoleUserResponse)
                    .collect(Collectors.toList());
            roleResponse.setUsers(userResponses);
            List<Permission> permissions = permissionRepo.findPermissionsByRoleIdAndIsDeletedFalse(role.getId());
            List<RolePermissionResponse> permissionResponses = permissions.stream()
                    .map(roleMapper::toRolePermissionResponse)
                    .collect(Collectors.toList());
            roleResponse.setPermissions(permissionResponses);
            roleResponse.setUsers(role.getUsers().stream().map(roleMapper::toRoleUserResponse).collect(Collectors.toList()));
            roleResponse.setPermissions(role.getPermissions().stream().map(roleMapper::toRolePermissionResponse).collect(Collectors.toList()));
            return roleResponse;
        }).collect(Collectors.toList());
    }

    @Transactional
    public RoleResponse getRoleById(Long id) {
        Role role = roleRepo.findRoleByIdAndIsDeletedFalse(id).orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
        RoleResponse roleResponse = roleMapper.toRoleResponse(role);
        List<User> users = userRepo.findUsersByRoleIdAndIsDeletedFalse(role.getId());
        List<RoleUserResponse> userResponses = users.stream()
                .map(roleMapper::toRoleUserResponse)
                .collect(Collectors.toList());
        roleResponse.setUsers(userResponses);
        List<Permission> permissions = permissionRepo.findPermissionsByRoleIdAndIsDeletedFalse(role.getId());
        List<RolePermissionResponse> permissionResponses = permissions.stream()
                .map(roleMapper::toRolePermissionResponse)
                .collect(Collectors.toList());
        roleResponse.setPermissions(permissionResponses);
        return roleResponse;
    }

    @Transactional
//    @PreAuthorize("hasAuthority('ROLE_UPDATE_ROLE_GROUP')")
    public RoleResponse updateRole(Long id, RoleUpdateRequest request) {
        Role role = roleRepo.findRoleByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
        roleMapper.toRoleUpdate(role, request);
        RoleUpdateRequest currentDto = roleMapper.toRoleUpdateRequest(role);
        Set<Long> currentUserIds = role.getUsers().stream()
                .map(User::getId)
                .collect(Collectors.toSet());
        Set<Long> currentPermissionIds = role.getPermissions().stream()
                .map(Permission::getId)
                .collect(Collectors.toSet());
        if(currentUserIds.equals(request.getUserIds()) && currentPermissionIds.equals(request.getPermissionIds()) && currentDto.equals(request)) {
            throw new AppException(ErrorCode.ROLE_NOT_CHANGED);
        }
        List<User> users = userRepo.findAllByIdInAndIsDeletedFalse(request.getUserIds());
        if (users.size() != request.getUserIds().size()) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        role.setUsers(new HashSet<>(users));
        List<Permission> permissions = permissionRepo.findAllByIdInAndIsDeletedFalse(request.getPermissionIds());
        if (permissions.size() != request.getPermissionIds().size()) {
            throw new AppException(ErrorCode.PERMISSION_NOT_FOUND);
        }
        role.setPermissions(new HashSet<>(permissions));
        role.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
        roleRepo.save(role);
        RoleResponse roleResponse = roleMapper.toRoleResponse(role);
        roleResponse.setUsers(role.getUsers().stream().map(roleMapper::toRoleUserResponse).collect(Collectors.toList()));
        roleResponse.setPermissions(role.getPermissions().stream().map(roleMapper::toRolePermissionResponse).collect(Collectors.toList()));
        return roleResponse;
    }

    @Transactional
    public void deleteRole(Long id) {
        Role role = roleRepo.findRoleByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
        role.setIsDeleted(1);
        role.setDeletedAt(Timestamp.valueOf(LocalDateTime.now()));
        roleRepo.save(role);
    }

    @Transactional
    public Page<RoleResponse> getRolesByPage(int page, int size) {
        if (page < 1) {
            throw new AppException(ErrorCode.PAGE_NO_ERROR);
        }
        if (size < 1) {
            throw new AppException(ErrorCode.PAGE_SIZE_ERROR);
        }
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Role> roles = roleRepo.searchByIsDeletedFalseAndPageable(pageable);
        if (roles.isEmpty()) {
            throw new AppException(ErrorCode.ROLE_NOT_FOUND);
        }
        return roles.map(role -> {
            RoleResponse roleResponse = roleMapper.toRoleResponse(role);
            List<User> users = userRepo.findUsersByRoleIdAndIsDeletedFalse(role.getId());
            roleResponse.setUsers(users.stream()
                    .map(roleMapper::toRoleUserResponse)
                    .collect(Collectors.toList()));
            return roleResponse;
        });
    }

    @Transactional
    public void restoreRole(Long id) {
        Role role = roleRepo.findById(id).orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
        if (role.getIsDeleted() == 0) {
            throw new AppException(ErrorCode.ROLE_NOT_DELETED);
        }
        role.setIsDeleted(0);
        role.setDeletedAt(null);
        role.setDeletedBy(null);
        roleRepo.save(role);
    }
}
