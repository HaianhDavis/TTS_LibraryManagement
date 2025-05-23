package com.example.TTS_LibraryManagement.controller;

import com.example.TTS_LibraryManagement.dto.request.Permission.PermissionSearchByUserRequest;
import com.example.TTS_LibraryManagement.dto.request.Permission.PermissionUpdateRequest;
import com.example.TTS_LibraryManagement.dto.response.ApiResponse;
import com.example.TTS_LibraryManagement.dto.request.Permission.PermissionCreationRequest;
import com.example.TTS_LibraryManagement.dto.response.Permission.PermissionResponse;
import com.example.TTS_LibraryManagement.dto.response.Role.RoleResponse;
import com.example.TTS_LibraryManagement.service.PermissionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permission")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionController {
    PermissionService permissionService;
    static final Logger logger = LoggerFactory.getLogger(PermissionController.class);
    @PostMapping("/create")
    ApiResponse<PermissionResponse> createPermission(@RequestBody PermissionCreationRequest request) {
        ApiResponse<PermissionResponse> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Successfully created permission");
        apiResponse.setResult(permissionService.createPermission(request));
        return apiResponse;
    }

    @GetMapping
    ApiResponse<List<PermissionResponse>> getPermissions() {
        ApiResponse<List<PermissionResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Successfully retrieved users");
        apiResponse.setResult(permissionService.getPermissions());
        return apiResponse;
    }

    @GetMapping("/detail/{id}")
    ApiResponse<PermissionResponse> getPermissionById(@PathVariable("id") Long userId) {
        ApiResponse<PermissionResponse> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Successfully retrieved user with ID " + userId);
        apiResponse.setResult(permissionService.getPermissionById(userId));
        return apiResponse;
    }

    @PutMapping("/update/{id}")
    ApiResponse<PermissionResponse> updatePermission(@PathVariable Long id, @RequestBody PermissionUpdateRequest request) {
        ApiResponse<PermissionResponse> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Successfully updated user");
        apiResponse.setResult(permissionService.updatePermission(id, request));
        return apiResponse;
    }

    @DeleteMapping("/delete/{id}")
    ApiResponse<PermissionResponse> deletePermission(@PathVariable Long id) {
        ApiResponse<PermissionResponse> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Successfully deleted user");
        permissionService.deletePermission(id);
        return apiResponse;
    }

    @PostMapping("/find-by-user")
    ApiResponse<Page<PermissionResponse>> searchPermissions(@RequestParam(name = "pageNo") int pageNo, @RequestParam(name = "pageSize") int pageSize, @RequestBody PermissionSearchByUserRequest request) {
        ApiResponse<Page<PermissionResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Successfully retrieved permissions");
        apiResponse.setResult(permissionService.getPermissionsByPage(pageNo,pageSize,request));
        return apiResponse;
    }

    @PatchMapping("/restore/{id}")
    ApiResponse<RoleResponse> restorePermission(@PathVariable Long id) {
        ApiResponse<RoleResponse> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Successfully restored permission with id " + id);
        permissionService.restorePermission(id);
        return apiResponse;
    }
}
