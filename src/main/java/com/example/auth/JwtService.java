package com.example.auth;

import com.example.entity.User;
import com.example.factory.AuthResponse;
import com.example.factory.RequestRegister;
import com.example.service.UserDetailCustom;
import com.example.service.UserDetailServiceCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    @Autowired
    private UserDetailServiceCustom detailServiceCustom;


    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    AuthenticationManager authenticationManager;


    public AuthResponse register(RequestRegister request) {

        User user = new User(request.getUsername(), request.getPassword(), request.getEmail());
        String tokenRegister = detailServiceCustom.registerUser(user);

//        String token=jwtUtils.generateJwt(request.getUsername());

        return new AuthResponse(request,tokenRegister);


    }
    public AuthResponse authenticate(RequestRegister request) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getEmail(), request.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailCustom userPrincipal = (UserDetailCustom) authentication.getPrincipal();
        String token = jwtUtils.generateJwt(userPrincipal.getUsername());


        return new AuthResponse(request,token);
    }

}
