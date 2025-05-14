package com.ayd.reservation_service.reservation.dtos;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.ayd.game_service_common.players.dtos.CreatePlayerRequestDTO;
import com.ayd.sharedInvoiceService.dtos.CreateInvoiceRequestDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Value;

@Value
public class CreateReservationRequestDTO {
    @NotNull(message = "La hora de inicio es requerida")
    LocalTime startTime;
    @NotNull(message = "La hora de fin es requerida")
    LocalTime endTime;
    @NotNull(message = "La fecha es requerida")
    LocalDate date;

    @NotBlank(message = "El NIT del cliente es requerido")
    String customerNit;
    @NotBlank(message = "El nombre del cliente es requerido")
    String customerFullname;

    @NotBlank(message = "El id del paquete es requerido")
    String packageId;

    CreateInvoiceRequestDTO createInvoiceRequestDTO;

    @Size(max = 4, message = "El maximo de jugadores es 4")
    List<CreatePlayerRequestDTO> players;

}
