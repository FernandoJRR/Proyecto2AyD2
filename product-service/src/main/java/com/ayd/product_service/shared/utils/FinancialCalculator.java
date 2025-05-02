package com.ayd.product_service.shared.utils;

import java.util.List;

/**
 * Define un contrato genérico para clases que realizan cálculos financieros
 * sobre un conjunto de movimientos, datos o registros financieros.
 * 
 * @param R el condensado con los totales obtenidos tras la operacion
 * @param T el tipo de datos sobre los cuales se realizarán los cálculos (por
 *          ejemplo, lista de ventas, egresos, etc.)
 */
public interface FinancialCalculator<R, T> {

    /**
     * Calcula los totales financieros (ventas, costo y ganancia...) a partir de un
     * dato o datos.
     *
     * @param financialMoves dato o conjunto de datos financieros a procesar
     * @return un objeto FinancialSummaryDTO con los totales calculados
     */
    public R calculateFinancialTotalsOfList(List<T> financialMoves);
    public R calculateFinancialTotals(T financialMove);
}
