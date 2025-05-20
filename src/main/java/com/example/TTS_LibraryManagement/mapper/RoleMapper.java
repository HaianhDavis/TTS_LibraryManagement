package com.example.TTS_LibraryManagement.mapper;

import com.example.TTS_LibraryManagement.dto.request.Role.RoleCreationRequest;
import com.example.TTS_LibraryManagement.dto.request.Role.RoleUpdateRequest;
import com.example.TTS_LibraryManagement.dto.response.Role.RolePermissionResponse;
import com.example.TTS_LibraryManagement.dto.response.Role.RoleResponse;
import com.example.TTS_LibraryManagement.dto.response.Role.RoleUserResponse;
import com.example.TTS_LibraryManagement.entity.Permission;
import com.example.TTS_LibraryManagement.entity.Role;
import com.example.TTS_LibraryManagement.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "users", ignore = true)
    @Mapping(target = "permissions", ignore = true)
    Role toRoleCreate(RoleCreationRequest request);

    @Mapping(target = "users", ignore = true)
    @Mapping(target = "permissions", ignore = true)
    RoleResponse toRoleResponse(Role role);

    @Mapping(target = "users" , ignore = true)
    @Mapping(target = "permissions", ignore = true)
    void toRoleUpdate(@MappingTarget Role role, RoleUpdateRequest request);

    RoleUserResponse toRoleUserResponse(User user);

    RolePermissionResponse toRolePermissionResponse(Permission permission);

    RoleUpdateRequest toRoleUpdateRequest(Role role);
}
