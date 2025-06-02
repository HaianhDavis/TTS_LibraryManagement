package com.example.TTS_LibraryManagement.exception;

import com.example.TTS_LibraryManagement.enums.ErrorCode;
import lombok.Data;

import java.util.List;

@Data
public class AppException extends RuntimeException{
    public AppException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public AppException(ErrorCode errorCode, List<?> errors) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.errors = errors;
    }

    private ErrorCode errorCode;
    private List<?> errors;
}
