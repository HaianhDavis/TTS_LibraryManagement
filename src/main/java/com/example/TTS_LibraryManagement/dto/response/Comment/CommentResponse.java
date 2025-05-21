package com.example.TTS_LibraryManagement.dto.response.Comment;

import com.example.TTS_LibraryManagement.dto.response.Post.PostBookResponse;
import com.example.TTS_LibraryManagement.dto.response.Post.PostUserResponse;
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
public class CommentResponse {
    Long id;
    CommentPostResponse post;
    CommentUserResponse user;
    String content;
    Timestamp createdAt;
    Timestamp updatedAt;
    String createdBy;
    String updatedBy;
    int isDeleted;
    Timestamp deletedAt;
    String deletedBy;
}
