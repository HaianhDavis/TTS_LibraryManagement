package com.example.TTS_LibraryManagement.dto.request.Book;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
public class ErrorRecordBook {
    int rowNumber;
    String errorCode;
    String errorMessage;
}
