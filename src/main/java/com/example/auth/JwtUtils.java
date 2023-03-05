package com.example.auth;

import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.function.Function;
@Slf4j
public class JwtUtils {


    private String jwtSecret;
    private int jwtExp = 600000;

    public String generateJwt(String username) {

        return Jwts.builder().setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration((new Date((new Date()).getTime() + jwtExp)))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String getUserFromToken(String token) {

        return getClaimsFromToken(token, Claims -> Claims.getSubject());
    }

    private <T> T getClaimsFromToken(String token, Function<Claims, T> function) {
        Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();

        return function.apply(claims);
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }
}
