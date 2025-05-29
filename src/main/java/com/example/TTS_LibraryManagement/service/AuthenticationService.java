package com.example.TTS_LibraryManagement.service;

import com.example.TTS_LibraryManagement.dto.request.Authentication.AuthenticationRequest;
import com.example.TTS_LibraryManagement.dto.request.Authentication.IntrospectRequest;
import com.example.TTS_LibraryManagement.dto.request.Authentication.LogoutRequest;
import com.example.TTS_LibraryManagement.dto.request.Authentication.RefreshRequest;
import com.example.TTS_LibraryManagement.dto.response.AuthenticationResponse;
import com.example.TTS_LibraryManagement.dto.response.IntrospectResponse;
import com.nimbusds.jose.JOSEException;

import java.text.ParseException;

public interface AuthenticationService {
    AuthenticationResponse authenticate(AuthenticationRequest request);
    IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException;
    void logout(LogoutRequest request) throws ParseException, JOSEException;
    AuthenticationResponse refreshToken(RefreshRequest request) throws JOSEException, ParseException;
}
