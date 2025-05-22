package com.example.TTS_LibraryManagement.config;

import com.example.TTS_LibraryManagement.entity.Role;
import com.example.TTS_LibraryManagement.entity.User;
import com.example.TTS_LibraryManagement.repository.RoleRepo;
import com.example.TTS_LibraryManagement.repository.UserRepo;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ApplicationInitConfig {

    PasswordEncoder passwordEncoder;

    @Bean
    @Transactional
    ApplicationRunner applicationRunner(UserRepo userRepo, RoleRepo roleRepo) {
        return args -> {
            roleRepo.findByRoleGroupCodeAndIsDeletedFalse("USER")
                    .orElseGet(() -> {
                        Role newRole = Role.builder()
                                .roleGroupCode("USER")
                                .roleGroupName("Người dùng")
                                .description("Vai trò người dùng thông thường")
                                .isDeleted(0)
                                .build();
                        return roleRepo.save(newRole);
                    });

            Role adminRole = roleRepo.findByRoleGroupCodeAndIsDeletedFalse("ADMIN")
                    .orElseGet(() -> {
                        Role newRole = Role.builder()
                                .roleGroupCode("ADMIN")
                                .roleGroupName("Quản trị viên")
                                .description("Vai trò quản trị viên")
                                .isDeleted(0)
                                .build();
                        return roleRepo.save(newRole);
                    });

            User user = userRepo.findByUsername("admin")
                    .orElseGet(() -> {
                        User admin = User.builder()
                                .username("admin")
                                .password(passwordEncoder.encode("admin"))
                                .isDeleted(0)
                                .build();
                        return userRepo.save(admin);
                    });
            Set<User> users = new HashSet<>();
            users.add(user);
            adminRole.setUsers(users);
            roleRepo.save(adminRole);
            log.warn("Admin account created with username: admin and password: admin");
        };
    }
}