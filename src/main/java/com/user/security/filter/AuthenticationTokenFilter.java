package com.user.security.filter;

import com.user.common.constants.AppConstants;
import com.user.security.service.AuthClientDetailsService;
import com.user.security.service.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class AuthenticationTokenFilter extends OncePerRequestFilter {

    private final JWTService jwtService;

    private final AuthClientDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        String tokenFromHeader = getTokenFromAuthHeader(request);
        UserDetails userDetails;
        if (StringUtils.hasText(tokenFromHeader)) {
            log.info("AuthenticationTokenFilter > doFilterInternal > Start");
            if (jwtService.validateJwtToken(tokenFromHeader)) {
                String username = jwtService.getUserNameFromJwtToken(tokenFromHeader);
                userDetails = userDetailsService.loadUserByUsername(username);
            } else {
                log.info("AuthenticationTokenFilter > doFilterInternal > JWT validation failed");
                filterChain.doFilter(request, response);
                return;
            }

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities()
            );
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("AuthenticationTokenFilter > doFilterInternal > End");
        }
        filterChain.doFilter(request, response);
    }

    private String getTokenFromAuthHeader(HttpServletRequest request) {
        String headerAuth = request.getHeader(AppConstants.AUTHORIZATION);
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith(AppConstants.BEARER)) {
            return headerAuth.substring(AppConstants.BEARER.length());
        }
        return null;
    }
}