package com.ayd.reports_service.reservationProfit.adapters;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ayd.reports_service.reservationProfit.dtos.ReservationProfitDTO;
import com.ayd.reports_service.reservationProfit.dtos.ReservationWithPayMethod;
import com.ayd.sharedInvoiceService.enums.PaymentMethod;

import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;

@ExtendWith(MockitoExtension.class)
public class ReservationProfitParametersAdapterTest {

    private ReservationProfitParametersAdapter builder;
    private ReservationProfitDTO reportDTO;


    private static final String RESERVATION_ID_1 = "resv-001";
    private static final LocalDate DATE_1 = LocalDate.of(2025, 5, 14);
    private static final LocalTime START_TIME_1 = LocalTime.of(9, 0);
    private static final LocalTime END_TIME_1 = LocalTime.of(10, 0);
    private static final String CLIENT_NAME_1 = "Pedro López";
    private static final String CLIENT_NIT_1 = "12345678";
    private static final PaymentMethod PAYMENT_METHOD_1 = PaymentMethod.CASH;
    private static final BigDecimal TOTAL_1 = new BigDecimal(100.00);

    private static final String RESERVATION_ID_2 = "resv-002";
    private static final LocalDate DATE_2 = LocalDate.of(2025, 5, 15);
    private static final LocalTime START_TIME_2 = LocalTime.of(11, 0);
    private static final LocalTime END_TIME_2 = LocalTime.of(12, 0);
    private static final String CLIENT_NAME_2 = "Ana Torres";
    private static final String CLIENT_NIT_2 = "87654321";
    private static final PaymentMethod PAYMENT_METHOD_2 = PaymentMethod.CARD;
    private static final BigDecimal TOTAL_2 = new BigDecimal(150.00);


    private static final BigDecimal TOTAL_PROFIT = new BigDecimal(250.00);

    private final static ReservationWithPayMethod RES_1 = new ReservationWithPayMethod(
            RESERVATION_ID_1, DATE_1, START_TIME_1, END_TIME_1,
            CLIENT_NAME_1, CLIENT_NIT_1, PAYMENT_METHOD_1.toString(), TOTAL_1);

    private final static ReservationWithPayMethod RES_2 = new ReservationWithPayMethod(
            RESERVATION_ID_2, DATE_2, START_TIME_2, END_TIME_2,
            CLIENT_NAME_2, CLIENT_NIT_2, PAYMENT_METHOD_2.toString(), TOTAL_2);

    @BeforeEach
    void setUp() {
        builder = new ReservationProfitParametersAdapter();
        reportDTO = new ReservationProfitDTO(TOTAL_PROFIT, List.of(RES_1, RES_2));
    }

    /**
     * dado: dto válido
     * cuando: se llama init y luego buildParameters
     * entonces: retorna mapa con claves esperadas y JRBeanArrayDataSource
     */
    @Test
    void buildParametersReturnsValidMap() {
        // arrange
        builder.init(reportDTO);

        // act
        Map<String, Object> params = builder.buildParameters();

        // assert
        assertAll(
                () -> assertNotNull(params),
                () -> assertEquals(2, params.size()),
                () -> assertTrue(params.containsKey("detail")),
                () -> assertTrue(params.containsKey("total")),
                () -> assertTrue(params.get("detail") instanceof JRBeanArrayDataSource),
                () -> assertEquals(TOTAL_PROFIT, params.get("total"))
        );
    }
}
