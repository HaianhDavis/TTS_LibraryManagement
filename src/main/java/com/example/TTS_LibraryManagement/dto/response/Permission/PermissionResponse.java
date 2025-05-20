package com.example.TTS_LibraryManagement.dto.response.Permission;

import lombok.*;
import lombok.experimental.FieldDefaults;
import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PermissionResponse {
    Long id;
    String functionCode;
    String functionName;
    String description;
    Timestamp createdAt;
    Timestamp updatedAt;
    String createdBy;
    String updatedBy;
    Timestamp deletedAt;
    String deletedBy;
}
