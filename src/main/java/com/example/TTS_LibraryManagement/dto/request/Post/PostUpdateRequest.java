package com.example.TTS_LibraryManagement.dto.request.Post;

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
    Long bookId;
    String title;
    String content;
}
