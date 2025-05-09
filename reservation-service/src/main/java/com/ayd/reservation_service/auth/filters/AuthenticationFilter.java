package com.ayd.reservation_service.auth.filters;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class AuthenticationFilter extends OncePerRequestFilter {

    /**
     * Método principal que intercepta cada solicitud HTTP y ejecuta la
     * validación del token JWT.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {
 
        String username = request.getHeader("auth-user");
        String rolesHeader = request.getHeader("auth-permissions");

        if (username != null && rolesHeader != null && !rolesHeader.isEmpty()) {
            List<GrantedAuthority> authorities = Stream.of(rolesHeader.split(","))
                    .map(permisson -> permisson.trim())
                    .map(permisson -> new SimpleGrantedAuthority(permisson))
                    .collect(Collectors.toList());

            Authentication auth = new UsernamePasswordAuthenticationToken(username, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        filterChain.doFilter(request, response);
    }

}
