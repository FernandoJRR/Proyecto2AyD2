package com.ayd.reservation_service.reservation.adapters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.*;

import com.ayd.game_service_common.games.dtos.CreateGameRequestDTO;
import com.ayd.game_service_common.games.dtos.GameResponseDTO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

class GameClientAdapterTest {

    private WebClient.Builder webClientBuilder;
    private WebClient webClient;
    private WebClient.RequestBodyUriSpec requestBodyUriSpec;
    private WebClient.RequestBodySpec requestBodySpec;
    @SuppressWarnings("rawtypes")
    private WebClient.RequestHeadersSpec requestHeadersSpec;
    private WebClient.ResponseSpec responseSpec;

    private GameClientAdapter gameClientAdapter;

    @BeforeEach
    void setUp() {
        webClientBuilder = mock(WebClient.Builder.class);
        webClient = mock(WebClient.class);
        requestBodyUriSpec = mock(WebClient.RequestBodyUriSpec.class);
        requestBodySpec = mock(WebClient.RequestBodySpec.class);
        requestHeadersSpec = mock(WebClient.RequestHeadersSpec.class);
        responseSpec = mock(WebClient.ResponseSpec.class);

        when(webClientBuilder.build()).thenReturn(webClient);
        when(webClient.method(HttpMethod.POST)).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(any(String.class))).thenReturn(requestBodySpec);
        when(requestBodySpec.bodyValue(any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);

        gameClientAdapter = new GameClientAdapter(webClientBuilder);
    }

    @Test
    void createGame_ShouldReturnGameResponse() {
        // Arrange
        CreateGameRequestDTO requestDTO = new CreateGameRequestDTO("reservation-id", List.of());
        GameResponseDTO expectedResponse = new GameResponseDTO("game-123", "reservation-id", List.of(), false, 1);

        when(responseSpec.bodyToMono(GameResponseDTO.class)).thenReturn(Mono.just(expectedResponse));

        // Act
        GameResponseDTO result = gameClientAdapter.createGame(requestDTO);

        // Assert
        assertNotNull(result);
        assertEquals("game-123", result.getId());
        assertEquals("reservation-id", result.getReservationId());

        verify(webClient).method(HttpMethod.POST);
        verify(requestBodyUriSpec).uri(contains("/games/free"));
        verify(requestBodySpec).bodyValue(any(CreateGameRequestDTO.class));
        verify(requestHeadersSpec).retrieve();
    }
}
