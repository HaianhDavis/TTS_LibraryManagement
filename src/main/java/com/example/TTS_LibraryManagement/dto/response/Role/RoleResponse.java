package com.example.TTS_LibraryManagement.dto.response.Role;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleResponse {
    Long id;
    String roleGroupCode;
    String roleGroupName;
    String description;
    Timestamp createdAt;
    Timestamp updatedAt;
    List<RoleUserResponse> users;
    List<RolePermissionResponse> permissions;
    String createdBy;
    String updatedBy;
    Timestamp deletedAt;
    String deletedBy;
}
