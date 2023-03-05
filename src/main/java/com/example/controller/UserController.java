package com.example.controller;

import com.example.entity.User;
import com.example.factory.RequestRegister;
import com.example.service.ConfirmMailService;
import com.example.service.UserDetailServiceCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {
    @Autowired
    private UserDetailServiceCustom detailServiceCustom;
    @Autowired
    ConfirmMailService confirmMailService;
    @PostMapping("/register")
    public String register(@RequestBody RequestRegister request) {

        User user = new User(request.getUsername(), request.getPassword(), request.getEmail());
        String token = detailServiceCustom.registerUser(user);


        return token;
    }
    @GetMapping("/confirm/{token}")
    public String confirmUser(@PathVariable("token") String token) {


        return detailServiceCustom.confirmToken(token);
    }
    @GetMapping("/sucess")
    public String loginsucess() {


        return "Login sucess";
    }
}
