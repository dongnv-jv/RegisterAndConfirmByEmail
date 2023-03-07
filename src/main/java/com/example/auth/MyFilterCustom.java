package com.example.auth;

import com.example.service.UserDetailServiceCustom;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;

//@Component
@Slf4j
public class MyFilterCustom implements Filter {



//    @Autowired
    private JwtUtils jwtUtils;

//    @Autowired
    private UserDetailServiceCustom userServiceCustom;


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {



        Filter.super.init(filterConfig);
    }
    private String parseJwt(HttpServletRequest request) {
        String responseHeader = request.getHeader("Authorization");
        log.warn("responseHeader: "+ responseHeader);

        if (StringUtils.hasText(responseHeader) && responseHeader.startsWith("Bearer ")) {
            return responseHeader.substring(7, responseHeader.length());

        }
        return null;
    }
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            String jwt = parseJwt((HttpServletRequest) servletRequest);
            log.warn("Jwt : "+ jwt);
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                String username = jwtUtils.getUserFromToken(jwt);

                UserDetails userDetails = userServiceCustom.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities());


                log.warn("username : "+ username);

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails((HttpServletRequest) servletRequest));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            log.error("Cannot set user authentication: {}", e);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
