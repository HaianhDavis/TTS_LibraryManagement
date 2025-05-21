package com.example.TTS_LibraryManagement.dto.response.Dashboard;

import lombok.*;
import lombok.experimental.FieldDefaults;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DashboardBookResponse {
    Long categoryId;
    String categoryName;
    int totalBooks;
}
