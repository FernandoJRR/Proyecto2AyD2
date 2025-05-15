package com.ayd.reports_service.qr.services;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.springframework.stereotype.Component;

import com.ayd.reports_service.qr.ports.ForQrCodePort;
import com.ayd.shared.security.AppProperties;
import com.ayd.sharedReservationService.dto.ReservationResponseDTO;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class QrCodeAdapter implements ForQrCodePort {

    private final AppProperties appProperties;

    @Override
    public byte[] createReservationQR(ReservationResponseDTO reservation)
            throws WriterException, IOException {
        return generateQrCode(appProperties.getFrontURL() + "/app/juegos/jugar/" + reservation.getGameId());
    }

    @Override
    public byte[] generateQrCode(String text) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, 200, 200);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        return pngOutputStream.toByteArray();
    }

}
