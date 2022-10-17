package com.example.identity.demo.service;


import com.example.identity.demo.dto.PasswordFragment;
import com.example.identity.demo.repository.jpa.UserRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.example.identity.demo.dto.AuthRequest;
import com.example.identity.demo.entity.Users;

import java.util.*;

@SpringBootTest
@ContextConfiguration(classes = {UserService.class, Argon2PasswordEncoder.class, JwtTokenService.class})
@ExtendWith(SpringExtension.class)
public class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenService jwtTokenService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @MockBean
    private UserRepository userRepository;


    private AuthRequest authRequest;
    private List<Users> users = new ArrayList<Users>();

    @BeforeEach
    void setUp() {
        authRequest = new AuthRequest();
        authRequest.setUsername("maika");
        authRequest.setPassword("flowerflag");

        var password = passwordEncoder.encode(authRequest.getPassword());
        var passwordFragment = PasswordFragment.parseArgon2id(password);

        var user1 = Users.builder()
                .id(UUID.randomUUID())
                .cifId(UUID.randomUUID())
                .username(authRequest.getUsername())
                .email(authRequest.getUsername() + "@example.net")
                .passwordParam(passwordFragment.getParam())
                .passwordDigest(passwordFragment.getDigest())
                .passwordSalt(passwordFragment.getSalt())
                .build();
        users.add(user1);

        Mockito.when(userRepository.findByUsernameOrEmail(user1.getUsername(), user1.getUsername()))
                .thenReturn(user1);
    }

    @Test
    void should_parse_password_fragment() {
        var password = passwordEncoder.encode("maika");
        var passwordFragment = PasswordFragment.parseArgon2id(password);

        var digest = passwordFragment.getDigest();
        var salt = passwordFragment.getSalt();
        var param = passwordFragment.getParam();

        Assertions.assertNotNull(digest);
        Assertions.assertNotNull(salt);
        Assertions.assertNotNull(param);
    }

    @Test
    void should_authenticate_and_login() {
        var jwt = userService.authenticate(authRequest);

        Assertions.assertNotNull(jwt);

        var valid = jwtTokenService.validateTokenAndGetUsername(jwt);

        Assertions.assertEquals(valid, authRequest.getUsername());
    }

    @Test
    void should_no_throw_encode_password() {
        Assertions.assertDoesNotThrow(() -> passwordEncoder.encode("Hello World"));
    }

    @Test
    void should_encode_password() {
        var result = passwordEncoder.encode("Hello World");
        Assertions.assertNotNull(result);
    }
}
