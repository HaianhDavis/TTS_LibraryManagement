package com.example.TTS_LibraryManagement.controller;

import com.example.TTS_LibraryManagement.dto.request.Permission.PermissionSearchByUserRequest;
import com.example.TTS_LibraryManagement.dto.request.Permission.PermissionUpdateRequest;
import com.example.TTS_LibraryManagement.dto.response.ApiResponse;
import com.example.TTS_LibraryManagement.dto.request.Permission.PermissionCreationRequest;
import com.example.TTS_LibraryManagement.dto.response.Permission.PermissionResponse;
import com.example.TTS_LibraryManagement.dto.response.Role.RoleResponse;
import com.example.TTS_LibraryManagement.service.PermissionService;
import com.example.TTS_LibraryManagement.utils.ApiUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permission")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Permission", description = "Permission Management APIs")
public class PermissionController {
    PermissionService permissionService;

    @Operation(summary = "Create a new permission")
    @PostMapping("/create")
    @PreAuthorize("fileRole(#httpServletRequest)")
    ApiResponse<PermissionResponse> createPermission(HttpServletRequest httpServletRequest, @RequestBody @Valid PermissionCreationRequest request) {
        return ApiUtils.success(permissionService.createPermission(request));
    }

    @Operation(summary = "Get all permissions")
    @GetMapping
    @PreAuthorize("fileRole(#httpServletRequest)")
    ApiResponse<List<PermissionResponse>> getPermissions(HttpServletRequest httpServletRequest) {
        return ApiUtils.success(permissionService.getPermissions());
    }

    @Operation(summary = "Get permission by ID")
    @GetMapping("/detail/{id}")
    @PreAuthorize("fileRole(#httpServletRequest)")
    ApiResponse<PermissionResponse> getPermissionById(HttpServletRequest httpServletRequest, @PathVariable("id") Long userId) {
        return ApiUtils.success(permissionService.getPermissionById(userId));
    }

    @Operation(summary = "Update permission by ID")
    @PutMapping("/update/{id}")
    @PreAuthorize("fileRole(#httpServletRequest)")
    ApiResponse<PermissionResponse> updatePermission(HttpServletRequest httpServletRequest, @PathVariable Long id, @RequestBody @Valid PermissionUpdateRequest request) {
        return ApiUtils.success(permissionService.updatePermission(id,request));
    }

    @Operation(summary = "Delete permission by ID")
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("fileRole(#httpServletRequest)")
    ApiResponse<PermissionResponse> deletePermission(HttpServletRequest httpServletRequest, @PathVariable Long id) {
        permissionService.deletePermission(id);
        return ApiUtils.successDeleteOrRestore("Successfully deleted permission with id " + id);
    }

    @Operation(summary = "Search permissions by user")
    @PostMapping("/find-by-user")
    @PreAuthorize("fileRole(#httpServletRequest)")
    ApiResponse<Page<PermissionResponse>> searchPermissions(HttpServletRequest httpServletRequest, @RequestParam(name = "pageNo") int pageNo, @RequestParam(name = "pageSize") int pageSize, @RequestBody PermissionSearchByUserRequest request) {
        return ApiUtils.success(permissionService.getPermissionsByPage(pageNo, pageSize, request));
    }

    @Operation(summary = "Restore a permission by ID")
    @PatchMapping("/restore/{id}")
    @PreAuthorize("fileRole(#httpServletRequest)")
    ApiResponse<RoleResponse> restorePermission(HttpServletRequest httpServletRequest, @PathVariable Long id) {
        permissionService.restorePermission(id);
        return ApiUtils.successDeleteOrRestore("Successfully restored permission with id " + id);
    }
}
