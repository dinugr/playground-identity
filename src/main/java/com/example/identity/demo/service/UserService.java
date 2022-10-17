package com.example.identity.demo.service;

import com.example.identity.demo.dto.PasswordFragment;
import com.example.identity.demo.entity.Users;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.identity.demo.dto.AuthRequest;
import com.example.identity.demo.repository.jpa.UserRepository;

@Service
public class UserService {
    private Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenService jwtTokenService;

    public String authenticate(AuthRequest authRequest) {

        String username = authRequest.getUsername();

        var user = userRepository.findByUsernameOrEmail(username, username);

        if (user == null) return null;

        var pwd = new PasswordFragment(user.getPasswordParam(), user.getPasswordSalt(), user.getPasswordDigest());
        var userIsValid = passwordEncoder.matches(authRequest.getPassword(), pwd.toString());

        if (userIsValid == false) return null;

        var token = jwtTokenService.generateToken(user);
        logger.info(token);
        return token;
    }
}
