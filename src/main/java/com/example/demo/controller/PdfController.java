package com.example.demo.controller;

import com.example.demo.service.PdfService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/public/pdf")
@RequiredArgsConstructor
public class PdfController {

    private final PdfService pdfService;

    @GetMapping("/bourse/{candidatId}")
    public ResponseEntity<byte[]> downloadBoursePdf(@PathVariable Long candidatId) throws IOException {
        byte[] pdfContent = pdfService.generateBoursePdf(candidatId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=demande_bourse.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfContent);
    }

    @GetMapping("/inscription/{candidatId}")
    public ResponseEntity<byte[]> downloadInscriptionPdf(@PathVariable Long candidatId) throws IOException {
        byte[] pdfContent = pdfService.generateInscriptionPdf(candidatId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=attestation_inscription.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfContent);
    }
}
