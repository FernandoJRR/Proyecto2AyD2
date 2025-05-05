package com.ayd.employee_service.auth.jwt.filters;

import java.io.IOException;
import java.util.Optional;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ayd.employee_service.auth.jwt.utils.JwtManagerUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtManagerUtil jwtManager;
    private final UserDetailsService userDetailsService;

    /**
     * Método principal que intercepta cada solicitud HTTP y ejecuta la
     * validación del token JWT.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        Optional<String> tokenOptional = extractTokenFromHeader(request);

        if (tokenOptional.isPresent()) {
            String token = tokenOptional.get();
            Optional<UserDetails> userDetailsOptional = validateToken(token);

            if (userDetailsOptional.isPresent()) {
                authenticateUser(userDetailsOptional.get(), token, request);
            }
        }
        filterChain.doFilter(request, response);
    }

    /**
     * Extrae el token JWT del encabezado `Authorization` de la solicitud.
     *
     * @param request solicitud HTTP de la que se extraerá el token.
     * @return optional que contiene el token JWT si está presente y
     *         es válido.
     */
    private Optional<String> extractTokenFromHeader(HttpServletRequest request) {
        // extraemos el header autorizacion d ela request
        String authHeader = request.getHeader("Authorization");
        // si el header no esta nulo y comienza por Bearer entonces significa que trae
        // un jwt
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); // ahora eliminamos la palabra Bearer
            return Optional.of(token);
        }

        return Optional.empty();
    }

    /**
     * Valida el token JWT y devuelve los detalles del usuario si el token es
     * válido.
     *
     * @param jwt El token JWT a validar.
     * @return Optional con los detalles del usuario si el token es
     *         válido.
     */
    private Optional<UserDetails> validateToken(String jwt) throws UsernameNotFoundException {
        String username = jwtManager.extractUsername(jwt);

        // cargar al usuario
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        // si esta invalido entonces esto lanzara excepcion
        jwtManager.isTokenValid(jwt, userDetails.getUsername());

        return Optional.of(userDetails);

    }

    /**
     * Carga al usuario y lo establece el contexto de seguridad de Spring.
     *
     * @param userDetails detalles del usuario autenticado.
     * @param token       el token JWT usado para la autenticación.
     * @param request     la solicitud HTTP actual.
     */
    private void authenticateUser(UserDetails userDetails, String token, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                userDetails, token, userDetails.getAuthorities());
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }
}
