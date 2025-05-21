package com.example.TTS_LibraryManagement.dto.request.Book;

import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookUpdateRequest {
    @NotBlank(message = "NOT_BLANK")
    String code;

    @NotBlank(message = "NOT_BLANK")
    @Size(max = 100, message = "INVALID_TITLE")
    String title;

    @NotBlank(message = "NOT_BLANK")
    @Size(max = 100, message = "INVALID_AUTHORS")
    String author;

    @NotBlank(message = "NOT_BLANK")
    @Size(max = 100, message = "INVALID_PUBLISHER")
    String publisher;

    @Min(value = 1, message = "PAGE_NO_ERROR")
    int pageCount;

    @NotBlank(message = "NOT_BLANK")
    @Size(max = 50, message = "INVALID_PRINT_TYPE")
    @Pattern(regexp = "BOOK", message = "INVALID_PRINT_TYPE")
    String printType;

    @NotBlank(message = "NOT_BLANK")
    @Size(max = 50, message = "INVALID_LANGUAGE")
    @Pattern(regexp = "vi|en", message = "INVALID_LANGUAGE")
    String language;

    @Size(max = 500, message = "INVALID_DESCRIPTION")
    String description;

    @Min(value = 0, message = "PAGE_SIZE_ERROR")
    int quantity;

    @NotEmpty(message = "INVALID_CATEGORY")
    @Size(min = 1, message = "INVALID_CATEGORY")
    Set<Long> categoryIds;
}
