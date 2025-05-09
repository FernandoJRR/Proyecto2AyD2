package com.ayd.reservation_service.reservation.models;

import java.time.LocalDateTime;

import com.ayd.reservation_service.shared.models.Auditor;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Reservation extends Auditor {
    @Column(nullable = false)
    private LocalDateTime reservationDateTime;

    @Column(nullable = false)
    private String clientDocument; // CUI o NIT del cliente que reservó

    @Column(nullable = false)
    private String packageId; // UUID del paquete de juego

    private boolean paid = false; 

    private boolean cancelled = false; 
}
