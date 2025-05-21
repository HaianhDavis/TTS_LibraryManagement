package com.example.TTS_LibraryManagement.service;

import com.example.TTS_LibraryManagement.dto.request.AuthenticationRequest;
import com.example.TTS_LibraryManagement.dto.request.IntrospectRequest;
import com.example.TTS_LibraryManagement.dto.response.AuthenticationResponse;
import com.example.TTS_LibraryManagement.dto.response.IntrospectResponse;
import com.nimbusds.jose.JOSEException;

import java.text.ParseException;

public interface AuthenticationService {
    AuthenticationResponse authenticate(AuthenticationRequest request);
    IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException;
}
