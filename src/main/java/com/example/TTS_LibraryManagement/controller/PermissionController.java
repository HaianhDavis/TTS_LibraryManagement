package com.example.TTS_LibraryManagement.controller;

import com.example.TTS_LibraryManagement.dto.request.Permission.PermissionSearchByUserRequest;
import com.example.TTS_LibraryManagement.dto.request.Permission.PermissionUpdateRequest;
import com.example.TTS_LibraryManagement.dto.response.ApiResponse;
import com.example.TTS_LibraryManagement.dto.request.Permission.PermissionCreationRequest;
import com.example.TTS_LibraryManagement.dto.response.Permission.PermissionResponse;
import com.example.TTS_LibraryManagement.dto.response.Role.RoleResponse;
import com.example.TTS_LibraryManagement.service.PermissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
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
    static final Logger logger = LoggerFactory.getLogger(PermissionController.class);

    @Operation(summary = "Create a new permission")
    @PostMapping("/create")
    @PreAuthorize("fileRole(#httpServletRequest)")
    ApiResponse<PermissionResponse> createPermission(HttpServletRequest httpServletRequest, @RequestBody PermissionCreationRequest request) {
        ApiResponse<PermissionResponse> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Successfully created permission");
        apiResponse.setResult(permissionService.createPermission(request));
        return apiResponse;
    }

    @Operation(summary = "Get all permissions")
    @GetMapping
    @PreAuthorize("fileRole(#httpServletRequest)")
    ApiResponse<List<PermissionResponse>> getPermissions(HttpServletRequest httpServletRequest) {
        ApiResponse<List<PermissionResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Successfully retrieved users");
        apiResponse.setResult(permissionService.getPermissions());
        return apiResponse;
    }

    @Operation(summary = "Get permission by ID")
    @GetMapping("/detail/{id}")
    @PreAuthorize("fileRole(#httpServletRequest)")
    ApiResponse<PermissionResponse> getPermissionById(HttpServletRequest httpServletRequest, @PathVariable("id") Long userId) {
        ApiResponse<PermissionResponse> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Successfully retrieved user with ID " + userId);
        apiResponse.setResult(permissionService.getPermissionById(userId));
        return apiResponse;
    }

    @Operation(summary = "Update permission by ID")
    @PutMapping("/update/{id}")
    @PreAuthorize("fileRole(#httpServletRequest)")
    ApiResponse<PermissionResponse> updatePermission(HttpServletRequest httpServletRequest, @PathVariable Long id, @RequestBody PermissionUpdateRequest request) {
        ApiResponse<PermissionResponse> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Successfully updated user");
        apiResponse.setResult(permissionService.updatePermission(id, request));
        return apiResponse;
    }

    @Operation(summary = "Delete permission by ID")
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("fileRole(#httpServletRequest)")
    ApiResponse<PermissionResponse> deletePermission(HttpServletRequest httpServletRequest, @PathVariable Long id) {
        ApiResponse<PermissionResponse> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Successfully deleted user");
        permissionService.deletePermission(id);
        return apiResponse;
    }

    @Operation(summary = "Search permissions by user")
    @PostMapping("/find-by-user")
    @PreAuthorize("fileRole(#httpServletRequest)")
    ApiResponse<Page<PermissionResponse>> searchPermissions(HttpServletRequest httpServletRequest, @RequestParam(name = "pageNo") int pageNo, @RequestParam(name = "pageSize") int pageSize, @RequestBody PermissionSearchByUserRequest request) {
        ApiResponse<Page<PermissionResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Successfully retrieved permissions");
        apiResponse.setResult(permissionService.getPermissionsByPage(pageNo, pageSize, request));
        return apiResponse;
    }

    @Operation(summary = "Restore a permission by ID")
    @PatchMapping("/restore/{id}")
    @PreAuthorize("fileRole(#httpServletRequest)")
    ApiResponse<RoleResponse> restorePermission(HttpServletRequest httpServletRequest, @PathVariable Long id) {
        ApiResponse<RoleResponse> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Successfully restored permission with id " + id);
        permissionService.restorePermission(id);
        return apiResponse;
    }
}
