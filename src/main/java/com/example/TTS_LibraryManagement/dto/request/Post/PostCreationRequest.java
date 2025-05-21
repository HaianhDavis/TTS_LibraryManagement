package com.example.TTS_LibraryManagement.dto.request.Post;

import com.example.TTS_LibraryManagement.dto.response.Post.PostBookResponse;
import com.example.TTS_LibraryManagement.dto.response.Post.PostUserResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
public class PostCreationRequest {
    Long bookId;
    String title;
    String content;
}
