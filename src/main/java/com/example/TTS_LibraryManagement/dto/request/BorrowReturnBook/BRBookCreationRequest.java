package com.example.TTS_LibraryManagement.dto.request.BorrowReturnBook;

import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
public class BRBookCreationRequest {
    @NotNull(message = "Book ID cannot be null")
    @Positive(message = "Book ID must be a positive number")
    Long bookId;

    @NotNull(message = "Return date cannot be null")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Timestamp returnAt;

}