package com.example.TTS_LibraryManagement.dto.request.Authentication;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
public class IntrospectRequest {
    String token;
}
