package com.example.demo.admin.controller;

import com.example.demo.admin.dto.DashboardStatsDto;
import com.example.demo.admin.service.AdminService;
import com.example.demo.candidat.model.Candidat;
import com.example.demo.professeur.model.Sujet;
import com.example.demo.scolarite.model.EtatDossier;
import com.example.demo.security.user.UserAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/stats")
    public ResponseEntity<DashboardStatsDto> getDashboardStats() {
        return ResponseEntity.ok(adminService.getDashboardStats());
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserAccount>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {
        if (size > 50) {
            // Return all users if requesting large page
            return ResponseEntity.ok(adminService.getAllUsersList());
        }
        Page<UserAccount> users = adminService.getAllUsers(PageRequest.of(page, size));
        return ResponseEntity.ok(users.getContent());
    }

    @PutMapping("/users/{id}/toggle-status")
    public ResponseEntity<UserAccount> toggleUserStatus(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.toggleUserStatus(id));
    }

    @GetMapping("/candidats")
    public ResponseEntity<List<Candidat>> getAllCandidats(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {
        if (size > 50) {
            return ResponseEntity.ok(adminService.getAllCandidatsList());
        }
        Page<Candidat> candidats = adminService.getAllCandidats(PageRequest.of(page, size));
        return ResponseEntity.ok(candidats.getContent());
    }

    @PutMapping("/candidats/{id}/status")
    public ResponseEntity<Candidat> updateCandidatStatus(
            @PathVariable Long id,
            @RequestParam EtatDossier status) {
        return ResponseEntity.ok(adminService.updateCandidatStatus(id, status));
    }

    @GetMapping("/sujets")
    public ResponseEntity<List<Sujet>> getAllSujets() {
        return ResponseEntity.ok(adminService.getAllSujets());
    }

    @PutMapping("/sujets/{id}/approve")
    public ResponseEntity<Sujet> approveSujet(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.approveSujet(id));
    }

    @PutMapping("/sujets/{id}/reject")
    public ResponseEntity<Sujet> rejectSujet(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.rejectSujet(id));
    }
}
