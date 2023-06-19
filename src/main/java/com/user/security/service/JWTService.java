package com.user.security.service;

import com.user.security.core.ClientDetailsImpl;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Slf4j
public class JWTService {

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Value("${app.jwtExpiry}")
    private int jwtExpiry;

    public String generateJWT(final Authentication authentication) {
        log.info("JWTService > generateJWTToken > Start [authentication : {}]", authentication);
        UserDetails loggedInUser = (ClientDetailsImpl) authentication.getPrincipal();
        String jwtToken = Jwts.builder()
                .setSubject((loggedInUser.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpiry))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
        log.info("JWTService > generateJWTToken > End");
        return jwtToken;
    }

    public boolean validateJwtToken(String authToken) {
        boolean isValidToken = false;
        try {
            log.info("JWTService > validateJwtToken > Start");
            Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken).getBody();
            //validate token expiration
            if (!claims.getExpiration().before(new Date())) {
                log.info("JWTService > validateJwtToken > Success");
                isValidToken = true;
            } else {
                log.info("JWTService > validateJwtToken > Failed");
            }
        } catch (SignatureException e) {
            log.error("JWTService > validateJwtToken > Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("JWTService > validateJwtToken > Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWTService > validateJwtToken > JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWTService > validateJwtToken > JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWTService > validateJwtToken > JWT claims string is empty: {}", e.getMessage());
        }
        return isValidToken;
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}