package com.example.TTS_LibraryManagement.dto.request.Comment;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
public class CommentCreationRequest {
    @NotBlank(message = "NOT_BLANK")
    Long postId;
    @NotBlank(message = "NOT_BLANK")
    String content;
}
