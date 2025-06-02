package com.example.TTS_LibraryManagement.dto.request.Category;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryUpdateRequest {
    @NotBlank(message = "NOT_BLANK")
    String categoryCode;
    @NotBlank(message = "NOT_BLANK")
    String categoryName;
}
