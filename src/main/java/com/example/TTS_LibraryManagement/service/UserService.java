package com.example.TTS_LibraryManagement.service;

import com.example.TTS_LibraryManagement.dto.request.User.UserCreationRequest;
import com.example.TTS_LibraryManagement.dto.request.User.UserSearchRequest;
import com.example.TTS_LibraryManagement.dto.request.User.UserUpdateRequest;
import com.example.TTS_LibraryManagement.dto.response.User.UserResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService {
    UserResponse createUser(UserCreationRequest request);
    UserResponse updateUser(Long id, UserUpdateRequest request);

    UserResponse getUserById(Long id);
    void deleteUser(Long id);

    List<UserResponse> getUsers();

    Page<UserResponse> getUsersByPage(int page, int size, UserSearchRequest request);
    void restoreUser(Long userId);
}
