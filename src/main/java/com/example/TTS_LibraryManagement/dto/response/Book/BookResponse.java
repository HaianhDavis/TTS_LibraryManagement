package com.example.TTS_LibraryManagement.dto.response.Book;

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
public class BookResponse {
    Long id;
    String code;
    String title;
    String author;
    String publisher;
    int pageCount;
    String printType;
    String language;
    String description;
    int borrowed_quantity;
    int quantity;
    Timestamp createdAt;
    Timestamp updatedAt;
    String createdBy;
    String updatedBy;
    Timestamp deletedAt;
    String deletedBy;
    List<BookCategoryResponse> categories;
}
