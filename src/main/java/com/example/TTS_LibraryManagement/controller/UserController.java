package com.example.TTS_LibraryManagement.controller;


import com.example.TTS_LibraryManagement.dto.request.User.UserSearchRequest;
import com.example.TTS_LibraryManagement.dto.response.ApiResponse;
import com.example.TTS_LibraryManagement.dto.request.User.UserCreationRequest;
import com.example.TTS_LibraryManagement.dto.request.User.UserUpdateRequest;

import com.example.TTS_LibraryManagement.dto.response.Role.RoleResponse;
import com.example.TTS_LibraryManagement.dto.response.User.UserResponse;
import com.example.TTS_LibraryManagement.service.UserService;
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
    @PreAuthorize("fileRole(#httpServletRequest)")
    ApiResponse<UserResponse> createUser(HttpServletRequest httpServletRequest, @RequestBody @Valid UserCreationRequest request) {
        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Successfully created user");
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
        ApiResponse<List<UserResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Successfully retrieved users");
        apiResponse.setResult(userService.getUsers());
        return apiResponse;
    }

    @Operation(summary = "Lấy người dùng theo ID")
    @GetMapping("/detail/{id}")
    @PreAuthorize("fileRole(#httpServletRequest)")
    ApiResponse<UserResponse> getUserById(HttpServletRequest httpServletRequest, @PathVariable("id") Long userId) {
        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Successfully retrieved user with ID " + userId);
        apiResponse.setResult(userService.getUserById(userId));
        return apiResponse;
    }

    @Operation(summary = "Cập nhật người dùng theo ID")
    @PutMapping("/update/{id}")
    @PreAuthorize("fileRole(#httpServletRequest)")
    ApiResponse<UserResponse> updateUser(HttpServletRequest httpServletRequest, @PathVariable Long id, @RequestBody UserUpdateRequest request) {
        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Successfully updated user");
        apiResponse.setResult(userService.updateUser(id, request));
        return apiResponse;
    }

    @Operation(summary = "Xóa người dùng theo ID")
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("fileRole(#httpServletRequest)")
    ApiResponse<UserResponse> deleteUser(HttpServletRequest httpServletRequest, @PathVariable Long id) {
        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Successfully deleted user with ID: " + id);
        userService.deleteUser(id);
        return apiResponse;
    }

    @Operation(summary = "Lấy người dùng theo vai trò")
    @PostMapping("/search")
    @PreAuthorize("fileRole(#httpServletRequest)")
    ApiResponse<Page<UserResponse>> searchUser(HttpServletRequest httpServletRequest, @RequestParam(name = "pageNo") int pageNo, @RequestParam(name = "pageSize") int pageSize, @RequestBody UserSearchRequest request) {
        ApiResponse<Page<UserResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Successfully retrieved users");
        apiResponse.setResult(userService.getUsersByPage(pageNo,pageSize,request));
        return apiResponse;
    }

    @Operation(summary = "Khôi phục người dùng đã xóa")
    @PatchMapping("/restore/{id}")
    @PreAuthorize("fileRole(#httpServletRequest)")
    ApiResponse<RoleResponse> restoreUser(HttpServletRequest httpServletRequest, @PathVariable Long id) {
        ApiResponse<RoleResponse> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Successfully restored user with id " + id);
        userService.restoreUser(id);
        return apiResponse;
    }
}
