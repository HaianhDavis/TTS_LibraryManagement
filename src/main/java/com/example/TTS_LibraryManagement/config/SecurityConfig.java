package com.example.TTS_LibraryManagement.config;


import com.example.TTS_LibraryManagement.repository.RoleRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final String[] PUBLIC_ENDPOINTS = {
            "/user/create",
            "/auth/token",
            "/auth/introspect",
            "/auth/logout",
    };
    @Autowired
    private CustomJwtDecoder customJwtDecoder;

    @Autowired
    private RoleRepo roleRepo;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(authorizeRequests ->
                authorizeRequests.requestMatchers(HttpMethod.POST, PUBLIC_ENDPOINTS).permitAll()
                        .anyRequest().authenticated());
        httpSecurity.oauth2ResourceServer(oauth2ResourceServer ->
                oauth2ResourceServer.jwt(jwtConfigurer ->
                        jwtConfigurer.decoder(customJwtDecoder).jwtAuthenticationConverter(jwtAuthenticationConverter()))
                        .authenticationEntryPoint(new JwtAuthenticationEntryPoint()));
        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        return httpSecurity.build();
    }

    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwt -> {
            // Lấy roles từ JWT token (thường ở claim "scope" hoặc "authorities")
            Collection<String> roles = jwt.getClaimAsStringList("scope");
            if (roles == null) {
                // Thử claim khác nếu không có "scope"
                roles = jwt.getClaimAsStringList("authorities");
            }

            Set<GrantedAuthority> authorities = new HashSet<>();

            if (roles != null) {
                for (String roleCode : roles) {
                    List<String> permissions = roleRepo.getAllPermissionOfRole(roleCode);
                    for (String permissionCode : permissions) {
                        authorities.add(new SimpleGrantedAuthority(permissionCode));
                    }
                }
            }
            return authorities;
        });
        return jwtAuthenticationConverter;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean("roleProperties")
    @Primary
    public Properties property() throws IOException {
        Properties properties = new Properties();
        InputStream inStream = getClass().getResourceAsStream("/role.properties");
        if (inStream == null) {
            throw new IllegalArgumentException("Tệp role.properties không được tìm thấy trong classpath!");
        }
        try {
            properties.load(inStream);
            log.info("Loaded properties: {}", properties);
        } finally {
            inStream.close();
        }
        return properties;
    }
}