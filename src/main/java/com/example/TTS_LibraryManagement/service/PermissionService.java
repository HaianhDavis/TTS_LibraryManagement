package com.example.TTS_LibraryManagement.service;

import com.example.TTS_LibraryManagement.dto.request.Permission.PermissionCreationRequest;
import com.example.TTS_LibraryManagement.dto.request.Permission.PermissionSearchByUserRequest;
import com.example.TTS_LibraryManagement.dto.request.Permission.PermissionUpdateRequest;
import com.example.TTS_LibraryManagement.dto.response.Permission.PermissionResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PermissionService {
    PermissionResponse createPermission(PermissionCreationRequest request);
    PermissionResponse updatePermission(Long id, PermissionUpdateRequest request);
    void deletePermission(Long id);
    List<PermissionResponse> getPermissions();
    PermissionResponse getPermissionById(Long id);
    Page<PermissionResponse> getPermissionsByPage(int page, int size, PermissionSearchByUserRequest request);
    void restorePermission(Long permissionId);
}
