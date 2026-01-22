package com.example.demo.directeur.pole.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Service;

import com.example.demo.directeur.pole.dto.PoleCandidatDto;
import com.example.demo.directeur.pole.dto.PoleResultatDto;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Service for exporting data to CSV format
 * CSV files are Excel-compatible with UTF-8 BOM encoding
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CsvExportService {

    /**
     * Export Liste Principale to CSV
     */
    public byte[] exportListePrincipale(List<PoleResultatDto> resultats, List<PoleCandidatDto> candidats) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            // Add UTF-8 BOM for Excel compatibility
            baos.write(0xEF);
            baos.write(0xBB);
            baos.write(0xBF);
            
            Writer writer = new OutputStreamWriter(baos, StandardCharsets.UTF_8);
            
            CSVFormat format = CSVFormat.EXCEL
                    .builder()
                    .setHeader("Rang", "CNE", "Nom", "Prénom", "Email", "Téléphone", 
                              "Formation", "Laboratoire", "Sujet", "Moyenne", "Décision")
                    .setDelimiter(';') // European Excel uses semicolon
                    .build();
            
            CSVPrinter printer = new CSVPrinter(writer, format);
            
            int rang = 1;
            for (PoleCandidatDto candidat : candidats) {
                printer.printRecord(
                    rang++,
                    candidat.getCne() != null ? candidat.getCne() : "",
                    candidat.getNom() != null ? candidat.getNom() : "",
                    candidat.getPrenom() != null ? candidat.getPrenom() : "",
                    candidat.getEmail() != null ? candidat.getEmail() : "",
                    candidat.getTelephone() != null ? candidat.getTelephone() : "",
                    candidat.getFormation() != null ? candidat.getFormation() : "",
                    candidat.getLaboratoire() != null ? candidat.getLaboratoire() : "",
                    candidat.getSujetTitre() != null ? candidat.getSujetTitre() : "",
                    candidat.getMoyenneGenerale() != null ? candidat.getMoyenneGenerale() : "",
                    "ADMIS"
                );
            }
            
            printer.flush();
            writer.flush();
            
            log.info("Exported {} candidates to LP CSV", candidats.size());
            return baos.toByteArray();
            
        } catch (Exception e) {
            log.error("Error exporting LP to CSV", e);
            throw new RuntimeException("Erreur lors de l'export CSV: " + e.getMessage());
        }
    }

    /**
     * Export Liste d'Attente to CSV
     */
    public byte[] exportListeAttente(List<PoleResultatDto> resultats, List<PoleCandidatDto> candidats) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            // Add UTF-8 BOM for Excel compatibility
            baos.write(0xEF);
            baos.write(0xBB);
            baos.write(0xBF);
            
            Writer writer = new OutputStreamWriter(baos, StandardCharsets.UTF_8);
            
            CSVFormat format = CSVFormat.EXCEL
                    .builder()
                    .setHeader("Rang", "CNE", "Nom", "Prénom", "Email", "Téléphone",
                              "Formation", "Laboratoire", "Sujet", "Moyenne", "Statut")
                    .setDelimiter(';')
                    .build();
            
            CSVPrinter printer = new CSVPrinter(writer, format);
            
            int rang = 1;
            for (PoleCandidatDto candidat : candidats) {
                printer.printRecord(
                    rang++,
                    candidat.getCne() != null ? candidat.getCne() : "",
                    candidat.getNom() != null ? candidat.getNom() : "",
                    candidat.getPrenom() != null ? candidat.getPrenom() : "",
                    candidat.getEmail() != null ? candidat.getEmail() : "",
                    candidat.getTelephone() != null ? candidat.getTelephone() : "",
                    candidat.getFormation() != null ? candidat.getFormation() : "",
                    candidat.getLaboratoire() != null ? candidat.getLaboratoire() : "",
                    candidat.getSujetTitre() != null ? candidat.getSujetTitre() : "",
                    candidat.getMoyenneGenerale() != null ? candidat.getMoyenneGenerale() : "",
                    "EN ATTENTE"
                );
            }
            
            printer.flush();
            writer.flush();
            
            log.info("Exported {} candidates to LA CSV", candidats.size());
            return baos.toByteArray();
            
        } catch (Exception e) {
            log.error("Error exporting LA to CSV", e);
            throw new RuntimeException("Erreur lors de l'export CSV: " + e.getMessage());
        }
    }

    /**
     * Export all inscriptions to CSV
     */
    public byte[] exportInscriptions(List<PoleCandidatDto> candidats) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            // Add UTF-8 BOM for Excel compatibility
            baos.write(0xEF);
            baos.write(0xBB);
            baos.write(0xBF);
            
            Writer writer = new OutputStreamWriter(baos, StandardCharsets.UTF_8);
            
            CSVFormat format = CSVFormat.EXCEL
                    .builder()
                    .setHeader("CNE", "Nom Complet", "Email", "Téléphone", "Formation", 
                              "Laboratoire", "Sujet", "Diplôme", "Établissement", "Moyenne", 
                              "Date Naissance", "Ville")
                    .setDelimiter(';')
                    .build();
            
            CSVPrinter printer = new CSVPrinter(writer, format);
            
            for (PoleCandidatDto candidat : candidats) {
                String nomComplet = (candidat.getNom() != null ? candidat.getNom() : "") + " " + 
                                   (candidat.getPrenom() != null ? candidat.getPrenom() : "");
                
                printer.printRecord(
                    candidat.getCne() != null ? candidat.getCne() : "",
                    nomComplet.trim(),
                    candidat.getEmail() != null ? candidat.getEmail() : "",
                    candidat.getTelephone() != null ? candidat.getTelephone() : "",
                    candidat.getFormation() != null ? candidat.getFormation() : "",
                    candidat.getLaboratoire() != null ? candidat.getLaboratoire() : "",
                    candidat.getSujetTitre() != null ? candidat.getSujetTitre() : "",
                    candidat.getDiplomePrecedent() != null ? candidat.getDiplomePrecedent() : "",
                    candidat.getEtablissement() != null ? candidat.getEtablissement() : "",
                    candidat.getMoyenneGenerale() != null ? candidat.getMoyenneGenerale() : "",
                    candidat.getDateNaissance() != null ? candidat.getDateNaissance() : "",
                    candidat.getLieuNaissance() != null ? candidat.getLieuNaissance() : ""
                );
            }
            
            printer.flush();
            writer.flush();
            
            log.info("Exported {} inscriptions to CSV", candidats.size());
            return baos.toByteArray();
            
        } catch (Exception e) {
            log.error("Error exporting inscriptions to CSV", e);
            throw new RuntimeException("Erreur lors de l'export CSV: " + e.getMessage());
        }
    }
}
