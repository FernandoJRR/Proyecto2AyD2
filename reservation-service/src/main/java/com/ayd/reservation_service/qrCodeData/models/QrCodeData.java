package com.ayd.reservation_service.qrCodeData.models;

import com.ayd.reservation_service.reservation.models.Reservation;
import com.ayd.reservation_service.shared.models.Auditor;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class QrCodeData extends Auditor {
    
    private String qrContent;

    @OneToOne
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;
}
