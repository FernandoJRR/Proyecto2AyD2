package com.ayd.reports_service.reservationExports.adapters;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.ayd.reports_service.qr.ports.ForQrCodePort;
import com.ayd.reports_service.shared.ports.ReportParameterBuilderPort;
import com.ayd.sharedReservationService.dto.ReservationInterServiceDTO;
import com.google.zxing.WriterException;

import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;

@Component
@RequiredArgsConstructor
public class InvoiceQrParameterBuilderAdapter implements ReportParameterBuilderPort<ReservationInterServiceDTO> {

    private ReservationInterServiceDTO report;
    private final ForQrCodePort forQrCode;

    @Override
    public void init(ReservationInterServiceDTO reportDTO) {
        this.report = reportDTO;
    }

    @Override
    public Map<String, Object> buildParameters() {
        // crear el mapa de params para el reporte
        Map<String, Object> params = new HashMap<>();
        try {

            // convertir la lista de la factura a un JRBeanArrayDataSource para
            // JasperReports
            JRBeanArrayDataSource detail = new JRBeanArrayDataSource(
                    report.getInvoiceResponseDTO().getDetails().toArray());

            // mandamos a crear el qr
            byte[] qr = forQrCode.createReservationQR(report.getReservationResponseDTO());
            InputStream qStream = new ByteArrayInputStream(qr);

            // agregar aprams clave al mapa
            params.put("qr", qStream);
            params.put("detail", detail);
            params.put("total", report.getInvoiceResponseDTO().getTotal());
            params.put("tax", report.getInvoiceResponseDTO().getTax());
            params.put("subTotal", report.getInvoiceResponseDTO().getSubtotal());

            params.put("reservationId", report.getReservationResponseDTO().getId());
            params.put("gameId", report.getReservationResponseDTO().getGameId());
            params.put("clientNit", report.getReservationResponseDTO().getCustomerNIT());
            params.put("clientFullname", report.getReservationResponseDTO().getCustomerFullName());
            params.put("date", report.getReservationResponseDTO().getDate());
            params.put("startTime", report.getReservationResponseDTO().getStartTime());
            params.put("endTime", report.getReservationResponseDTO().getEndTime());

        } catch (WriterException | IOException e) {
            e.printStackTrace();

        }
        return params;
    }

}
