package com.example.TTS_LibraryManagement.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ValidNumberValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidNumber {
    String message() default "Page count validation failed"; // Không còn được sử dụng, nhưng giữ cho rõ ràng
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}