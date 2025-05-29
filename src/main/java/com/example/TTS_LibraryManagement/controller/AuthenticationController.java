package com.example.TTS_LibraryManagement.controller;

import com.example.TTS_LibraryManagement.dto.request.Authentication.AuthenticationRequest;
import com.example.TTS_LibraryManagement.dto.request.Authentication.IntrospectRequest;
import com.example.TTS_LibraryManagement.dto.request.Authentication.LogoutRequest;
import com.example.TTS_LibraryManagement.dto.request.Authentication.RefreshRequest;
import com.example.TTS_LibraryManagement.dto.response.ApiResponse;
import com.example.TTS_LibraryManagement.dto.response.AuthenticationResponse;
import com.example.TTS_LibraryManagement.dto.response.IntrospectResponse;
import com.example.TTS_LibraryManagement.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Authentication", description = "Authentication Management APIs")
public class AuthenticationController {
    AuthenticationService authenticationService;

    @Operation(summary = "Authenticate user and generate token")
    @PostMapping("/token")
    ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        ApiResponse<AuthenticationResponse> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Successfully authenticated");
        apiResponse.setResult(authenticationService.authenticate(request));
        return apiResponse;
    }

    @Operation(summary = "Introspect token to get user details")
    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest request) throws ParseException, JOSEException {
        ApiResponse<IntrospectResponse> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Successfully introspected");
        apiResponse.setResult(authenticationService.introspect(request));
        return apiResponse;
    }

    @Operation(summary = "Logout user and invalidate token")
    @PostMapping("/logout")
    ApiResponse<Void> logout(@RequestBody LogoutRequest request) throws ParseException, JOSEException {
        ApiResponse<Void> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Successfully logged out");
        authenticationService.logout(request);
        return apiResponse;
    }

    @Operation(summary = "Refresh token to get new access token")
    @PostMapping("/refresh")
    ApiResponse<AuthenticationResponse> refresh(@RequestBody RefreshRequest request) throws ParseException, JOSEException {
        ApiResponse<AuthenticationResponse> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Successfully refreshed token");
        apiResponse.setResult(authenticationService.refreshToken(request));
        return apiResponse;
    }
}
