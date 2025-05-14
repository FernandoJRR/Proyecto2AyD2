package com.ayd.reservation_service.qr.ports;

import java.io.IOException;

import com.google.zxing.WriterException;

public interface ForQrCode {

    public byte[] generateQrCode(String text) throws WriterException, IOException;
}
