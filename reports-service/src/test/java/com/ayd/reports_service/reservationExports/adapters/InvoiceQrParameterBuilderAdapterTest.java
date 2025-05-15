package com.ayd.reports_service.reservationExports.adapters;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import com.ayd.reports_service.qr.ports.ForQrCodePort;
import com.ayd.sharedInvoiceService.dtos.InvoiceDetailResponseDTO;
import com.ayd.sharedInvoiceService.dtos.InvoiceResponseDTO;
import com.ayd.sharedInvoiceService.enums.ItemType;
import com.ayd.sharedInvoiceService.enums.PaymentMethod;
import com.ayd.sharedReservationService.dto.ReservationInterServiceDTO;
import com.ayd.sharedReservationService.dto.ReservationResponseDTO;

import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class InvoiceQrParameterBuilderAdapterTest {

        private static final String DETAIL_ID = "det-001";
        private static final String ITEM_ID = "item-001";
        private static final String ITEM_NAME = "Pizza";
        private static final ItemType ITEM_TYPE = ItemType.GOOD;
        private static final int QUANTITY = 1;
        private static final BigDecimal UNIT_PRICE = new BigDecimal("100.00");
        private static final BigDecimal DETAIL_TOTAL = new BigDecimal("100.00");

        private static final String INVOICE_ID = "inv-001";
        private static final PaymentMethod PAYMENT_METHOD = PaymentMethod.CASH;
        private static final BigDecimal SUBTOTAL = new BigDecimal("100.00");
        private static final BigDecimal TAX = new BigDecimal("12.00");
        private static final BigDecimal TOTAL = new BigDecimal("112.00");
        private static final String CLIENT_DOCUMENT = "1234567-8";

        private static final String RES_ID = "res-001";
        private static final String GAME_ID = "game-001";
        private static final LocalTime START = LocalTime.of(10, 0);
        private static final LocalTime END = LocalTime.of(11, 0);
        private static final LocalDate DATE = LocalDate.of(2025, 5, 15);
        private static final String CLIENT_NAME = "Juan Pérez";
        private static final String CLIENT_NIT = "1234567-8";

        private static final byte[] FAKE_QR_BYTES = "fake-bytes".getBytes();

        @Mock
        private ForQrCodePort qrCodePort;

        @InjectMocks
        private InvoiceQrParameterBuilderAdapter builder;

        private ReservationInterServiceDTO report;

        @BeforeEach
        void setUp() throws Exception {
                InvoiceDetailResponseDTO detail = new InvoiceDetailResponseDTO(
                                DETAIL_ID, ITEM_ID, ITEM_NAME, ITEM_TYPE, QUANTITY, UNIT_PRICE, DETAIL_TOTAL);

                InvoiceResponseDTO invoice = new InvoiceResponseDTO(
                                INVOICE_ID, PAYMENT_METHOD, SUBTOTAL, TAX, TOTAL, CLIENT_DOCUMENT, List.of(detail));

                ReservationResponseDTO reservation = new ReservationResponseDTO(
                                RES_ID, START, END, DATE, false, true, GAME_ID, CLIENT_NAME, CLIENT_NIT, INVOICE_ID);

                report = new ReservationInterServiceDTO(reservation, invoice);

                when(qrCodePort.createReservationQR(any())).thenReturn(FAKE_QR_BYTES);

                builder.init(report);
        }

        /**
         * dado: DTO de reserva-factura valido
         * cuando: se llama buildParameters
         * entonces: retorna mapa con claves esperadas y tipos correctos
         */
        @Test
        void buildParametersReturnsValidMap() {
                // act
                Map<String, Object> result = builder.buildParameters();

                // assert
                assertAll(
                                () -> assertNotNull(result),
                                () -> assertTrue(result.get("detail") instanceof JRBeanArrayDataSource),
                                () -> assertTrue(result.get("qr") instanceof InputStream),
                                () -> assertEquals(TOTAL, result.get("total")),
                                () -> assertEquals(SUBTOTAL, result.get("subTotal")),
                                () -> assertEquals(TAX, result.get("tax")),
                                () -> assertEquals(CLIENT_NAME, result.get("clientFullname")),
                                () -> assertEquals(CLIENT_NIT, result.get("clientNit")),
                                () -> assertEquals(GAME_ID, result.get("gameId")),
                                () -> assertEquals(RES_ID, result.get("reservationId")),
                                () -> assertEquals(DATE, result.get("date")),
                                () -> assertEquals(START, result.get("startTime")),
                                () -> assertEquals(END, result.get("endTime")));
        }
}
