package com.example.TTS_LibraryManagement.utils;

import com.example.TTS_LibraryManagement.dto.request.Book.ErrorRecordBook;
import com.example.TTS_LibraryManagement.dto.response.ApiResponse;

import java.util.List;

public class ApiUtils {
    // Hàm success để tạo phản hồi thành công
    public static <T> ApiResponse<T> success(T result) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setResult(result);
        return response;
    }

    public static <T> ApiResponse<T> successDeleteOrRestore(String message) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setMessage(message);
        return response;
    }

    public static <T> ApiResponse<T> error(String code, String message, List<ErrorRecordBook> errors) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode(code);
        response.setMessage(message);
        response.setError(errors);
        return response;
    }
}
