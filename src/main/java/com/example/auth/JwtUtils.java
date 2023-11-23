package com.example.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.function.Function;

@Slf4j
@Component
public class JwtUtils {


    private final String JWT_SECRET = "dhfksjdhsjdhfkjshgjksdhgksdgfhsdfghdfjfgjfgjhdfgjfhkfgjdgjfkgjlkfhjdfhg" +
            "dfhfgkhfghdfhdfjkfgjdfghdfhfgkfgjfghfgfhfjfgjdfgdfhdfhfgjghkghkghjghkjghtyurtyeryturth";
    private int jwtExp = 600000;

    public String generateJwt(String username) {
        return Jwts.builder().setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration((new Date((new Date()).getTime() + jwtExp)))
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
                .compact();
    }

    public String getUserFromToken(String token) {

        return getClaimsFromToken(token, Claims -> Claims.getSubject());
    }

    private <T> T getClaimsFromToken(String token, Function<Claims, T> function) {
        Claims claims = Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token).getBody();

        return function.apply(claims);
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(authToken);
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
