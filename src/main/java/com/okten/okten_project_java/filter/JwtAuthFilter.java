package com.okten.okten_project_java.filter;

import com.okten.okten_project_java.services.UserService;
import com.okten.okten_project_java.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;


@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private static final String AUTHORIZATION_HEADER_PREFIX = "Bearer ";

    private final JwtUtil jwtUtil;

    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeaderValue = request.getHeader(AUTHORIZATION);

        if (StringUtils.isBlank(authorizationHeaderValue) || !StringUtils.startsWith(authorizationHeaderValue, AUTHORIZATION_HEADER_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwtValue = authorizationHeaderValue.substring(AUTHORIZATION_HEADER_PREFIX.length());

        if (jwtUtil.isTokenExpired(jwtValue)) {
            filterChain.doFilter(request, response);
            return;
        }

        String username = jwtUtil.extractUsername(jwtValue);

        if (!StringUtils.isBlank(username)) {
            UserDetails userDetails = userService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authentication = UsernamePasswordAuthenticationToken.authenticated(username, userDetails.getPassword(), userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }
}
