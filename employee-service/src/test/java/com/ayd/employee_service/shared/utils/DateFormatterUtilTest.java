package com.ayd.employee_service.shared.utils;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class DateFormatterUtilTest {

    private final DateFormatterUtil dateFormatterUtil = new DateFormatterUtil();

    /**
     * dado: una fecha válida.
     * cuando: se llama a formatDateToLocalFormat.
     * entonces: se retorna el string formateado en formato dd/MM/yyyy.
     */
    @Test
    public void formatDateToLocalFormatShouldReturnCorrectFormat() {
        // Arrange
        LocalDate date = LocalDate.of(2023, 9, 15); // 15 de septiembre de 2023

        // Act
        String result = dateFormatterUtil.formatDateToLocalFormat(date);

        // Assert
        assertEquals("15/09/2023", result);
    }

    /**
     * dado: una fecha nula.
     * cuando: se llama a formatDateToLocalFormat.
     * entonces: se lanza NullPointerException.
     */
    @Test
    public void formatDateToLocalFormatShouldThrowWhenNull() {
        assertThrows(NullPointerException.class, () -> {
            dateFormatterUtil.formatDateToLocalFormat(null);
        });
    }
}
