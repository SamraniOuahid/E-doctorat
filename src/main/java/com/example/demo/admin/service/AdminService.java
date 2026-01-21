package com.example.demo.admin.service;

import com.example.demo.admin.dto.*;
import com.example.demo.candidat.model.Candidat;
import com.example.demo.candidat.repository.CandidatRepository;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.professeur.model.ProfesseurModel;
import com.example.demo.professeur.model.Sujet;
import com.example.demo.candidat.repository.SujetRepository;
import com.example.demo.scolarite.model.EtatDossier;
import com.example.demo.security.user.AuthProvider;
import com.example.demo.security.user.Role;
import com.example.demo.security.user.UserAccount;
import com.example.demo.security.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service for admin operations.
 * Provides functionality for dashboard statistics, user management,
 * candidat management, and thesis subject management.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AdminService {

    private final UserRepository userRepository;
    private final CandidatRepository candidatRepository;
    private final SujetRepository sujetRepository;
    private final PasswordEncoder passwordEncoder;

    // ==================== DASHBOARD ====================

    public DashboardStatsDto getDashboardStats() {
        long totalUsers = userRepository.count();
        long totalCandidats = candidatRepository.count();
        long totalSujets = sujetRepository.count();

        long enabledUsers = userRepository.countByEnabled(true);
        long disabledUsers = totalUsers - enabledUsers;

        // Count pending candidatures (status EN_ATTENTE)
        long pendingCandidatures = candidatRepository.countByEtatDossier(EtatDossier.EN_ATTENTE);

        // Calculate Role Distribution
        List<ChartDataDto> roleDistribution = Arrays.stream(Role.values())
                .map(role -> new ChartDataDto(role.name(), userRepository.countByRole(role)))
                .filter(dto -> dto.getValue() > 0)
                .collect(Collectors.toList());

        // Mock recent registrations (In real app, query by date)
        List<ChartDataDto> recentRegistrations = List.of(
                new ChartDataDto("Jan", 10L),
                new ChartDataDto("Feb", 15L),
                new ChartDataDto("Mar", totalUsers));

        log.debug("Dashboard stats: users={}, candidats={}, sujets={}, pending={}",
                totalUsers, totalCandidats, totalSujets, pendingCandidatures);

        return new DashboardStatsDto(
                totalUsers,
                totalCandidats,
                totalSujets,
                pendingCandidatures,
                enabledUsers,
                disabledUsers,
                roleDistribution,
                recentRegistrations);
    }

    // ==================== USER MANAGEMENT ====================

    public PageResponse<UserResponseDto> getAllUsers(Pageable pageable) {
        Page<UserAccount> page = userRepository.findAll(pageable);
        List<UserResponseDto> dtos = page.getContent().stream()
                .map(this::mapToUserDto)
                .collect(Collectors.toList());
        return PageResponse.of(page, dtos);
    }

    public List<UserResponseDto> getAllUsersList() {
        return userRepository.findAll().stream()
                .map(this::mapToUserDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public UserResponseDto toggleUserStatus(Long userId) {
        UserAccount user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        boolean newStatus = !user.isEnabled();
        user.setEnabled(newStatus);
        UserAccount saved = userRepository.save(user);

        log.info("AUDIT: User {} status toggled to {} by admin", userId, newStatus ? "ENABLED" : "DISABLED");

        return mapToUserDto(saved);
    }

    @Transactional
    public UserResponseDto createUser(AdminCreateUserDto dto) {
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("Email déjà utilisé");
        }

        UserAccount user = UserAccount.builder()
                .email(dto.getEmail())
                .fullName(dto.getFullName())
                .password(passwordEncoder.encode(dto.getPassword()))
                .roles(dto.getRoles())
                .enabled(dto.isEnabled())
                .provider(AuthProvider.LOCAL)
                .build();

        UserAccount saved = userRepository.save(user);
        log.info("AUDIT: User {} created by admin", saved.getEmail());
        return mapToUserDto(saved);
    }

    @Transactional
    public UserResponseDto updateUser(Long id, AdminUpdateUserDto dto) {
        UserAccount user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

        if (dto.getFullName() != null)
            user.setFullName(dto.getFullName());
        if (dto.getRoles() != null)
            user.setRoles(dto.getRoles());
        if (dto.getEnabled() != null)
            user.setEnabled(dto.getEnabled());

        UserAccount saved = userRepository.save(user);
        log.info("AUDIT: User {} updated by admin", id);
        return mapToUserDto(saved);
    }

    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User", "id", id);
        }
        userRepository.deleteById(id);
        log.info("AUDIT: User {} deleted by admin", id);
    }

    // ==================== CANDIDAT MANAGEMENT ====================

    public PageResponse<CandidatResponseDto> getAllCandidats(Pageable pageable) {
        Page<Candidat> page = candidatRepository.findAll(pageable);
        List<CandidatResponseDto> dtos = page.getContent().stream()
                .map(this::mapToCandidatDto)
                .collect(Collectors.toList());
        return PageResponse.of(page, dtos);
    }

    public List<CandidatResponseDto> getAllCandidatsList() {
        return candidatRepository.findAll().stream()
                .map(this::mapToCandidatDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public CandidatResponseDto updateCandidatStatus(Long candidatId, EtatDossier status) {
        Candidat candidat = candidatRepository.findById(candidatId)
                .orElseThrow(() -> new ResourceNotFoundException("Candidat", "id", candidatId));

        EtatDossier oldStatus = candidat.getEtatDossier();
        candidat.setEtatDossier(status);
        Candidat saved = candidatRepository.save(candidat);

        log.info("AUDIT: Candidat {} status changed from {} to {} by admin",
                candidatId, oldStatus, status);

        return mapToCandidatDto(saved);
    }

    // ==================== SUJET MANAGEMENT ====================

    public List<SujetResponseDto> getAllSujets() {
        return sujetRepository.findAll().stream()
                .map(this::mapToSujetDto)
                .collect(Collectors.toList());
    }

    public PageResponse<SujetResponseDto> getAllSujetsPaginated(Pageable pageable) {
        Page<Sujet> page = sujetRepository.findAll(pageable);
        List<SujetResponseDto> dtos = page.getContent().stream()
                .map(this::mapToSujetDto)
                .collect(Collectors.toList());
        return PageResponse.of(page, dtos);
    }

    @Transactional
    public SujetResponseDto approveSujet(Long sujetId) {
        Sujet sujet = sujetRepository.findById(sujetId)
                .orElseThrow(() -> new ResourceNotFoundException("Sujet", "id", sujetId));

        sujet.setPublier(true);
        Sujet saved = sujetRepository.save(sujet);

        log.info("AUDIT: Sujet {} approved (published) by admin", sujetId);

        return mapToSujetDto(saved);
    }

    @Transactional
    public SujetResponseDto rejectSujet(Long sujetId) {
        Sujet sujet = sujetRepository.findById(sujetId)
                .orElseThrow(() -> new ResourceNotFoundException("Sujet", "id", sujetId));

        sujet.setPublier(false);
        Sujet saved = sujetRepository.save(sujet);

        log.info("AUDIT: Sujet {} rejected (unpublished) by admin", sujetId);

        return mapToSujetDto(saved);
    }

    @Transactional
    public void deleteCandidat(Long id) {
        if (!candidatRepository.existsById(id)) {
            throw new ResourceNotFoundException("Candidat", "id", id);
        }
        candidatRepository.deleteById(id);
        log.info("AUDIT: Candidat {} deleted by admin", id);
    }

    @Transactional
    public void deleteSujet(Long id) {
        if (!sujetRepository.existsById(id)) {
            throw new ResourceNotFoundException("Sujet", "id", id);
        }
        sujetRepository.deleteById(id);
        log.info("AUDIT: Sujet {} deleted by admin", id);
    }

    // ==================== MAPPERS ====================

    private UserResponseDto mapToUserDto(UserAccount user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .roles(user.getRoles())
                .enabled(user.isEnabled())
                .provider(user.getProvider() != null ? user.getProvider().name() : null)
                .build();
    }

    private CandidatResponseDto mapToCandidatDto(Candidat candidat) {
        return CandidatResponseDto.builder()
                .id(candidat.getId())
                .email(candidat.getEmail())
                .nom(candidat.getNomCandidatAr())
                .prenom(candidat.getPrenomCandidatAr())
                .cne(candidat.getCne())
                .cin(candidat.getCin())
                .telCandidat(candidat.getTelCandidat())
                .etatDossier(candidat.getEtatDossier())
                .build();
    }

    private SujetResponseDto mapToSujetDto(Sujet sujet) {
        SujetResponseDto.SujetResponseDtoBuilder builder = SujetResponseDto.builder()
                .id(sujet.getId())
                .titre(sujet.getTitre())
                .description(sujet.getDescription())
                .publier(sujet.isPublier());

        if (sujet.getProfesseur() != null) {
            // ProfesseurModel doesn't have nom/prenom, use userId to get from UserAccount
            // if needed
            // For now, we skip professor name as it requires additional query
            ProfesseurModel prof = sujet.getProfesseur();
            if (prof.getLaboratoire() != null) {
                builder.laboratoireNom(prof.getLaboratoire().getNomLaboratoire());
            }
        }

        if (sujet.getFormationDoctorale() != null) {
            builder.formationDoctoraleTitre(sujet.getFormationDoctorale().getTitre());
        }

        return builder.build();
    }
}
