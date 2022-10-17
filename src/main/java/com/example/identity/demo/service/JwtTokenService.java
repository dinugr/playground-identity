package com.example.identity.demo.service;


import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.identity.demo.entity.Users;

@Service
public class JwtTokenService {

    private Logger logger = LoggerFactory.getLogger(JwtTokenService.class);

    private final Algorithm hmac512;
    private final JWTVerifier verifier;

    private final String duration;

    public JwtTokenService(@Value("${app.jwt.secret}") final String secret, @Value("${app.jwt.duration}") final String duration) {
        this.hmac512 = Algorithm.HMAC512(secret);
        this.verifier = JWT.require(this.hmac512).build();
        this.duration = duration;
    }

    public String generateToken(final Users userDetails, String duration_) {
        ZoneId defaultZoneId = ZoneId.systemDefault();

        var d = Duration.parse(duration_);
        var dt = LocalDateTime.now().plusSeconds(d.toSeconds());

        var date = Date.from(dt.atZone(defaultZoneId).toInstant());
        
        return JWT.create()
                .withSubject(userDetails.getUsername())
                .withExpiresAt(date)
                .sign(this.hmac512);
    }

    public String generateToken(final Users userDetails) {
        return generateToken(userDetails, duration);
    }

    public String validateTokenAndGetUsername(final String token) {
        try {
            return verifier.verify(token).getSubject();
        } catch (final JWTVerificationException verificationEx) {
            logger.warn("token invalid: {}", verificationEx.getMessage());
            return null;
        }
    }
}