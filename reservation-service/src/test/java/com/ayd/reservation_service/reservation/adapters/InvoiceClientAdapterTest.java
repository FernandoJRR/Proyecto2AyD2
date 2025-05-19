package com.ayd.reservation_service.reservation.adapters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.*;

import com.ayd.game_service_common.games.dtos.CreateGameRequestDTO;
import com.ayd.game_service_common.games.dtos.GameResponseDTO;
import com.ayd.sharedInvoiceService.dtos.CreateInvoiceRequestDTO;
import com.ayd.sharedInvoiceService.dtos.InvoiceResponseDTO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersUriSpec;

import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

class InvoiceClientAdapterTest {

    private WebClient.Builder webClientBuilder;
    private WebClient webClient;
    private WebClient.RequestBodyUriSpec requestBodyUriSpec;
    private WebClient.RequestBodySpec requestBodySpec;
    @SuppressWarnings("rawtypes")
    private WebClient.RequestHeadersSpec requestHeadersSpec;
    private WebClient.ResponseSpec responseSpec;

    private InvoiceClientAdapter invoiceClientAdapter;

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

        invoiceClientAdapter = new InvoiceClientAdapter(webClientBuilder);
    }

    @Test
    void createInvoice_ShouldReturnInvoiceResponseDTO() {
        // Arrange
        CreateInvoiceRequestDTO requestDTO = new CreateInvoiceRequestDTO(null, null, null);
        InvoiceResponseDTO expectedResponse = new InvoiceResponseDTO("invoice-123", null, null, null, null, null, null);

        when(responseSpec.bodyToMono(InvoiceResponseDTO.class)).thenReturn(Mono.just(expectedResponse));

        // Act
        InvoiceResponseDTO result = invoiceClientAdapter.createInvoice(requestDTO);

        // Assert
        assertNotNull(result);
        assertEquals("invoice-123", result.getId());

        verify(webClient).method(HttpMethod.POST);
        verify(requestBodyUriSpec).uri(contains("/invoices"));
        verify(requestBodySpec).bodyValue(any(CreateInvoiceRequestDTO.class));
        verify(requestHeadersSpec).retrieve();
    }

    @Test
    void getInvoice_ShouldReturnInvoiceResponseDTO() {
        // Arrange
        String invoiceId = UUID.randomUUID().toString();
        InvoiceResponseDTO expected = new InvoiceResponseDTO(invoiceId, null, null, null, null, null, null);

        @SuppressWarnings("unchecked")
        WebClient.RequestHeadersUriSpec getUriSpec = mock(WebClient.RequestHeadersUriSpec.class);
        @SuppressWarnings("unchecked")
        WebClient.RequestHeadersSpec getHeadersSpec = mock(WebClient.RequestHeadersSpec.class);

        when(webClient.get()).thenReturn(getUriSpec);
        when(getUriSpec.uri(anyString())).thenReturn(getHeadersSpec);
        when(getHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(InvoiceResponseDTO.class))
                .thenReturn(Mono.just(expected));

        // Act
        InvoiceResponseDTO result = invoiceClientAdapter.getInvoice(invoiceId);

        // Assert
        assertNotNull(result);
        assertEquals(invoiceId, result.getId());

        verify(webClient).get();
        verify(getUriSpec).uri(ArgumentMatchers.contains(invoiceId));
        verify(getHeadersSpec).retrieve();
    }
}
