package com.example.TTS_LibraryManagement.dto.response.BorrowReturnBook;

import com.example.TTS_LibraryManagement.entity.Book;
import com.example.TTS_LibraryManagement.entity.User;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BorrowReturnBookResponse {
    Long id;
    BRUserResponse user;
    BRBookResponse book;
    Timestamp borrowedAt;
    Timestamp returnAt;
    Timestamp returnedAt;
    String status;
    Timestamp createdAt;
    String createdBy;
    Timestamp updatedAt;
    String updatedBy;
    Timestamp deletedAt;
    String deletedBy;

}
