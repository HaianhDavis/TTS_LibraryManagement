package com.example.TTS_LibraryManagement.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor      // Constructor không tham số
@AllArgsConstructor     // Constructor có tất cả tham số
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String username;
    String password;
    String fullname;
    String phoneNumber;
    String identityNumber;
    Integer age;
    LocalDate birthday;
    String address;
    Timestamp createdAt;
    Timestamp updatedAt;
    String createdBy;
    String updatedBy;
    int isDeleted;
    Timestamp deletedAt;
    String deletedBy;

    @ManyToMany(mappedBy = "users")
    private Set<Role> roles;

    @OneToMany(mappedBy = "user")
    Set<BorrowReturnBook> borrowRecords;

    @OneToMany(mappedBy = "user")
    Set<Post> posts;

    @OneToMany(mappedBy = "user")
    Set<PostLike> postLikes;

    @OneToMany(mappedBy = "user")
    Set<Comment> comments;
}
