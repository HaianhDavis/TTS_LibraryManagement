package com.example.TTS_LibraryManagement.mapper;

import com.example.TTS_LibraryManagement.dto.request.Permission.PermissionCreationRequest;
import com.example.TTS_LibraryManagement.dto.request.Permission.PermissionUpdateRequest;
import com.example.TTS_LibraryManagement.dto.response.Permission.PermissionResponse;
import com.example.TTS_LibraryManagement.entity.Permission;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermissionCreate(PermissionCreationRequest request);

    PermissionResponse toPermissionResponse(Permission permission);

    void toPermissionUpdate(@MappingTarget Permission permission, PermissionUpdateRequest request);

    PermissionUpdateRequest toPermissionUpdateRequest(Permission permission);
}
