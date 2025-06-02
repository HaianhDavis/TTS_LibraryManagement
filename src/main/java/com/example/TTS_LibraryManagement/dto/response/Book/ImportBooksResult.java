package com.example.TTS_LibraryManagement.dto.response.Book;

import com.example.TTS_LibraryManagement.dto.request.Book.ErrorRecordBook;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ImportBooksResult {
    List<BookResponse> successfulImports;
    List<ErrorRecordBook> errorRecords;
}
