package com.example.TTS_LibraryManagement.dto.response.Book;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookCategoryResponse {
    Long id;
    String categoryCode;
    String categoryName;
}
