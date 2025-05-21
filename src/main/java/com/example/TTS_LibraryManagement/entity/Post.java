package com.example.TTS_LibraryManagement.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;
import java.util.Set;

@Entity
@Table(name = "posts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    Book book;

    String title;
    String content;
    Timestamp createdAt;
    Timestamp updatedAt;
    String createdBy;
    String updatedBy;
    int isDeleted;
    Timestamp deletedAt;
    String deletedBy;

    @OneToMany(mappedBy = "post")
    Set<PostLike> postLikes;

    @OneToMany(mappedBy = "post")
    Set<Comment> comments;
}
