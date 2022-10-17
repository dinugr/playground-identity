package com.example.identity.demo.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.identity.demo.dto.AuthRequest;
import com.example.identity.demo.entity.Users;
import com.example.identity.demo.service.UserService;
import com.example.identity.demo.service.JwtTokenService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserController {
    @Autowired
    private JwtTokenService jwtTokenService;

    @Autowired
    private UserService userService;

    @PostMapping(value = "/authenticate")
    public String authenticate(@RequestBody final AuthRequest authRequest) {
//        Users userDetails = Users.builder()
//            .cifId(UUID.randomUUID())
//            .username(authRequest.getUsername())
//            .passwordDigest(authRequest.getPassword())
//            .build();

//        return jwtTokenService.generateToken(userDetails);

        return userService.authenticate(authRequest);
    }

    @GetMapping(value = "/getsomething")
    public String getsomething() {
        return "Cool!";
    }
}
