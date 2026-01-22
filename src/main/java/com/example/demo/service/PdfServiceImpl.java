package com.example.demo.service;

import com.example.demo.candidat.model.Candidat;
import com.example.demo.candidat.model.CandidatChoix;
import com.example.demo.candidat.repository.CandidatChoixRepository;
import com.example.demo.candidat.repository.CandidatRepository;
import com.example.demo.professeur.model.Inscription;
import com.example.demo.professeur.repository.InscriptionRepository;
import com.example.demo.scolarite.model.EtatDossier;
import com.example.demo.security.user.UserRepository;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class PdfServiceImpl implements PdfService {

    private final InscriptionRepository inscriptionRepository;
    private final UserRepository userRepository;
    private final CandidatRepository candidatRepository;
    private final CandidatChoixRepository candidatChoixRepository;

    @Override
    public byte[] generateBoursePdf(Long candidatId) throws IOException {
        Inscription inscription = getAcceptedInscriptionByCandidat(candidatId);
        String candidatName = inscription.getCandidat().getUser().getFullName();
        String profName = getProfesseurName(inscription.getSujet().getProfesseur().getUserId());

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, out);
            document.open();

            // Font styles
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 12);

            // Title
            Paragraph title = new Paragraph("DEMANDE DE BOURSE", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph("\n"));

            // Content
            document.add(new Paragraph("Je soussigné(e),", normalFont));
            document.add(new Paragraph("Nom et Prénom: " + candidatName, normalFont));
            document.add(new Paragraph("CNE: " + inscription.getCandidat().getCne(), normalFont));

            document.add(new Paragraph(
                    "\nSollicite par la présente l'attribution d'une bourse d'étude pour mon doctorat.", normalFont));

            document.add(new Paragraph("\nSujet de thèse: " + inscription.getSujet().getTitre(), normalFont));
            document.add(new Paragraph("Professeur encadrant: " + profName, normalFont));

            document.add(new Paragraph("\n\nSignature:", normalFont));

            document.close();
            return out.toByteArray();
        }
    }

    @Override
    public byte[] generateInscriptionPdf(Long candidatId) throws IOException {
        Inscription inscription = getAcceptedInscriptionByCandidat(candidatId);
        String candidatName = inscription.getCandidat().getUser().getFullName();
        String profName = getProfesseurName(inscription.getSujet().getProfesseur().getUserId());

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, out);
            document.open();

            // Header
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20);
            Paragraph header = new Paragraph("ATTESTATION D'INSCRIPTION", headerFont);
            header.setAlignment(Element.ALIGN_CENTER);
            document.add(header);
            document.add(new Paragraph("\n\n"));

            // Body
            Font bodyFont = FontFactory.getFont(FontFactory.HELVETICA, 14);
            Paragraph body = new Paragraph();
            body.add(new Chunk("Le directeur du CED certifie que l'étudiant(e) :\n\n", bodyFont));
            body.add(new Chunk(
                    "M./Mme " + candidatName.toUpperCase() + "\n",
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14)));
            body.add(new Chunk("CNE: " + inscription.getCandidat().getCne() + "\n\n", bodyFont));
            body.add(new Chunk("Est régulièrement inscrit(e) en première année de Doctorat.\n", bodyFont));
            body.add(new Chunk("Sujet: " + inscription.getSujet().getTitre() + "\n", bodyFont));
            body.add(new Chunk("Encadrant: Pr. " + profName.toUpperCase() + "\n\n", bodyFont));

            document.add(body);

            // Footer
            Paragraph footer = new Paragraph("\n\nFait à Casablanca, le " + java.time.LocalDate.now());
            footer.setAlignment(Element.ALIGN_RIGHT);
            document.add(footer);

            document.close();
            return out.toByteArray();
        }
    }

    private Inscription getAcceptedInscriptionByCandidat(Long candidatId) {
        Candidat candidat = candidatRepository.findById(candidatId)
                .orElseThrow(() -> new RuntimeException("Candidat non trouvé ID: " + candidatId));

        // 1. Tenter de trouver une inscription déjà validée (par le prof)
        java.util.Optional<Inscription> validatedInsc = inscriptionRepository.findAll().stream()
                .filter(i -> i.getCandidat().getId().equals(candidatId) && Boolean.TRUE.equals(i.getValider()))
                .findFirst();

        if (validatedInsc.isPresent()) {
            return validatedInsc.get();
        }

        // 2. Tenter de trouver n'importe quelle inscription existante
        java.util.Optional<Inscription> anyInsc = inscriptionRepository.findAll().stream()
                .filter(i -> i.getCandidat().getId().equals(candidatId))
                .findFirst();

        if (anyInsc.isPresent()) {
            return anyInsc.get();
        }

        // 3. Fallback sur les choix du candidat (CandidatChoix) si le candidat est
        // accepté
        if (candidat.getEtatDossier() == EtatDossier.ACCEPTE) {
            java.util.List<CandidatChoix> choixList = candidatChoixRepository.findByCandidatId(candidatId);
            if (!choixList.isEmpty()) {
                // Créer une inscription temporaire à partir du premier choix
                CandidatChoix premierChoix = choixList.get(0);
                Inscription tempInsc = new Inscription();
                tempInsc.setCandidat(candidat);
                tempInsc.setSujet(premierChoix.getSujet());
                tempInsc.setValider(true);
                return tempInsc;
            }
        }

        throw new RuntimeException("Aucune inscription ou choix trouvé pour le candidat ID: " + candidatId);
    }

    private String getProfesseurName(Long userId) {
        return userRepository.findById(userId)
                .map(com.example.demo.security.user.UserAccount::getFullName)
                .orElse("Inconnu");
    }
}
