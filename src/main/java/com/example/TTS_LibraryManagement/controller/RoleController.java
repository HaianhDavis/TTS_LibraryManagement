package com.example.TTS_LibraryManagement.controller;

import com.example.TTS_LibraryManagement.dto.response.ApiResponse;
import com.example.TTS_LibraryManagement.dto.request.Role.RoleCreationRequest;
import com.example.TTS_LibraryManagement.dto.request.Role.RoleUpdateRequest;
import com.example.TTS_LibraryManagement.dto.response.Role.RoleResponse;
import com.example.TTS_LibraryManagement.service.RoleService;
import com.example.TTS_LibraryManagement.utils.ApiUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/role")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Role", description = "Role Management APIs")
public class RoleController {
    RoleService roleService;

    @Operation(summary = "Create a new role")
    @PostMapping("/create")
    @PreAuthorize("fileRole(#httpServletRequest)")
    ApiResponse<RoleResponse> createRole(HttpServletRequest httpServletRequest, @RequestBody @Valid RoleCreationRequest request) {
        return ApiUtils.success(roleService.createRole(request));
    }

    @Operation(summary = "Get all roles")
    @GetMapping
    @PreAuthorize("fileRole(#httpServletRequest)")
    ApiResponse<List<RoleResponse>> getRoles(HttpServletRequest httpServletRequest) {
        return ApiUtils.success(roleService.getRoles());
    }

    @Operation(summary = "Get role by ID")
    @GetMapping("/detail/{id}")
    @PreAuthorize("fileRole(#httpServletRequest)")
    ApiResponse<RoleResponse> getRoleById(HttpServletRequest httpServletRequest, @PathVariable Long id) {
        return ApiUtils.success(roleService.getRoleById(id));
    }

    @Operation(summary = "Update role by ID")
    @PutMapping("/update/{id}")
    @PreAuthorize("fileRole(#httpServletRequest)")
    ApiResponse<RoleResponse> updateRole(HttpServletRequest httpServletRequest, @PathVariable Long id, @RequestBody @Valid RoleUpdateRequest request) {
        return ApiUtils.success(roleService.updateRole(id, request));
    }

    @Operation(summary = "Delete role by ID")
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("fileRole(#httpServletRequest)")
    ApiResponse<RoleResponse> deleteRole(HttpServletRequest httpServletRequest, @PathVariable Long id) {
        roleService.deleteRole(id);
        return ApiUtils.successDeleteOrRestore("Successfully deleted role with id " + id);
    }

    @Operation(summary = "Search roles by page")
    @GetMapping("/search")
    @PreAuthorize("fileRole(#httpServletRequest)")
    ApiResponse<Page<RoleResponse>> searchRoles(HttpServletRequest httpServletRequest, @RequestParam(name = "pageNo") int pageNo, @RequestParam(name = "pageSize") int pageSize) {
        return ApiUtils.success(roleService.getRolesByPage(pageNo, pageSize));
    }

    @Operation(summary = "Restore role by ID")
    @PatchMapping("/restore/{id}")
    @PreAuthorize("fileRole(#httpServletRequest)")
    ApiResponse<RoleResponse> restoreRole(HttpServletRequest httpServletRequest, @PathVariable Long id) {
        roleService.restoreRole(id);
        return ApiUtils.successDeleteOrRestore("Successfully restored role with id " + id);
    }
}
