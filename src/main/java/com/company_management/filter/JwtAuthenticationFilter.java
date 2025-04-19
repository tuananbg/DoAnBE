package com.company_management.filter;

import com.company_management.dto.au.EmployeeInfo;
import com.company_management.service.au.JwtService;
import com.company_management.service.au.impl.CustomUserDetailsService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {
        final String authHeader = req.getHeader("Authorization");
        final String jwt;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            chain.doFilter(req, res);
            return;
        }

        jwt = authHeader.substring(7);
        String username = "";
        try {
            username = jwtService.extractUsername(jwt);
        } catch (SignatureException e) {
            logger.warn("Token is invalid!");
        } catch (ExpiredJwtException e) {
            logger.warn("Token has been expired!");
        }

        if (StringUtils.isEmpty(username)) {
            chain.doFilter(req, res);
            return;
        }

        EmployeeInfo employee = (EmployeeInfo) userDetailsService.loadUserByUsername(username);
        if (StringUtils.isEmpty(username) || employee == null) {
            chain.doFilter(req, res);
            return;
        }
        employee.setToken(jwt);
        // Once we get the token validate it.
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                    employee, null, employee.getAuthorities());
            usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        }

        chain.doFilter(req, res);
    }

}