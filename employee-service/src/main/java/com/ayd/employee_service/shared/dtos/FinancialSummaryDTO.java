package com.ayd.employee_service.shared.dtos;

import java.math.BigDecimal;

import lombok.Value;

/**
 * Representa un resumen financiero general con información agregada de ventas.
 * Esta clase contiene el total de ventas, el costo total y la ganancia total
 * calculada.
 *
 * @param totalSales  el monto total generado por las ventas.
 * @param totalCost   el costo total asociado a las ventas realizadas.
 * @param totalProfit la ganancia total, calculada como totalSales - totalCost.
 */
@Value
public class FinancialSummaryDTO {

    BigDecimal totalSales;
    BigDecimal totalCost;
    BigDecimal totalProfit;

}