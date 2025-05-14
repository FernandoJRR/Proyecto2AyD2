package com.ayd.reservation_service.reservation.adapters;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.ayd.game_service_common.games.dtos.CreateGameRequestDTO;
import com.ayd.game_service_common.games.dtos.GameResponseDTO;
import com.ayd.reservation_service.reservation.ports.ForGameClientPort;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GameClientAdapter implements ForGameClientPort {

    private final WebClient.Builder webClientBuilder;

    @Override
    public GameResponseDTO createGame(CreateGameRequestDTO createGameRequestDTO) {
        return webClientBuilder.build()
                // usamos usa method en lugar de get para permitir enviar un body en la
                // solicitud
                .method(HttpMethod.GET)
                .uri("lb://API-GATEWAY/api/v1/games/")
                .bodyValue(createGameRequestDTO)
                // ejecutmaos la solicitud y se prepara para obtener la respuesta
                .retrieve()
                // flux espera una lista
                .bodyToMono(GameResponseDTO.class).block();
    }

}
