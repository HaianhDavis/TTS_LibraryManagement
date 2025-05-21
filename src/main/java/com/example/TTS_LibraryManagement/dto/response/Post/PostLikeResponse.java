package com.example.TTS_LibraryManagement.dto.response.Post;

import com.example.TTS_LibraryManagement.entity.Post;
import com.example.TTS_LibraryManagement.entity.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostLikeResponse {
    Long id;
    PLPostResponse post;
    PLUserResponse user;
    String isLike;
    Timestamp createdAt;
    Timestamp updatedAt;
    String createdBy;
    String updatedBy;
    Timestamp deletedAt;
    String deletedBy;
}
