package com.example.TTS_LibraryManagement.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "books")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String code;
    String title;
    String author;
    String publisher;
    int pageCount;
    String printType;
    String language;
    String description;
    int status;
    int borrowedQuantity;
    int quantity;
    Timestamp createdAt;
    Timestamp updatedAt;
    String createdBy;
    String updatedBy;
    int isDeleted;
    Timestamp deletedAt;
    String deletedBy;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "book_categories",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    Set<Category> categories;

    @OneToMany(mappedBy = "book")
    Set<BorrowReturnBook> borrowRecords;

    @OneToMany(mappedBy = "book")
    Set<Post> posts;
}
