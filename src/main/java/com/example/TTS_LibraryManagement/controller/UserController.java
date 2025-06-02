package com.example.TTS_LibraryManagement.controller;


import com.example.TTS_LibraryManagement.dto.request.User.UserSearchRequest;
import com.example.TTS_LibraryManagement.dto.response.ApiResponse;
import com.example.TTS_LibraryManagement.dto.request.User.UserCreationRequest;
import com.example.TTS_LibraryManagement.dto.request.User.UserUpdateRequest;

import com.example.TTS_LibraryManagement.dto.response.Role.RoleResponse;
import com.example.TTS_LibraryManagement.dto.response.User.UserResponse;
import com.example.TTS_LibraryManagement.service.UserService;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "User", description = "User Management APIs")
public class UserController {
    UserService userService;

    @Operation(summary = "Tạo người dùng mới")
    @PostMapping("/create")
    ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest request) {
        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.createUser(request));
        return apiResponse;
    }

    @Operation(summary = "Lấy danh sách người dùng")
    @GetMapping
    @PreAuthorize("fileRole(#httpServletRequest)")
    ApiResponse<List<UserResponse>> getUsers(HttpServletRequest httpServletRequest) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("Username : {}", authentication.getName());
        authentication.getAuthorities().forEach(grantedAuthority -> log.info(grantedAuthority.getAuthority()));
        return ApiUtils.success(userService.getUsers());
    }

    @Operation(summary = "Lấy người dùng theo ID")
    @GetMapping("/detail/{id}")
    @PreAuthorize("fileRole(#httpServletRequest)")
    ApiResponse<UserResponse> getUserById(HttpServletRequest httpServletRequest, @PathVariable("id") Long userId) {
        return ApiUtils.success(userService.getUserById(userId));
    }

    @Operation(summary = "Cập nhật người dùng theo ID")
    @PutMapping("/update/{id}")
    @PreAuthorize("fileRole(#httpServletRequest)")
    ApiResponse<UserResponse> updateUser(HttpServletRequest httpServletRequest, @PathVariable Long id, @RequestBody @Valid UserUpdateRequest request) {
        return ApiUtils.success(userService.updateUser(id, request));
    }

    @Operation(summary = "Xóa người dùng theo ID")
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("fileRole(#httpServletRequest)")
    ApiResponse<UserResponse> deleteUser(HttpServletRequest httpServletRequest, @PathVariable Long id) {
        userService.deleteUser(id);
        return ApiUtils.successDeleteOrRestore("Successfully deleted user with ID: " + id);
    }

    @Operation(summary = "Lấy người dùng theo vai trò")
    @PostMapping("/search")
    @PreAuthorize("fileRole(#httpServletRequest)")
    ApiResponse<Page<UserResponse>> searchUser(HttpServletRequest httpServletRequest, @RequestParam(name = "pageNo") int pageNo, @RequestParam(name = "pageSize") int pageSize, @RequestBody UserSearchRequest request) {
        return ApiUtils.success(userService.getUsersByPage(pageNo, pageSize, request));
    }

    @Operation(summary = "Khôi phục người dùng đã xóa")
    @PatchMapping("/restore/{id}")
    @PreAuthorize("fileRole(#httpServletRequest)")
    ApiResponse<RoleResponse> restoreUser(HttpServletRequest httpServletRequest, @PathVariable Long id) {
        userService.restoreUser(id);
        return ApiUtils.successDeleteOrRestore("Successfully restored user with ID: " + id);
    }
}
