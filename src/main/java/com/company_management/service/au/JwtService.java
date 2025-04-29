package com.company_management.service.au;

import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
     String generateToken(UserDetails userDetails);

     String extractUsername(String token);
}