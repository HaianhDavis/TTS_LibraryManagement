package com.example.TTS_LibraryManagement.dto.request.Comment;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
public class CommentCreationRequest {
    Long postId;
    String content;
}
