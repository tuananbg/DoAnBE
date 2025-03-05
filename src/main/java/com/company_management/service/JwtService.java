package com.company_management.service;


import com.company_management.entity.UserAccount;
import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
     String generateToken(UserAccount user);

     boolean isTokenValid(String token, UserDetails userDetails) ;

     String extractUserName(String token);
}