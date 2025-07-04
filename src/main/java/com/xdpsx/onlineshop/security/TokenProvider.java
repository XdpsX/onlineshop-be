package com.xdpsx.onlineshop.security;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import com.nimbusds.jose.crypto.MACVerifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TokenProvider {
    @Value("${app.jwt.access.secret}")
    private String ACCESS_TOKEN_SECRET_KEY;

    @Value("${app.jwt.refresh.secret}")
    private String REFRESH_TOKEN_SECRET_KEY;

    @Value("${app.jwt.access.expiration-sec}")
    private Long ACCESS_TOKEN_EXPIRATION;

    @Value("${app.jwt.refresh.expiration-sec}")
    private Long REFRESH_TOKEN_EXPIRATION;

    public String generateAccessToken(CustomUserDetails user) {
        return generateToken(user, ACCESS_TOKEN_EXPIRATION, UUID.randomUUID().toString(), ACCESS_TOKEN_SECRET_KEY);
    }

    public String generateRefreshToken(CustomUserDetails user) {
        return generateToken(user, REFRESH_TOKEN_EXPIRATION, UUID.randomUUID().toString(), REFRESH_TOKEN_SECRET_KEY);
    }

    private String generateToken(CustomUserDetails user, long expiration, String jti, String secretKey) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("xdpsx.com")
                .issueTime(new Date())
                //                .expirationTime(new Date(Instant.now().plus(expiration,
                // ChronoUnit.SECONDS).toEpochMilli()))
                .expirationTime(Date.from(Instant.now().plusSeconds(expiration)))
                .claim(
                        "scope",
                        user.getAuthorities().stream()
                                .map(GrantedAuthority::getAuthority)
                                .toList())
                .jwtID(jti)
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);
        try {
            jwsObject.sign(new MACSigner(secretKey.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Cannot create token", e);
            throw new RuntimeException(e);
        }
    }

    public String extractJti(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            return signedJWT.getJWTClaimsSet().getJWTID();
        } catch (Exception e) {
            log.error("Cannot extract JTI from token", e);
            throw new IllegalArgumentException("Invalid token", e);
        }
    }

    public long getRemainingTTL(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            Instant expiration = signedJWT.getJWTClaimsSet().getExpirationTime().toInstant();
            Instant now = Instant.now();
            long seconds = expiration.getEpochSecond() - now.getEpochSecond();
            return Math.max(seconds, 0);
        } catch (Exception e) {
            log.error("Cannot get remaining TTL from token", e);
            throw new IllegalArgumentException("Invalid token", e);
        }
    }

    public SignedJWT verifyRefreshToken(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);

            JWSVerifier verifier = new MACVerifier(REFRESH_TOKEN_SECRET_KEY.getBytes());

            if (!signedJWT.verify(verifier)) {
                log.warn("Invalid signature for refresh token");
                throw new JOSEException("Invalid token signature");
            }

            Date expiration = signedJWT.getJWTClaimsSet().getExpirationTime();
            if (expiration == null || expiration.before(new Date())) {
                log.warn("Refresh token expired");
                throw new JOSEException("Token has expired");
            }

            return signedJWT;

        } catch (Exception e) {
            log.error("Failed to verify refresh token", e);
            throw new IllegalArgumentException("Invalid refresh token", e);
        }
    }

}
