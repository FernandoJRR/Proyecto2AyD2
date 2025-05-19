package com.ayd.api_gateway.jwt.filters;

import java.util.Optional;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.ayd.api_gateway.jwt.utils.JwtManagerUtil;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements GlobalFilter {

    private final JwtManagerUtil jwtManager;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        // obtenemos la rquest
        ServerHttpRequest request = exchange.getRequest();

        String path = exchange.getRequest().getURI().getPath();

        if (path.equals("/api/v1/login")
                || path.equals("/api/v1/reservations/online")
                || path.startsWith("/api/v1/games")
                || path.equals("/api/v1/reservations-exports/reservation-ticket")) {
            return chain.filter(exchange);
        }
        try {

            // mandamos a obtener el toked del header
            Optional<String> tokenOptional = extractTokenFromHeader(request);

            // si el token no es validio entonces se devuelve vacio y debemos rechazar la
            // request
            if (tokenOptional.isEmpty()) {
                throw new Exception();
            }

            // si el token si viene en la request lo otenemos y lo mandamos a validar
            String token = tokenOptional.get();

            // validamos que el token es valido
            if (!jwtManager.isTokenValid(token)) {
                throw new Exception();
            }

            // alteramos la request para anadir el header auth-user con el nombre de usuario
            ServerHttpRequest mutatedRequest = request.mutate()
                    .header("auth-user", jwtManager.extractUsername(token))
                    .header("auth-permissions", jwtManager.extractPermissons(token))
                    .header("jwt", token)
                    .build();

            return chain.filter(exchange.mutate().request(mutatedRequest).build());

        } catch (Exception e) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

    }

    /**
     * Extrae el token JWT del encabezado `Authorization` de la solicitud.
     *
     * @param request solicitud HTTP de la que se extraerá el token.
     * @return optional que contiene el token JWT si está presente y
     *         es válido.
     */
    private Optional<String> extractTokenFromHeader(ServerHttpRequest request) {
        // extraemos el header autorizacion d ela request
        String authHeader = request.getHeaders().getFirst("Authorization");
        // si el header no esta nulo y comienza por Bearer entonces significa que trae
        // un jwt
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); // ahora eliminamos la palabra Bearer
            return Optional.of(token);
        }

        return Optional.empty();
    }

}
