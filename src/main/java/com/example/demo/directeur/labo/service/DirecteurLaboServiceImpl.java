package com.example.demo.directeur.labo.service;

import com.example.demo.candidat.model.Candidat;
import com.example.demo.directeur.labo.dto.LaboCandidatDto;
import com.example.demo.directeur.labo.dto.LaboResultatDto;
import com.example.demo.directeur.labo.dto.LaboSujetDto;
import com.example.demo.directeur.labo.dto.ProfessorDto;
import com.example.demo.professeur.model.Inscription;
import com.example.demo.professeur.model.Sujet;
import com.example.demo.professeur.model.ProfesseurModel;
import com.example.demo.professeur.model.Laboratoire;
import com.example.demo.professeur.repository.InscriptionRepository;
import com.example.demo.candidat.repository.SujetRepository;
import com.example.demo.professeur.repository.ProfesseurRepository;
import com.example.demo.security.user.UserRepository;
import com.example.demo.security.user.UserAccount;
import com.example.demo.professeur.model.Commission;
import com.example.demo.professeur.model.CommissionProfesseur;
import com.example.demo.professeur.repository.CommissionRepository;
import com.example.demo.professeur.repository.CommissionProfesseurRepository;
import com.example.demo.directeur.labo.dto.CommissionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DirecteurLaboServiceImpl implements DirecteurLaboService {

    private final SujetRepository sujetRepository;
    private final InscriptionRepository inscriptionRepository;
    private final UserRepository userRepository;
    private final ProfesseurRepository professeurRepository;
    private final com.example.demo.professeur.repository.FormationDoctoraleRepository formationDoctoraleRepository;
    private final CommissionRepository commissionRepository;
    private final CommissionProfesseurRepository commissionProfesseurRepository;
    private final com.example.demo.professeur.repository.LaboratoireRepository laboratoireRepository;
    private final jakarta.persistence.EntityManager entityManager;

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
        // 1. Find the ProfesseurModel for the main professor (Director)
        // We assume the frontend sends the ProfesseurModel.id or UserAccount.id? 
        // Based on my research, the frontend uses ProfessorDto which has 'id'. 
        // In the existing code, it uses professeurRepository.findById(dto.getProfesseurId()).
        // Let's stick to the current logic but ensure we can fetch by UserID if needed.
        // HOWEVER, the user said "selectionner dont les selecte de directeur et codirecteur se trouve dans sec_user_account".
        // This means the ID sent from frontend will be the UserAccount ID.
        
        UserAccount userProf = userRepository.findById(dto.getProfesseurId())
                .orElseThrow(() -> new RuntimeException("User Account not found for Director"));
        
        ProfesseurModel prof = professeurRepository.findByUserId(userProf.getId())
                .orElseThrow(() -> new RuntimeException("Professeur profile not found for Director"));

        // 2. Verify Prof belongs to this Labo (use native query to avoid Hibernate issues)
        Long profLaboId = professeurRepository.findLaboIdByUserId(userProf.getId());
        if (profLaboId == null || !profLaboId.equals(laboId)) {
            throw new RuntimeException("Director does not belong to this laboratory");
        }

        // 3. Validation: PA (Professeur Assistant) cannot be a Director
        if ("PA".equals(prof.getGrade())) {
            throw new RuntimeException("Professeur Assistant (PA) cannot be a primary Director");
        }

        // 4. Create Sujet
        Sujet sujet = new Sujet();
        sujet.setTitre(dto.getSujetTitre());
        sujet.setDescription(dto.getSujetTitre());
        sujet.setProfesseur(prof);

        if (dto.getFormationDoctoraleId() != null) {
            com.example.demo.professeur.model.FormationDoctorale fd = formationDoctoraleRepository.findById(dto.getFormationDoctoraleId())
                    .orElseThrow(() -> new RuntimeException("Formation Doctorale not found"));
            sujet.setFormationDoctorale(fd);
        }

        if (dto.getCoDirecteurId() != null) {
            UserAccount userCoDir = userRepository.findById(dto.getCoDirecteurId())
                    .orElseThrow(() -> new RuntimeException("User Account not found for Co-Director"));
            
            ProfesseurModel coDir = professeurRepository.findByUserId(userCoDir.getId())
                    .orElseThrow(() -> new RuntimeException("Professeur profile not found for Co-Director"));

            // Validation: max 12 subjects for Co-Director
            long countDir = sujetRepository.countByProfesseur_Id(coDir.getId());
            long countCoDir = sujetRepository.countByCoDirecteur_Id(coDir.getId());
            if ((countDir + countCoDir) >= 12) {
                throw new RuntimeException("Co-Director has reached the limit of 12 subjects");
            }
            sujet.setCoDirecteur(coDir);
        }

        sujet = sujetRepository.save(sujet);
        return toSujetDto(sujet);
    }

    // ================== UPDATE SUJET ==================

    @Override
    public LaboSujetDto updateSujet(Long laboId, Long sujetId, LaboSujetDto dto) {
        Sujet sujet = sujetRepository.findById(sujetId)
                .orElseThrow(() -> new RuntimeException("Sujet not found"));

        if (sujet.getProfesseur() == null) {
            throw new RuntimeException("Sujet professor not found");
        }
        
        Long currentProfLaboId = professeurRepository.findLaboIdByUserId(sujet.getProfesseur().getUserId());
        if (currentProfLaboId == null || !currentProfLaboId.equals(laboId)) {
            throw new RuntimeException("Sujet does not belong to this laboratory");
        }

        // Potential Director change
        if (dto.getProfesseurId() != null && !dto.getProfesseurId().equals(sujet.getProfesseur().getUserId())) {
            UserAccount userProf = userRepository.findById(dto.getProfesseurId())
                    .orElseThrow(() -> new RuntimeException("User Account not found for new Director"));
            
            ProfesseurModel newProf = professeurRepository.findByUserId(userProf.getId())
                    .orElseThrow(() -> new RuntimeException("Professeur profile not found for new Director"));

            Long newProfLaboId = professeurRepository.findLaboIdByUserId(userProf.getId());
            if (newProfLaboId == null || !newProfLaboId.equals(laboId)) {
                throw new RuntimeException("New Director does not belong to this laboratory");
            }

            // Validation: PA cannot be a Director
            if ("PA".equals(newProf.getGrade())) {
                throw new RuntimeException("Professeur Assistant (PA) cannot be a primary Director");
            }

            sujet.setProfesseur(newProf);
        }

        sujet.setTitre(dto.getSujetTitre());
        if (sujet.getDescription() == null || sujet.getDescription().equals("")) {
             sujet.setDescription(dto.getSujetTitre());
        }

        if (dto.getCoDirecteurId() != null) {
            UserAccount userCoDir = userRepository.findById(dto.getCoDirecteurId())
                    .orElseThrow(() -> new RuntimeException("User Account not found for Co-Director"));
            
            ProfesseurModel coDir = professeurRepository.findByUserId(userCoDir.getId())
                    .orElseThrow(() -> new RuntimeException("Professeur profile not found for Co-Director"));

            // Validation: max 12 subjects (if it's a NEW co-director or we just want to be safe)
            if (sujet.getCoDirecteur() == null || !sujet.getCoDirecteur().getId().equals(coDir.getId())) {
                long countDir = sujetRepository.countByProfesseur_Id(coDir.getId());
                long countCoDir = sujetRepository.countByCoDirecteur_Id(coDir.getId());
                if ((countDir + countCoDir) >= 12) {
                    throw new RuntimeException("Co-Director has reached the limit of 12 subjects");
                }
            }
            sujet.setCoDirecteur(coDir);
        } else {
            sujet.setCoDirecteur(null);
        }

        if (dto.getFormationDoctoraleId() != null) {
            com.example.demo.professeur.model.FormationDoctorale fd = formationDoctoraleRepository.findById(dto.getFormationDoctoraleId())
                    .orElseThrow(() -> new RuntimeException("Formation Doctorale not found"));
            sujet.setFormationDoctorale(fd);
        }

        sujet = sujetRepository.save(sujet);
        return toSujetDto(sujet);
    }

    // ================== DELETE SUJET ==================

    @Override
    public void deleteSujet(Long laboId, Long sujetId) {
        Sujet sujet = sujetRepository.findById(sujetId)
                .orElseThrow(() -> new RuntimeException("Sujet not found"));

        // Verify Subject belongs to a Prof in this Labo
        if (sujet.getProfesseur().getLaboratoire() == null || !sujet.getProfesseur().getLaboratoire().getId().equals(laboId)) {
            throw new RuntimeException("Sujet does not belong to this laboratory");
        }

        sujetRepository.delete(sujet);
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
            // Important: we return the UserAccount.id to match the frontend expectation 
            // of selecting from sec_user_account list
            dto.setProfesseurId(sujet.getProfesseur().getUserId());
            dto.setProfesseurGrade(sujet.getProfesseur().getGrade());
            dto.setProfesseurNumSom(sujet.getProfesseur().getNumSOM());
        }

        if (sujet.getCoDirecteur() != null) {
            dto.setCoDirecteurId(sujet.getCoDirecteur().getUserId());
            dto.setCoDirecteurNom(sujet.getCoDirecteur().getNom() + " " + sujet.getCoDirecteur().getPrenom());
        }

        if (sujet.getFormationDoctorale() != null) {
            dto.setFormationDoctoraleId(sujet.getFormationDoctorale().getId());
            dto.setFormationDoctoraleTitre(sujet.getFormationDoctorale().getTitre());
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

    @Override
    public java.util.List<java.util.Map<String, Object>> getFormationsByLabo(Long laboId) {
        String sql = "SELECT f.id, f.titre " +
                    "FROM professeur_formationdoctorale f " +
                    "JOIN professeur_laboratoire l ON f.ced_id = l.ced_id " +
                    "WHERE l.id = :laboId";
        
        List<Object[]> results = entityManager.createNativeQuery(sql)
                .setParameter("laboId", laboId)
                .getResultList();
        
        return results.stream().map(row -> {
            java.util.Map<String, Object> m = new java.util.HashMap<>();
            m.put("id", ((Number) row[0]).longValue());
            m.put("titre", (String) row[1]);
            return m;
        }).toList();
    }

    @Override
    public List<ProfessorDto> getProfesseursDuLabo(Long laboId) {
        String sql = "SELECT p.user_id, u.full_name, p.grade, l.nom_laboratoire, l.id as lab_id " +
                    "FROM professeur p " +
                    "JOIN sec_user_account u ON p.user_id = u.id " +
                    "LEFT JOIN professeur_laboratoire l ON p.labo_id = l.id " +
                    "WHERE p.labo_id = :laboId";
        
        List<Object[]> results = entityManager.createNativeQuery(sql)
                .setParameter("laboId", laboId)
                .getResultList();
        
        return results.stream().map(row -> {
            ProfessorDto dto = new ProfessorDto();
            dto.setId(((Number) row[0]).longValue());
            dto.setNomComplet((String) row[1]);
            dto.setGrade((String) row[2]);
            dto.setLaboratoire((String) row[3]);
            dto.setLaboId(row[4] != null ? ((Number) row[4]).longValue() : null);
            
            // Still need to count subjects - we can use the repository methods as they don't depend on the lab relation
            ProfesseurModel p = professeurRepository.findByUserId(dto.getId()).orElse(null);
            if (p != null) {
                long countDir = sujetRepository.countByProfesseur_Id(p.getId());
                long countCoDir = sujetRepository.countByCoDirecteur_Id(p.getId());
                dto.setSubjectCount(countDir + countCoDir);
            }
            
            return dto;
        }).toList();
    }

    @Override
    public List<ProfessorDto> getAllProfesseurs() {
        String sql = "SELECT u.id, u.full_name, p.grade, l.nom_laboratoire, l.id as lab_id " +
                    "FROM sec_user_account u " +
                    "JOIN sec_user_roles r ON u.id = r.user_id " +
                    "LEFT JOIN professeur p ON u.id = p.user_id " +
                    "LEFT JOIN professeur_laboratoire l ON p.labo_id = l.id " +
                    "WHERE r.role = 'PROFESSEUR' OR r.role = 'DIRECTEUR_LABO'";
        
        List<Object[]> results = entityManager.createNativeQuery(sql)
                .getResultList();
        
        return results.stream().map(row -> {
            ProfessorDto dto = new ProfessorDto();
            dto.setId(((Number) row[0]).longValue());
            dto.setNomComplet((String) row[1]);
            dto.setGrade((String) row[2]);
            dto.setLaboratoire((String) row[3]);
            dto.setLaboId(row[4] != null ? ((Number) row[4]).longValue() : null);
            
            ProfesseurModel p = professeurRepository.findByUserId(dto.getId()).orElse(null);
            if (p != null) {
                long countDir = sujetRepository.countByProfesseur_Id(p.getId());
                long countCoDir = sujetRepository.countByCoDirecteur_Id(p.getId());
                dto.setSubjectCount(countDir + countCoDir);
            }
            
            return dto;
        }).toList();
    }

    @Override
    public java.util.Map<String, Object> getMyLaboInfo(String email) {
        // Find user by email
        UserAccount user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        // Use native SQL query to get laboratory information directly
        String sql = "SELECT l.id, l.nom_laboratoire, l.initial " +
                    "FROM professeur p " +
                    "JOIN professeur_laboratoire l ON p.labo_id = l.id " +
                    "WHERE p.user_id = :userId";
        
        try {
            Object[] result = (Object[]) entityManager.createNativeQuery(sql)
                    .setParameter("userId", user.getId())
                    .getSingleResult();
            
            java.util.Map<String, Object> response = new java.util.HashMap<>();
            response.put("laboId", ((Number) result[0]).longValue());
            response.put("laboNom", (String) result[1]);
            response.put("laboInitial", (String) result[2]);
            response.put("professorId", user.getId());
            response.put("userId", user.getId());
            
            return response;
        } catch (jakarta.persistence.NoResultException e) {
            throw new RuntimeException("Director is not associated with any laboratory");
        }
    }

    // ================== COMMISSIONS IMPLEMENTATION ==================

    @Override
    public List<CommissionDto> getCommissionsByLabo(Long laboId) {
        List<Commission> commissions = commissionRepository.findByLaboratoire_Id(laboId);
        
        return commissions.stream().map(c -> {
            CommissionDto dto = new CommissionDto();
            dto.setId(c.getId());
            dto.setDateCommission(c.getDateCommission().toString());
            dto.setLieu(c.getLieu());
            dto.setHeure(c.getHeure().toString());
            dto.setLaboId(laboId);
            
            // Get Subjects
            List<Sujet> sujets = sujetRepository.findAll().stream()
                    .filter(s -> s.getCommission() != null && s.getCommission().getId().equals(c.getId()))
                    .toList();
            dto.setSujetIds(sujets.stream().map(Sujet::getId).toList());
            
            // Get Members
            List<CommissionProfesseur> cps = commissionProfesseurRepository.findAll().stream()
                    .filter(cp -> cp.getCommission().getId().equals(c.getId()))
                    .toList();
            dto.setMembreIds(cps.stream().map(cp -> cp.getProfesseur().getUserId()).toList());
            
            return dto;
        }).toList();
    }

    @Override
    @Transactional
    public CommissionDto createCommission(Long laboId, CommissionDto dto) {
        Laboratoire labo = laboratoireRepository.findById(laboId)
                .orElseThrow(() -> new RuntimeException("Laboratoire not found"));

        Commission c = new Commission();
        c.setLaboratoire(labo);
        c.setDateCommission(java.sql.Date.valueOf(dto.getDateCommission()));
        c.setHeure(java.time.LocalTime.parse(dto.getHeure()));
        c.setLieu(dto.getLieu());
        
        c = commissionRepository.save(c);
        
        // Assign Sujets
        if (dto.getSujetIds() != null) {
            for (Long sId : dto.getSujetIds()) {
                Sujet s = sujetRepository.findById(sId).orElse(null);
                if (s != null) s.setCommission(c);
            }
        }
        
        // Assign Members
        if (dto.getMembreIds() != null) {
            for (Long userId : dto.getMembreIds()) {
                ProfesseurModel p = professeurRepository.findByUserId(userId).orElse(null);
                if (p != null) {
                    CommissionProfesseur cp = new CommissionProfesseur();
                    cp.setCommission(c);
                    cp.setProfesseur(p);
                    commissionProfesseurRepository.save(cp);
                }
            }
        }
        
        dto.setId(c.getId());
        return dto;
    }

    @Override
    @Transactional
    public CommissionDto updateCommission(Long laboId, Long id, CommissionDto dto) {
        Commission c = commissionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Commission not found"));
        
        c.setDateCommission(java.sql.Date.valueOf(dto.getDateCommission()));
        c.setHeure(java.time.LocalTime.parse(dto.getHeure()));
        c.setLieu(dto.getLieu());
        
        c = commissionRepository.save(c);
        
        // Reset Sujets
        List<Sujet> oldSujets = sujetRepository.findAll().stream()
                .filter(s -> s.getCommission() != null && s.getCommission().getId().equals(id))
                .toList();
        for (Sujet s : oldSujets) {
            s.setCommission(null);
        }
        
        // Re-assign Sujets
        if (dto.getSujetIds() != null) {
            for (Long sId : dto.getSujetIds()) {
                Sujet s = sujetRepository.findById(sId).orElse(null);
                if (s != null) s.setCommission(c);
            }
        }
        
        // Reset Members
        List<CommissionProfesseur> oldCps = commissionProfesseurRepository.findAll().stream()
                .filter(cp -> cp.getCommission().getId().equals(id))
                .toList();
        commissionProfesseurRepository.deleteAll(oldCps);
        
        // Re-assign Members
        if (dto.getMembreIds() != null) {
            for (Long userId : dto.getMembreIds()) {
                ProfesseurModel p = professeurRepository.findByUserId(userId).orElse(null);
                if (p != null) {
                    CommissionProfesseur cp = new CommissionProfesseur();
                    cp.setCommission(c);
                    cp.setProfesseur(p);
                    commissionProfesseurRepository.save(cp);
                }
            }
        }
        
        return dto;
    }

    @Override
    @Transactional
    public void deleteCommission(Long laboId, Long id) {
        Commission c = commissionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Commission not found"));
        
        // Reset Sujets
        List<Sujet> oldSujets = sujetRepository.findAll().stream()
                .filter(s -> s.getCommission() != null && s.getCommission().getId().equals(id))
                .toList();
        for (Sujet s : oldSujets) {
            s.setCommission(null);
        }
        
        // Delete Members links
        List<CommissionProfesseur> oldCps = commissionProfesseurRepository.findAll().stream()
                .filter(cp -> cp.getCommission().getId().equals(id))
                .toList();
        commissionProfesseurRepository.deleteAll(oldCps);
        
        commissionRepository.delete(c);
    }
}
