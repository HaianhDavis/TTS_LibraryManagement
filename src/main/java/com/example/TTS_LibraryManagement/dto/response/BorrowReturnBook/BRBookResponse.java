package com.example.TTS_LibraryManagement.dto.response.BorrowReturnBook;

import com.example.TTS_LibraryManagement.dto.response.Book.BookCategoryResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BRBookResponse {
    String code;
    String title;
    List<BookCategoryResponse> categories;
}
