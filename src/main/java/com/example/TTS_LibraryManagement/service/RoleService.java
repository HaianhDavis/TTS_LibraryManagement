package com.example.TTS_LibraryManagement.service;

import com.example.TTS_LibraryManagement.dto.request.Role.RoleCreationRequest;
import com.example.TTS_LibraryManagement.dto.request.Role.RoleUpdateRequest;
import com.example.TTS_LibraryManagement.dto.response.Role.RoleResponse;
import org.springframework.data.domain.Page;
import java.util.List;

public interface RoleService {
    RoleResponse createRole(RoleCreationRequest request);
    List<RoleResponse> getRoles();
    RoleResponse getRoleById(Long roleId);
    RoleResponse updateRole(Long roleId, RoleUpdateRequest request);
    void deleteRole(Long roleId);
    Page<RoleResponse> getRolesByPage(int page, int size);
    void restoreRole(Long id);
}