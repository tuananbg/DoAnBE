package com.company_management.service.au.impl;

import com.company_management.config.AppConfig;
import com.company_management.service.au.JwtService;
import com.company_management.utils.DateUtils;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
@Log4j2
@RequiredArgsConstructor
public class JWTServiceImpl implements JwtService {

    private final AppConfig appConfig;

    @Override
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder().setSubject(userDetails.getUsername()).setIssuedAt(DateUtils.getNow())
                .setExpiration(new Date(System.currentTimeMillis() + appConfig.getJWTExpireTime() * 60 * 1000))
                .signWith(getKey(appConfig.getJWTSecretKey())).compact();
    }

    @Override
    public String extractUsername(String token) {
        return Jwts.parserBuilder().setSigningKey(getKey(appConfig.getJWTSecretKey())).build().parseClaimsJws(token)
                .getBody().getSubject();
    }

    private Key getKey(String secret) {
        byte[] secretBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(secretBytes);
    }
}
