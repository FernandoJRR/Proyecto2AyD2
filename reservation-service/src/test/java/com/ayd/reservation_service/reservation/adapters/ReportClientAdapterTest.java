package com.ayd.reservation_service.reservation.adapters;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.ayd.sharedReservationService.dto.ReservationInterServiceDTO;
import com.ayd.sharedReservationService.dto.ReservationResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

class ReportClientAdapterTest {

    private WebClient.Builder webClientBuilder;
    private WebClient webClient;
    private WebClient.RequestBodyUriSpec requestBodyUriSpec;
    private WebClient.RequestBodySpec requestBodySpec;
    @SuppressWarnings("rawtypes")
    private WebClient.RequestHeadersSpec requestHeadersSpec;
    private WebClient.ResponseSpec responseSpec;

    private ReportClientAdapter reportClientAdapter;

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

        reportClientAdapter = new ReportClientAdapter(webClientBuilder);
    }

    @Test
    void exportInvoiceWithQR_ShouldReturnPdfBytes() {
        byte[] expectedBytes = "PDF_INVOICE".getBytes();
        when(responseSpec.bodyToMono(byte[].class)).thenReturn(Mono.just(expectedBytes));

        byte[] result = reportClientAdapter.exportInvoiceWithQR(new ReservationInterServiceDTO(null, null));

        assertArrayEquals(expectedBytes, result);
        verify(webClient).method(HttpMethod.POST);
        verify(requestBodyUriSpec).uri(contains("/invoice-qr"));
    }

    @Test
    void exportReservationTicket_ShouldReturnPdfBytes() {
        byte[] expectedBytes = "PDF_TICKET".getBytes();
        when(responseSpec.bodyToMono(byte[].class)).thenReturn(Mono.just(expectedBytes));

        byte[] result = reportClientAdapter.exportReservationTicket(
                new ReservationResponseDTO(null, null, null, null, null, null, null, null, null, null));

        assertArrayEquals(expectedBytes, result);
        verify(webClient).method(HttpMethod.POST);
        verify(requestBodyUriSpec).uri(contains("/reservation-ticket"));
    }
}
