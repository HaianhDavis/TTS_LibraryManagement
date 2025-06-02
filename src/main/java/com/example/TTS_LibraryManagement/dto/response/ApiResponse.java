package com.example.TTS_LibraryManagement.dto.response;

import com.example.TTS_LibraryManagement.dto.request.Book.ErrorRecordBook;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    String  code = "PASSED";
    String message;
    T result;
    List<?> error;
}
