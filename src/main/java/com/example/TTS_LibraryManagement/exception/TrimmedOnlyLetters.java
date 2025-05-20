package com.example.TTS_LibraryManagement.exception;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = TrimmedOnlyLettersValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface TrimmedOnlyLetters {
    String message() default "Category name must contain only letters (a-z, A-Z)";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}