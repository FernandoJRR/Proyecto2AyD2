package com.ayd.reports_service.reservationProfit.dtos;

import java.math.BigDecimal;
import java.util.List;

import lombok.Value;

@Value
public class ReservationProfitDTO {

    BigDecimal totalProfit;
    List<ReservationWithPayMethod> reservations;
}
