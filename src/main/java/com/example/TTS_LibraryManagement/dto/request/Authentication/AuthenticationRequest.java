package com.example.TTS_LibraryManagement.dto.request.Authentication;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
public class AuthenticationRequest {
    @NotBlank(message = "NOT_BLANK")
    String username;
    @NotBlank(message = "NOT_BLANK")
    String password;
}
