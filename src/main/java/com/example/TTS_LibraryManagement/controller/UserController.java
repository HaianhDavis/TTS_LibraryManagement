package com.example.TTS_LibraryManagement.controller;


import com.example.TTS_LibraryManagement.dto.request.User.UserSearchRequest;
import com.example.TTS_LibraryManagement.dto.response.ApiResponse;
import com.example.TTS_LibraryManagement.dto.request.User.UserCreationRequest;
import com.example.TTS_LibraryManagement.dto.request.User.UserUpdateRequest;

import com.example.TTS_LibraryManagement.dto.response.Role.RoleResponse;
import com.example.TTS_LibraryManagement.dto.response.User.UserResponse;
import com.example.TTS_LibraryManagement.service.UserService;
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
@RequestMapping("/api/v1/library/user")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService userService;

    @PostMapping("/create")
    ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest request) {
        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Successfully created user");
        apiResponse.setResult(userService.createUser(request));
        return apiResponse;
    }

    @GetMapping
    ApiResponse<List<UserResponse>> getUsers() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("Username : {}", authentication.getName());
        authentication.getAuthorities().forEach(grantedAuthority -> log.info(grantedAuthority.getAuthority()));
        ApiResponse<List<UserResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Successfully retrieved users");
        apiResponse.setResult(userService.getUsers());
        return apiResponse;
    }

    @GetMapping("/detail/{id}")
    ApiResponse<UserResponse> getUserById(@PathVariable("id") Long userId) {
        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Successfully retrieved user with ID " + userId);
        apiResponse.setResult(userService.getUserById(userId));
        return apiResponse;
    }

    @PutMapping("/update/{id}")
    ApiResponse<UserResponse> updateUser(@PathVariable Long id, @RequestBody UserUpdateRequest request) {
        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Successfully updated user");
        apiResponse.setResult(userService.updateUser(id, request));
        return apiResponse;
    }

    @DeleteMapping("/delete/{id}")
    ApiResponse<UserResponse> deleteUser(@PathVariable Long id) {
        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Successfully deleted user with ID: " + id);
        userService.deleteUser(id);
        return apiResponse;
    }

    @PostMapping("/search")
    ApiResponse<Page<UserResponse>> searchUser(@RequestParam(name = "pageNo") int pageNo, @RequestParam(name = "pageSize") int pageSize, @RequestBody UserSearchRequest request) {
        ApiResponse<Page<UserResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Successfully retrieved users");
        apiResponse.setResult(userService.getUsersByPage(pageNo,pageSize,request));
        return apiResponse;
    }

    @PatchMapping("/restore/{id}")
    ApiResponse<RoleResponse> restoreUser(@PathVariable Long id) {
        ApiResponse<RoleResponse> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Successfully restored user with id " + id);
        userService.restoreUser(id);
        return apiResponse;
    }
}
