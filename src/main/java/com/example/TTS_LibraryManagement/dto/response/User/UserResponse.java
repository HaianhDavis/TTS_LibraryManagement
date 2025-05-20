package com.example.TTS_LibraryManagement.dto.response.User;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;
import java.time.LocalDate;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    Long id;
    String username;
    String password;
    String fullname;
    String phoneNumber;
    String identityNumber;
    Integer age;
    LocalDate birthday;
    String address;
    Timestamp createdAt;
    Timestamp updatedAt;
    String createdBy;
    String updatedBy;
    Timestamp deletedAt;
    String deletedBy;
}
