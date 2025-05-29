package com.example.TTS_LibraryManagement.service.impl;

import com.example.TTS_LibraryManagement.dto.request.Authentication.AuthenticationRequest;
import com.example.TTS_LibraryManagement.dto.request.Authentication.IntrospectRequest;
import com.example.TTS_LibraryManagement.dto.request.Authentication.LogoutRequest;
import com.example.TTS_LibraryManagement.dto.request.Authentication.RefreshRequest;
import com.example.TTS_LibraryManagement.dto.response.AuthenticationResponse;
import com.example.TTS_LibraryManagement.dto.response.IntrospectResponse;
import com.example.TTS_LibraryManagement.entity.RefreshToken;
import com.example.TTS_LibraryManagement.entity.User;
import com.example.TTS_LibraryManagement.exception.AppException;
import com.example.TTS_LibraryManagement.exception.ErrorCode;
import com.example.TTS_LibraryManagement.repository.RefreshTokenRepo;
import com.example.TTS_LibraryManagement.repository.RoleRepo;
import com.example.TTS_LibraryManagement.repository.UserRepo;
import com.example.TTS_LibraryManagement.service.AuthenticationService;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationServiceImpl implements AuthenticationService {
    UserRepo userRepo;
    RefreshTokenRepo refreshTokenRepo;
    RoleRepo roleRepo;

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    @NonFinal
    @Value("${jwt.valid-duration}")
    protected long VALID_DURATION;

    @NonFinal
    @Value("${jwt.refreshable-duration}")
    protected long REFRESHABLE_DURATION;

    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
        var token = request.getToken();
        boolean isValid = true;
        try {
            verifyToken(token, false);
        } catch (AppException e) {
            isValid = false;
        }
        return IntrospectResponse.builder()
                .valid(isValid)
                .build();
    }

    @Transactional
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        var user = userRepo.findByUsername(request.getUsername()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        if (user.getIsDeleted() == 1) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (!authenticated) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        var accessToken = generateAccessToken(user);
        Map<String, String> refreshTokenData = generateRefreshToken(user);
        String refreshToken = refreshTokenData.get("token");
        String jwtId = refreshTokenData.get("jwtId");
        RefreshToken refreshTokenEntity = RefreshToken.builder()
                .id(jwtId)
                .expiredTime(new Timestamp(Instant.now().plus(REFRESHABLE_DURATION, ChronoUnit.SECONDS).toEpochMilli()))
                .build();
        refreshTokenRepo.save(refreshTokenEntity);
        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .authenticated(true)
                .build();
    }

    public void logout(LogoutRequest request) throws ParseException, JOSEException {
        var signTok = verifyToken(request.getToken(), true);
        String jit = signTok.getJWTClaimsSet().getJWTID();
        refreshTokenRepo.findById(jit)
                .ifPresent(refreshTokenRepo::delete);
    }

    public AuthenticationResponse refreshToken(RefreshRequest request) throws JOSEException, ParseException {
        var signedJWT = verifyToken(request.getRefreshToken(), true);
        var jit = signedJWT.getJWTClaimsSet().getJWTID();
        var refreshToken = refreshTokenRepo.findById(jit)
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));
        var username = signedJWT.getJWTClaimsSet().getSubject();
        var user = userRepo.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));
        refreshTokenRepo.delete(refreshToken);
        String newAccessToken = generateAccessToken(user);
        Map<String, String> newRefreshTokenData = generateRefreshToken(user);
        String newRefreshToken = newRefreshTokenData.get("token");
        String newJwtId = newRefreshTokenData.get("jwtId");
        RefreshToken refreshTokenEntityNew = RefreshToken.builder()
                .id(newJwtId)
                .expiredTime(new Timestamp(Instant.now().plus(REFRESHABLE_DURATION, ChronoUnit.SECONDS).toEpochMilli()))
                .build();
        refreshTokenRepo.save(refreshTokenEntityNew);

        return AuthenticationResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .authenticated(true)
                .build();
    }

    private SignedJWT verifyToken(String token, boolean isRefresh) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);
        Date expiration = signedJWT.getJWTClaimsSet().getExpirationTime();
        var verified = signedJWT.verify(verifier);
        if (!(verified && expiration.after(new Date()))) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        if (isRefresh) {
            String jwtId = signedJWT.getJWTClaimsSet().getJWTID();
            var refreshToken = refreshTokenRepo.findById(jwtId)
                    .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));
            if (refreshToken.getExpiredTime().before(new Date())) {
                throw new AppException(ErrorCode.UNAUTHENTICATED);
            }
        }

        return signedJWT;
    }

    String generateAccessToken(User user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("TTS_LibraryManagement")
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(VALID_DURATION, ChronoUnit.SECONDS).toEpochMilli()))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", buildScope(user))
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);
        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("cannot create token", e);
            throw new RuntimeException(e);
        }
    }

    private Map<String, String> generateRefreshToken(User user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        String jwtId = UUID.randomUUID().toString();
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("TTS_LibraryManagement")
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(REFRESHABLE_DURATION, ChronoUnit.SECONDS).toEpochMilli()))
                .jwtID(jwtId)
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            Map<String, String> result = new HashMap<>();
            result.put("token", jwsObject.serialize());
            result.put("jwtId", jwtId);
            return result;
        } catch (JOSEException e) {
            log.error("Cannot create refresh token", e);
            throw new RuntimeException(e);
        }
    }

    List<String> buildScope(User user) {
        return roleRepo.getAllRoleOfUser(user.getId());
    }

    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void cleanupExpiredTokens() {
        log.info("Starting cleanup of expired refresh tokens");
        refreshTokenRepo.deleteByExpiredTimeBefore(new Timestamp(System.currentTimeMillis()));
        log.info("Cleanup completed");
    }
}
