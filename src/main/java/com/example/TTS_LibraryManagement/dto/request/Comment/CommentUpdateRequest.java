package com.example.TTS_LibraryManagement.dto.request.Comment;

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
public class CommentUpdateRequest {
    @NotBlank(message = "NOT_BLANK")
    Long postId;
    @NotBlank(message = "NOT_BLANK")
    String content;
}
