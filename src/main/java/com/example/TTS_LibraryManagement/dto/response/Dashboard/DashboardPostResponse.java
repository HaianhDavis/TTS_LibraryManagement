package com.example.TTS_LibraryManagement.dto.response.Dashboard;

import com.example.TTS_LibraryManagement.dto.response.Post.PostBookResponse;
import com.example.TTS_LibraryManagement.dto.response.Post.PostUserResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DashboardPostResponse {
    Long postId;
    String postTitle;
    int totalLikes;
}
