
package com.example.rental_platform.security;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    // =========================
    // LOGGER
    // =========================
    private static final Logger logger =
            LoggerFactory.getLogger(JwtAuthFilter.class);

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        // Debug - Check whether filter is executing
        logger.info("===== JWT FILTER EXECUTED ===== {}",
                request.getRequestURI());

        // Read Authorization Header
        String authHeader = request.getHeader("Authorization");

        // Print Authorization Header
        logger.debug("Authorization Header: {}", authHeader);

        // Check whether Authorization header exists
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Remove "Bearer " from Authorization Header
        String token = authHeader.substring(7);

        logger.debug("JWT Token: {}", token);

        // Extract Email From JWT
        String email = jwtUtil.extractEmail(token);

        logger.debug("Authenticated Email: {}", email);

        // Load User From Database
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        logger.debug("User loaded: {}", userDetails.getUsername());

        // Validate Token
        if (jwtUtil.validateToken(token, userDetails.getUsername())) {

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities());

            authentication.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            logger.info("User authenticated successfully.");
        } else {
            logger.warn("Invalid JWT token received.");
        }

        // Continue Request
        filterChain.doFilter(request, response);
    }
}