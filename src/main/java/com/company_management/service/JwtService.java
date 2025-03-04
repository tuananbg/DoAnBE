package com.company_management.service;

import com.company_management.entity.Employee;
import com.company_management.entity.UserAccount;
import com.company_management.exception.AppException;
import com.company_management.exception.BadRequestException;
import com.company_management.dto.UserCustomDTO;
import com.company_management.repository.EmployeeRepository;
import com.company_management.repository.UserAccountRepository;
import com.company_management.repository.EmployeeInfoRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@Slf4j
@RequiredArgsConstructor
public class JwtService {
    private final UserAccountRepository userCustomRepository;
    private final EmployeeRepository employeeRepository;
    private static final String SERCRET_KEY = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";

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
        Map<String, Object> claims = new HashMap<>();
        String jsonUserCustom = "";

        try {
            UserAccount userCustom =
                    userCustomRepository.findByEmail(user.getEmail()).orElseThrow(() -> new BadRequestException("Không tìm thấy User"));
            Employee employee = employeeRepository.findById(userCustom.getEmployee().getId()).orElseThrow(
                    () -> new AppException("ERR01", "Không tìm thấy nhân viên!")
            );
            UserCustomDTO userCustomDTO = new UserCustomDTO();
            userCustomDTO.setId(userCustom.getId());
            userCustomDTO.setUserDetailId(userCustom.getId());
            userCustomDTO.setUserDetailName(employee.getFullName());
            userCustomDTO.setUserName(userCustom.getUsername());
            userCustomDTO.setEmail(userCustom.getEmail());
            jsonUserCustom = convertObjectToJson(userCustomDTO);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }

        claims.put("user", jsonUserCustom);
        return generateToken(claims, user);
    }

    public String generateToken(Map<String, Object> extraClaims, UserAccount user) {
        return Jwts.builder()
                .addClaims(extraClaims)
                .setSubject(user.getEmail())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
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
        byte[] keyBytes = Decoders.BASE64.decode(SERCRET_KEY);
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