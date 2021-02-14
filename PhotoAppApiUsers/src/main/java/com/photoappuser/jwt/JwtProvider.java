package com.photoappuser.jwt;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtProvider.class);

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Value("${app.jwtExpiration}")
    private int jwtExpiration;

    public String generateVerificationToken(String username){
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(  new Date().getTime()+ jwtExpiration * 1000 ))
                .signWith(SignatureAlgorithm.HS512 , jwtSecret)
                .compact();
    }

    public String generateToken(Authentication authentication){
//        UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        logger.info("Generate token with authentication ------> " + userDetails.getUsername());
        return Jwts.builder()
                   .setSubject(userDetails.getUsername())
                   .setIssuedAt(new Date())
                   .setExpiration(new Date(  new Date().getTime()+ jwtExpiration * 1000 ))
                   .signWith(SignatureAlgorithm.HS512 , jwtSecret)
                   .compact();
    }

    public String getUsernameFromToken(String token){
        return Jwts.parser()
                   .setSigningKey(jwtSecret)
                   .parseClaimsJws(token)
                   .getBody().getSubject();
    }

    public Boolean validateToken(String token){
        Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
        Date tokenExpirationDate = claims.getExpiration();
        Date todayDate = new Date();
        return tokenExpirationDate.before(todayDate);
    }

    public Boolean validateTokenWithAuthentication(String token){
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        }catch (SignatureException e){
            logger.error("Invalid JWT signature -> Message: {} " , e);
        }catch (MalformedJwtException e){
            logger.error("Invalid JWT signature -> Message: {} " , e);
        }catch (ExpiredJwtException e){
            logger.error("Expired JWT token -> Message: {}", e);
        }catch (UnsupportedJwtException e){
            logger.error("Unsupported JWT token -> Message: {}", e);
        }catch (IllegalArgumentException e){
            logger.error("JWT claims string is empty -> Message: {}", e);
        }
        return false;
    }
}
