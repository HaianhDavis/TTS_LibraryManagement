package com.example.TTS_LibraryManagement.mapper;

import com.example.TTS_LibraryManagement.dto.request.User.UserUpdateRequest;
import com.example.TTS_LibraryManagement.dto.response.User.UserResponse;
import com.example.TTS_LibraryManagement.entity.User;
import com.example.TTS_LibraryManagement.dto.request.User.UserCreationRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {RoleMapper.class})
public interface  UserMapper {
    User toUserCreate(UserCreationRequest request);

    UserResponse toUserResponse(User user);

    void toUserUpdate(@MappingTarget User user, UserUpdateRequest request);

    UserUpdateRequest toUserUpdateRequest(User user);
}
