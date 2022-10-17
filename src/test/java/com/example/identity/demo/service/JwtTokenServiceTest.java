package com.example.identity.demo.service;

import java.time.format.DateTimeParseException;
import java.util.UUID;

import com.example.identity.demo.dto.PasswordFragment;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.example.identity.demo.entity.Users;

@SpringBootTest
    @ContextConfiguration(classes = {JwtTokenService.class, Argon2PasswordEncoder.class})
    @ExtendWith(SpringExtension.class)
    public class JwtTokenServiceTest {

    @Autowired
    private JwtTokenService jwtTokenService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    Users user = new Users();

    @BeforeEach
    void before() {
        user = Users.builder()
            .id(UUID.randomUUID())
            .username("kotiya")
            .build();
    }

    @Test
    void should_generate() {
        String token = jwtTokenService.generateToken(user);
        Assertions.assertNotNull(token);
    }

    @Test
    void should_generate_with_custom_duration() {
        String token = jwtTokenService.generateToken(user, "PT1S");
        Assertions.assertNotNull(token);
    }

    @Test
    void should_throw_DateTimeParseException() {
        Assertions.assertThrows(DateTimeParseException.class, () ->  jwtTokenService.generateToken(user, "HelloWorld"));
    }

    @Test
    void should_throw_DateTimeParseException_null() {
        Assertions.assertThrows(NullPointerException.class, () ->  jwtTokenService.generateToken(user, null));
    }

    @Test
	void should_validate() {
        String token = jwtTokenService.generateToken(user, "PT5S"); // 5 second
        String username = jwtTokenService.validateTokenAndGetUsername(token);

        Assertions.assertNotNull(username);
	}

    @Test
    void should_expired() throws Exception {
        String token = jwtTokenService.generateToken(user, "PT1S"); // 1 second
        Thread.sleep(2000); // 2 second
        var username = jwtTokenService.validateTokenAndGetUsername(token);

        Assertions.assertNull(username);
    }
}
