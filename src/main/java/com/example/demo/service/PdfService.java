package com.example.demo.service;

import java.io.IOException;

public interface PdfService {
    byte[] generateBoursePdf(Long candidatId) throws IOException;

    byte[] generateInscriptionPdf(Long candidatId) throws IOException;
}
