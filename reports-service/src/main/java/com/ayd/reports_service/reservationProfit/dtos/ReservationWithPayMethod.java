package com.ayd.reports_service.reservationProfit.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Value;

@Value
public class ReservationWithPayMethod {
    String reservationId;
    LocalDate date;
    LocalTime startTime;
    LocalTime endTime;
    String customerFullName;
    String customerNIT;
    String paymentMethod;
    BigDecimal total;

}
