package com.example.TTS_LibraryManagement.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "permissions")
@Data                   // Sinh getter, setter, toString, equals, hashCode
@NoArgsConstructor      // Constructor không tham số
@AllArgsConstructor     // Constructor có tất cả tham số
@Builder                // Cho phép dùng Builder pattern để khởi tạo object
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String functionCode;
    String functionName;
    String description;
    Timestamp createdAt;
    Timestamp updatedAt;
    String createdBy;
    String updatedBy;
    int isDeleted;
    Timestamp deletedAt;
    String deletedBy;
    @ManyToMany(mappedBy = "permissions")
    private Set<Role> roles;
}
