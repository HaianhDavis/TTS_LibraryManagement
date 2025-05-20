package com.example.TTS_LibraryManagement.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;

@Entity
@Table(name = "borrow_return_book")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
public class BorrowReturnBook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    Book book;

    Timestamp borrowedAt;
    Timestamp returnAt;
    Timestamp returnedAt;
    int status; // 0: đang mượn, 1: đã trả
    Timestamp createdAt;
    String createdBy;
    Timestamp updatedAt;
    String updatedBy;
    int isDeleted;
    Timestamp deletedAt;
    String deletedBy;
}
