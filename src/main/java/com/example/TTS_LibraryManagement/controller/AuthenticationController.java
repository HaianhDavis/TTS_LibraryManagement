package com.example.TTS_LibraryManagement.controller;

import com.example.TTS_LibraryManagement.dto.request.AuthenticationRequest;
import com.example.TTS_LibraryManagement.dto.request.IntrospectRequest;
import com.example.TTS_LibraryManagement.dto.request.LogoutRequest;
import com.example.TTS_LibraryManagement.dto.response.ApiResponse;
import com.example.TTS_LibraryManagement.dto.response.AuthenticationResponse;
import com.example.TTS_LibraryManagement.dto.response.IntrospectResponse;
import com.example.TTS_LibraryManagement.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
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
public class AuthenticationController {
    AuthenticationService authenticationService;

    @PostMapping("/token")
    ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        ApiResponse<AuthenticationResponse> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Successfully authenticated");
        apiResponse.setResult(authenticationService.authenticate(request));
        return apiResponse;
    }

    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest request) throws ParseException, JOSEException {
        ApiResponse<IntrospectResponse> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Successfully introspected");
        apiResponse.setResult(authenticationService.introspect(request));
        return apiResponse;
    }

    @PostMapping("/logout")
    ApiResponse<Void> logout(@RequestBody LogoutRequest request) throws ParseException, JOSEException {
        ApiResponse<Void> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Successfully logged out");
        authenticationService.logout(request);
        return apiResponse;
    }
}
