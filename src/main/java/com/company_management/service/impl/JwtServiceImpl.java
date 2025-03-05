package com.company_management.service.impl;

import com.company_management.config.AppConfig;
import com.company_management.dto.UserCustomDTO;
import com.company_management.entity.UserAccount;
import com.company_management.service.JwtService;
import com.company_management.utils.DateUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Service
@Log4j2
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    private final AppConfig appConfig;


    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public UserCustomDTO extractUser(String token) {
        try {
            Claims claims = extractAllClaims(token);
            String jsonUserCustomDTO = claims.get("user").toString();

            ObjectMapper mapper = new ObjectMapper();
            UserCustomDTO user = mapper.readValue(jsonUserCustomDTO, UserCustomDTO.class);

            return user;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserAccount user) {
        return Jwts.builder().setSubject(user.getEmail()).setIssuedAt(DateUtils.getNow())
                .setExpiration(new Date(System.currentTimeMillis() + appConfig.getJWTExpireTime() * 60 * 1000))
                .signWith(getKey(appConfig.getJWTSecretKey())).compact();
    }

    private Key getKey(String secret) {
        byte[] secretBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(secretBytes);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUserName(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(appConfig.getJWTSecretKey());
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String convertObjectToJson(Object object) throws JsonProcessingException {
        if (object == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }
}
