package com.example.demo.admin.service;

import com.example.demo.admin.dto.DashboardStatsDto;
import com.example.demo.candidat.model.Candidat;
import com.example.demo.candidat.repository.CandidatRepository;
import com.example.demo.professeur.model.Sujet;
import com.example.demo.candidat.repository.SujetRepository;
import com.example.demo.security.user.UserAccount;
import com.example.demo.security.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final CandidatRepository candidatRepository;
    private final SujetRepository sujetRepository;

    public DashboardStatsDto getDashboardStats() {
        long totalUsers = userRepository.count();
        long totalCandidats = candidatRepository.count();
        long totalSujets = sujetRepository.count();

        long enabledUsers = userRepository.findAll().stream()
                .filter(UserAccount::isEnabled)
                .count();
        long disabledUsers = totalUsers - enabledUsers;

        return new DashboardStatsDto(
                totalUsers,
                totalCandidats,
                totalSujets,
                0, // TODO: implement pending candidatures count
                enabledUsers,
                disabledUsers);
    }

    public Page<UserAccount> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public List<UserAccount> getAllUsersList() {
        return userRepository.findAll();
    }

    @Transactional
    public UserAccount toggleUserStatus(Long userId) {
        UserAccount user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setEnabled(!user.isEnabled());
        return userRepository.save(user);
    }

    public Page<Candidat> getAllCandidats(Pageable pageable) {
        return candidatRepository.findAll(pageable);
    }

    public List<Candidat> getAllCandidatsList() {
        return candidatRepository.findAll();
    }

    @Transactional
    public Candidat updateCandidatStatus(Long candidatId, com.example.demo.scolarite.model.EtatDossier status) {
        Candidat candidat = candidatRepository.findById(candidatId)
                .orElseThrow(() -> new RuntimeException("Candidat not found"));
        candidat.setEtatDossier(status);
        return candidatRepository.save(candidat);
    }

    public List<Sujet> getAllSujets() {
        return sujetRepository.findAll();
    }

    @Transactional
    public Sujet approveSujet(Long sujetId) {
        Sujet sujet = sujetRepository.findById(sujetId)
                .orElseThrow(() -> new RuntimeException("Sujet not found"));
        sujet.setPublier(true);
        return sujetRepository.save(sujet);
    }

    @Transactional
    public Sujet rejectSujet(Long sujetId) {
        Sujet sujet = sujetRepository.findById(sujetId)
                .orElseThrow(() -> new RuntimeException("Sujet not found"));
        sujet.setPublier(false);
        return sujetRepository.save(sujet);
    }
}
