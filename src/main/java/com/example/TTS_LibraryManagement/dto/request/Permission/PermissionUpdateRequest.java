package com.example.TTS_LibraryManagement.dto.request.Permission;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PermissionUpdateRequest {
    String functionCode;
    String functionName;
    String description;
    List<Long> roleIds;
}
