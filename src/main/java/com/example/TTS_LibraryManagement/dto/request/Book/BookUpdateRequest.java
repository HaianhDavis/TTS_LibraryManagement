package com.example.TTS_LibraryManagement.dto.request.Book;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookUpdateRequest {
    String code;
    String title;
    String author;
    String publisher;
    int pageCount;
    String printType;
    String language;
    String description;
    int quantity;
    Set<Long> categoryIds;
}
