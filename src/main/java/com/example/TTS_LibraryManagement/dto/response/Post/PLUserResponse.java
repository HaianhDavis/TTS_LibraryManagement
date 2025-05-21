package com.example.TTS_LibraryManagement.dto.response.Post;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PLUserResponse {
    String fullname;
    String phoneNumber;
    String identityNumber;
    Integer age;
    LocalDate birthday;
    String address;
}
