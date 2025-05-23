package com.example.TTS_LibraryManagement.dto.request.Role;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleUpdateRequest {
    String roleGroupCode;
    String roleGroupName;
    String description;
    Set<Long> userIds;
    Set<Long> permissionIds;
}
