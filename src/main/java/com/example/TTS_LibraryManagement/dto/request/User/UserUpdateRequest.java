package com.example.TTS_LibraryManagement.dto.request.User;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRequest {
    @NotBlank(message = "NOT_BLANK")
    String password;

    @NotBlank(message = "NOT_BLANK")
    String fullname;

    @NotBlank(message = "NOT_BLANK")
    @Pattern(regexp = "^\\d{10}$", message = "INVALID_PHONE_NUMBER")
    String phoneNumber;

    @NotBlank(message = "NOT_BLANK")
    String identityNumber;

    @NotNull(message = "AGE_NOT_NULL")
    @Min(value = 0, message = "AGE_MUST_BE_POSITIVE")
    Integer age;

    LocalDate birthday;

    @NotBlank(message = "NOT_BLANK")
    String address;
}
