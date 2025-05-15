package com.ayd.invoice_service.Invoice.adapter;

import com.ayd.invoice_service.Invoice.ports.EmployeeClientPort;
import com.ayd.shared.exceptions.NotFoundException;
import com.ayd.sharedEmployeeService.dto.EmployeeResponseDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@AllArgsConstructor
public class EmployeeClientAdapter implements EmployeeClientPort {

    private final WebClient.Builder webClientBuilder;

    @Override
    public EmployeeResponseDTO findEmployeeByUserName(String userName) throws NotFoundException {
        return webClientBuilder.build()
            .get()
            .uri("lb://API-GATEWAY/api/v1/employees/user-name/" + userName)
            .retrieve()
            .onStatus(status -> status.value() == 404,
                clientResponse -> clientResponse.bodyToMono(String.class)
                    .map(message -> new NotFoundException(message)))
            .bodyToMono(EmployeeResponseDTO.class)
            .block();
    }
}
