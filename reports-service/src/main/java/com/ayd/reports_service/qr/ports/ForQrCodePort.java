package com.ayd.reports_service.qr.ports;

import java.io.IOException;

import com.ayd.sharedReservationService.dto.ReservationResponseDTO;
import com.google.zxing.WriterException;

public interface ForQrCodePort {

    public byte[] createReservationQR(ReservationResponseDTO reservation)
            throws WriterException, IOException;

    public byte[] generateQrCode(String text) throws WriterException, IOException;
}
