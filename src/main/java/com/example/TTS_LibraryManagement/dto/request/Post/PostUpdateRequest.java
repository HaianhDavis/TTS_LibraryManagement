package com.example.TTS_LibraryManagement.dto.request.Post;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
public class PostUpdateRequest {
    @NotBlank(message = "NOT_BLANK")
    String title;
    @NotBlank(message = "NOT_BLANK")
    String content;
}
