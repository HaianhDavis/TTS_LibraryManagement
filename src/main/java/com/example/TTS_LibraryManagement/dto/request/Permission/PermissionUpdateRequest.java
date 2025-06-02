package com.example.TTS_LibraryManagement.dto.request.Permission;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PermissionUpdateRequest {
    @NotBlank(message = "NOT_BLANK")
    String functionCode;
    @NotBlank(message = "NOT_BLANK")
    String functionName;
    @NotBlank(message = "NOT_BLANK")
    String description;
}
