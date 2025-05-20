package com.example.TTS_LibraryManagement.exception;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class TrimmedOnlyLettersValidator implements ConstraintValidator<TrimmedOnlyLetters, String> {

    @Override
    public void initialize(TrimmedOnlyLetters constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.trim().isEmpty()) {
            return true; // @NotBlank sẽ xử lý trường hợp này
        }

        // Loại bỏ khoảng trắng ở đầu/cuối và chuẩn hóa khoảng trắng giữa các từ
        String normalizedValue = value.trim().replaceAll("\\s+", " ");

        // Kiểm tra xem chuỗi có chứa số không
        if (normalizedValue.matches(".*\\d+.*")) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Category name must not contain numbers")
                    .addConstraintViolation();
            return false;
        }

        // Kiểm tra xem chuỗi có chứa ký tự đặc biệt không (ngoài chữ và khoảng trắng giữa các từ)
        if (normalizedValue.matches(".*[^a-zA-Z\\s].*")) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Category name must contain only letters (a-z, A-Z) and spaces between words")
                    .addConstraintViolation();
            return false;
        }

        // Kiểm tra xem chuỗi có ít nhất một chữ cái không
        if (!normalizedValue.matches(".*[a-zA-Z].*")) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Category name must contain at least one letter")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}