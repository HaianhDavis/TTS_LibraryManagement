package com.example.TTS_LibraryManagement.dto.request.Comment;

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
    Long postId;
    String content;
}
