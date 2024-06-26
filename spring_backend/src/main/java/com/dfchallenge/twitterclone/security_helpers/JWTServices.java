package com.dfchallenge.twitterclone.security_helpers;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Service
public class JWTServices {

    @Value("${jwt.secret}")
    private String secret;


    private Key getSignInKey(){
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Claims extractAllClaims(String token){

        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims;
    }

    public Integer extractAccountId(String token){
        Claims claims = extractAllClaims(token);
        String accountId = claims.get("sub", String.class);
        return Integer.parseInt(accountId);
    }

    private Date extractExpiration(String token){
        Claims claims = extractAllClaims(token);
        return claims.getExpiration();
    }

    public String generateToken(Integer accountId){
        return generateToken(new HashMap<>(), accountId);
    }

    public String generateToken(Map<String, Object> extraClaims, Integer accountId){
        final int expirationTime = 1000 * 60 * 60 * 24 * 10;
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(Integer.toString(accountId))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();

    }

    public boolean isTokenValid(String token, Integer accountId){
        System.out.println("========== JWT ==========");
        System.out.println(token);
        System.out.println("=========================");
        final Integer idFromToken = extractAccountId(token);
        return (accountId.equals(idFromToken) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }




}
