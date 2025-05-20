package com.example.TTS_LibraryManagement.dto.request.BorrowReturnBook;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
public class BRBookCreationRequest {
    Long userId;
    Long bookId;
    Timestamp returnAt;
}
