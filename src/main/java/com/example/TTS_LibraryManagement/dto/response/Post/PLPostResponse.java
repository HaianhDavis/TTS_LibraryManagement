package com.example.TTS_LibraryManagement.dto.response.Post;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PLPostResponse {
    PostUserResponse user;
    PostBookResponse book;
    String title;
    String content;
}
