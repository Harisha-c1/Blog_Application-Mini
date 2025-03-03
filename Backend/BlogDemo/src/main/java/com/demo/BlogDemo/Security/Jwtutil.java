package com.demo.BlogDemo.Security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class Jwtutil {

    private final String SECRET_KEY = "mysecretkeymysecretkeymysecretkeymysecretkey";
    public SecretKey Getkey(){
        return  Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    // Generate JWT Token
    public String generateToken(String username) {
//        SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
        SecretKey key= Getkey();
        Map<String,Object> claims=new HashMap<>();
//        return Jwts.builder()
//                .setSubject(username)
//                .setIssuedAt(new Date())
//                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hour expiry
//                .signWith(key, SignatureAlgorithm.HS256)
//                .compact();

        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+1000*60*60))
                .and()
                .signWith(key)
                .compact();
    }








    // Extract Username from JWT Token
    public String extractUsername(String token) {
        Claims claims = extractAllClaims(token);
        return claims.getSubject();
    }

    // Validate JWT Token
    public boolean validateToken(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    // Check if Token is Expired
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Extract Expiration Date
    private Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }

    // Extract All Claims
    private Claims extractAllClaims(String token) {
//        Key Securitykey = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
        return Jwts.parser()
                .verifyWith(Getkey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
