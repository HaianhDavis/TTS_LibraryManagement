package com.example.TTS_LibraryManagement.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidNumberValidator implements ConstraintValidator<ValidNumber, String> {

    @Override
    public void initialize(ValidNumber constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        if (value == null) {
            context.buildConstraintViolationWithTemplate("Page count must not be null").addConstraintViolation();
            return false; // @NotBlank sẽ xử lý trường hợp này
        }
        try {
            int number = Integer.parseInt(value);
            if (number <= 0) {
                // Số không dương
                context.buildConstraintViolationWithTemplate("Page count must be a positive number")
                        .addConstraintViolation();
                return false;
            }
            return true;  // Đảm bảo số dương
        } catch (NumberFormatException e) {
            context.buildConstraintViolationWithTemplate("Page count must be a valid number, not a string")
                    .addConstraintViolation();
            return false;
        }
    }
}
