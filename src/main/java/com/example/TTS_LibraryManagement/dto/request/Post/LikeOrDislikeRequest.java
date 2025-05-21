package com.example.TTS_LibraryManagement.dto.request.Post;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
public class LikeOrDislikeRequest {
    Long postId;
    String likeOrDislike;
}
