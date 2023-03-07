package com.example.controller;

import com.example.auth.JwtService;
import com.example.entity.User;
import com.example.factory.RequestRegister;
import com.example.service.ConfirmMailService;
import com.example.service.UserDetailServiceCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/auth")
public class UserController {
    @Autowired
    private UserDetailServiceCustom detailServiceCustom;
    @Autowired
    JwtService jwtService;


    @Autowired
    ConfirmMailService confirmMailService;
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RequestRegister request) {
        return ResponseEntity.ok(jwtService.register(request));
    }
    @GetMapping("/confirm/{token}")
    public String confirmUser(@PathVariable("token") String token) {
        return detailServiceCustom.confirmToken(token);
    }
    @GetMapping("/sucess")
    public String loginsucess() {
        return "Login sucess";
    }

    @PostMapping("/signin")
    public ResponseEntity<?> singin(@RequestBody RequestRegister request) {
        return ResponseEntity.ok(jwtService.authenticate(request));
    }



}
