package com.example.TTS_LibraryManagement.controller;

import com.example.TTS_LibraryManagement.dto.response.ApiResponse;
import com.example.TTS_LibraryManagement.dto.request.Role.RoleCreationRequest;
import com.example.TTS_LibraryManagement.dto.request.Role.RoleUpdateRequest;
import com.example.TTS_LibraryManagement.dto.response.Role.RoleResponse;
import com.example.TTS_LibraryManagement.service.RoleService;
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
public class RoleController {
    RoleService roleService;

    @PostMapping("/create")
    @PreAuthorize("fileRole(#httpServletRequest)")
    ApiResponse<RoleResponse> createRole(@RequestBody RoleCreationRequest request) {
        ApiResponse<RoleResponse> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Successfully created role");
        log.info(apiResponse.getMessage());
        apiResponse.setResult(roleService.createRole(request));
        return apiResponse;
    }

    @GetMapping
    @PreAuthorize("fileRole(#httpServletRequest)")
    ApiResponse<List<RoleResponse>> getRoles() {
        ApiResponse<List<RoleResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Successfully retrieved all roles");
        apiResponse.setResult(roleService.getRoles());
        return apiResponse;
    }

    @GetMapping("/detail/{id}")
    @PreAuthorize("fileRole(#httpServletRequest)")
    ApiResponse<RoleResponse> getRoleById(@PathVariable Long id) {
        ApiResponse<RoleResponse> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Successfully retrieved role with id " + id);
        apiResponse.setResult(roleService.getRoleById(id));
        return apiResponse;
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("fileRole(#httpServletRequest)")
    ApiResponse<RoleResponse> updateRole(@PathVariable Long id, @RequestBody RoleUpdateRequest request) {
        ApiResponse<RoleResponse> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Successfully updated role with id " + id);
        apiResponse.setResult(roleService.updateRole(id, request));
        return apiResponse;
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("fileRole(#httpServletRequest)")
    ApiResponse<RoleResponse> deleteRole(@PathVariable Long id) {
        ApiResponse<RoleResponse> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Successfully deleted role with id " + id);
        roleService.deleteRole(id);
        return apiResponse;
    }

    @GetMapping("/search")
    @PreAuthorize("fileRole(#httpServletRequest)")
    ApiResponse<Page<RoleResponse>> searchRoles(@RequestParam(name = "pageNo") int pageNo, @RequestParam(name = "pageSize") int pageSize) {
        ApiResponse<Page<RoleResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Successfully retrieved all roles");
        apiResponse.setResult(roleService.getRolesByPage(pageNo, pageSize));
        return apiResponse;
    }

    @PatchMapping("/restore/{id}")
    @PreAuthorize("fileRole(#httpServletRequest)")
    ApiResponse<RoleResponse> restoreRole(@PathVariable Long id) {
        ApiResponse<RoleResponse> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Successfully restored role with id " + id);
        roleService.restoreRole(id);
        return apiResponse;
    }
}
