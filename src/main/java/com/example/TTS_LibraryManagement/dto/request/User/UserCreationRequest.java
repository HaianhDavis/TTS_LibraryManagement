package com.example.TTS_LibraryManagement.dto.request.User;

import com.example.TTS_LibraryManagement.validator.DobConstraint;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {
    @NotBlank(message = "NOT_BLANK")
    String username;

    @NotBlank(message = "NOT_BLANK")
    String password;

    @NotBlank(message = "NOT_BLANK")
    String fullname;

    @NotBlank(message = "NOT_BLANK")
    @Pattern(regexp = "^\\d{9}$", message = "INVALID_PHONE_NUMBER")
    String phoneNumber;

    @NotBlank(message = "NOT_BLANK")
    String identityNumber;

    @NotNull(message = "AGE_NOT_NULL")
    @Min(value = 0, message = "AGE_MUST_BE_POSITIVE")
    Integer age;

    @DobConstraint(min = 12)
    LocalDate birthday;

    @NotBlank(message = "NOT_BLANK")
    String address;
}