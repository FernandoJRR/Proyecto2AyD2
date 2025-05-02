package com.ayd.product_service.shared.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.mapstruct.Named;
import org.springframework.stereotype.Component;

@Component
public class DateFormatterUtil {

    @Named("formatDateToLocalFormat")
    public String formatDateToLocalFormat(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }
}
