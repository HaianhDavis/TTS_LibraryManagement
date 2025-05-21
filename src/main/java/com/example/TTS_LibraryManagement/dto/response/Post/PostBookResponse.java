package com.example.TTS_LibraryManagement.dto.response.Post;

import com.example.TTS_LibraryManagement.dto.response.Book.BookCategoryResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostBookResponse {
    String code;
    String title;
    List<BookCategoryResponse> categories;
}
