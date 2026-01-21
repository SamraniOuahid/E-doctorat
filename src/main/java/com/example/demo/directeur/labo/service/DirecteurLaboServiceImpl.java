package com.example.demo.directeur.labo.service;

import com.example.demo.candidat.model.Candidat;
import com.example.demo.directeur.labo.dto.LaboCandidatDto;
import com.example.demo.directeur.labo.dto.LaboResultatDto;
import com.example.demo.directeur.labo.dto.LaboSujetDto;
import com.example.demo.professeur.model.Inscription;
import com.example.demo.professeur.model.Sujet;
import com.example.demo.professeur.model.ProfesseurModel;
import com.example.demo.professeur.repository.InscriptionRepository;
import com.example.demo.candidat.repository.SujetRepository;
import com.example.demo.professeur.repository.ProfesseurRepository;
import com.example.demo.security.user.UserRepository;
import com.example.demo.security.user.UserAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DirecteurLaboServiceImpl implements DirecteurLaboService {

    private final SujetRepository sujetRepository;
    private final InscriptionRepository inscriptionRepository;
    private final UserRepository userRepository;
    private final ProfesseurRepository professeurRepository;

    // ================== SUJETS DU LABO ==================

    @Override
    public List<LaboSujetDto> getSujetsDuLabo(Long laboId) {
        List<Sujet> sujets = sujetRepository.findByProfesseur_Laboratoire_Id(laboId);
        return sujets.stream()
                .map(this::toSujetDto)
                .toList();
    }

    // ================== CREATE SUJET (MANUAL) ==================

    @Override
    public LaboSujetDto createSujet(Long laboId, LaboSujetDto dto) {
        // 1. Verify Prof exists
        ProfesseurModel prof = professeurRepository.findById(dto.getProfesseurId())
                .orElseThrow(() -> new RuntimeException("Professeur not found"));

        // 2. Verify Prof belongs to this Labo
        if (prof.getLaboratoire() == null || !prof.getLaboratoire().getId().equals(laboId)) {
            throw new RuntimeException("Professeur does not belong to this laboratory");
        }

        // 3. Create Sujet
        Sujet sujet = new Sujet();
        sujet.setTitre(dto.getSujetTitre());
        sujet.setDescription(dto.getSujetTitre()); // defaulting description to title if not provided
        sujet.setProfesseur(prof);

        sujet = sujetRepository.save(sujet);

        return toSujetDto(sujet);
    }

    // ================== CANDIDATS DU LABO ==================

    @Override
    public List<LaboCandidatDto> getCandidatsDuLabo(Long laboId, Long sujetId) {
        List<Inscription> inscriptions;

        if (sujetId != null) {
            inscriptions = inscriptionRepository.findBySujet_Id(sujetId);
        } else {
            inscriptions = inscriptionRepository.findByLaboId(laboId);
        }

        return inscriptions.stream()
                .map(this::toCandidatDto)
                .toList();
    }

    // ================== RESULTATS ==================

    @Override
    public List<LaboResultatDto> getResultatsDuLabo(Long laboId) {
        List<Inscription> inscriptions = inscriptionRepository.findByLaboId(laboId);
        return inscriptions.stream()
                .map(this::toResultatDto)
                .toList();
    }

    // ================== INSCRITS ==================

    @Override
    public List<LaboResultatDto> getInscritsDuLabo(Long laboId) {
        List<Inscription> inscriptions = inscriptionRepository.findAcceptedByLaboId(laboId);
        return inscriptions.stream()
                .map(this::toResultatDto)
                .toList();
    }

    // ================== CSV IMPORT ==================

    @Override
    public void importSujetsCsv(Long laboId, java.io.InputStream inputStream) {
        try (java.io.Reader reader = new java.io.InputStreamReader(inputStream)) {

            com.opencsv.bean.CsvToBean<com.example.demo.directeur.labo.dto.LaboSujetCsvDto> csvToBean = new com.opencsv.bean.CsvToBeanBuilder<com.example.demo.directeur.labo.dto.LaboSujetCsvDto>(
                    reader)
                    .withType(com.example.demo.directeur.labo.dto.LaboSujetCsvDto.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            List<com.example.demo.directeur.labo.dto.LaboSujetCsvDto> dtos = csvToBean.parse();

            for (com.example.demo.directeur.labo.dto.LaboSujetCsvDto csvDto : dtos) {
                // Find User by email
                UserAccount user = userRepository.findByEmail(csvDto.getEmailProfesseur())
                        .orElse(null);

                if (user != null) {
                    // Find Professeur by User ID
                    ProfesseurModel prof = professeurRepository.findByUserId(user.getId())
                            .orElse(null);

                    if (prof != null && prof.getLaboratoire() != null && prof.getLaboratoire().getId().equals(laboId)) {
                        Sujet sujet = new Sujet();
                        sujet.setTitre(csvDto.getTitre());
                        sujet.setDescription(csvDto.getDescription());
                        sujet.setProfesseur(prof);
                        sujetRepository.save(sujet);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse CSV file: " + e.getMessage());
        }
    }

    // ================== PV GLOBAL ==================

    @Override
    public byte[] genererPvGlobal(Long laboId) {
        try (java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream()) {
            com.lowagie.text.Document document = new com.lowagie.text.Document();
            com.lowagie.text.pdf.PdfWriter.getInstance(document, out);

            document.open();
            document.add(new com.lowagie.text.Paragraph("PV Global du Laboratoire " + laboId));
            document.add(new com.lowagie.text.Paragraph("Liste des doctorants inscrits:"));
            document.add(new com.lowagie.text.Paragraph(" ")); // Spacer

            List<LaboResultatDto> inscrits = getInscritsDuLabo(laboId);

            com.lowagie.text.Table table = new com.lowagie.text.Table(3);
            table.addCell("Nom Complet");
            table.addCell("Sujet");
            table.addCell("Status");

            for (LaboResultatDto dto : inscrits) {
                table.addCell(dto.getCandidatNomComplet());
                table.addCell(dto.getSujetTitre());
                table.addCell(dto.getStatut());
            }

            document.add(table);
            document.close();

            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF: " + e.getMessage());
        }
    }

    // ================== MAPPERS ==================

    private LaboSujetDto toSujetDto(Sujet sujet) {
        LaboSujetDto dto = new LaboSujetDto();
        dto.setSujetId(sujet.getId());
        dto.setSujetTitre(sujet.getTitre());

        if (sujet.getProfesseur() != null) {
            dto.setProfesseurId(sujet.getProfesseur().getId());
            dto.setProfesseurGrade(sujet.getProfesseur().getGrade());
            dto.setProfesseurNumSom(sujet.getProfesseur().getNumSOM());
        }
        return dto;
    }

    private LaboCandidatDto toCandidatDto(Inscription i) {
        LaboCandidatDto dto = new LaboCandidatDto();
        dto.setInscriptionId(i.getId());
        dto.setSujetId(i.getSujet().getId());
        dto.setSujetTitre(i.getSujet().getTitre());

        Candidat c = i.getCandidat();
        if (c != null) {
            dto.setCandidatId(c.getId());
            dto.setCandidatNomComplet(c.getNomComplet());
            dto.setCandidatCne(c.getCne());
        }

        dto.setValider(i.getValider());
        dto.setRemarque(i.getRemarque());
        return dto;
    }

    private LaboResultatDto toResultatDto(Inscription i) {
        LaboResultatDto dto = new LaboResultatDto();
        dto.setInscriptionId(i.getId());
        dto.setSujetId(i.getSujet().getId());
        dto.setSujetTitre(i.getSujet().getTitre());

        Candidat c = i.getCandidat();
        if (c != null) {
            dto.setCandidatId(c.getId());
            dto.setCandidatNomComplet(c.getNomComplet());
            dto.setCandidatCne(c.getCne());
        }

        Boolean valider = i.getValider();
        dto.setValider(valider);

        String statut;
        if (Boolean.TRUE.equals(valider)) {
            statut = "ACCEPTE";
        } else if (Boolean.FALSE.equals(valider)) {
            statut = "REFUSE";
        } else {
            statut = "EN_ATTENTE";
        }
        dto.setStatut(statut);
        dto.setRemarque(i.getRemarque());
        return dto;
    }
}
