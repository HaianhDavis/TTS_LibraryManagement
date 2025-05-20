package com.example.TTS_LibraryManagement.dto.request.Book;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookSearchRequest {
    String code;
    String title;
    String author;
    String publisher;
    int pageCount;
    String printType;
    String language;
}
