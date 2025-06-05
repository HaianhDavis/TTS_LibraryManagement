package com.example.TTS_LibraryManagement.dto.response.Post;

import com.example.TTS_LibraryManagement.dto.response.Book.BookCategoryResponse;
import com.example.TTS_LibraryManagement.entity.Book;
import com.example.TTS_LibraryManagement.entity.Comment;
import com.example.TTS_LibraryManagement.entity.PostLike;
import com.example.TTS_LibraryManagement.entity.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostResponse {
    Long id;
    PostUserResponse user;
    PostBookResponse book;
    String title;
    String content;
    int totalLikes;
    Timestamp createdAt;
    Timestamp updatedAt;
    String createdBy;
    String updatedBy;
    Timestamp deletedAt;
    String deletedBy;
}
