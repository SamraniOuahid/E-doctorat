package com.example.demo.admin.controller;

import com.example.demo.admin.dto.*;
import com.example.demo.admin.service.AdminService;
import com.example.demo.scolarite.model.EtatDossier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for admin operations.
 * All endpoints require ADMIN role.
 */
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Slf4j
public class AdminController {

    private final AdminService adminService;

    // ==================== DASHBOARD ====================

    @GetMapping("/stats")
    public ResponseEntity<DashboardStatsDto> getDashboardStats() {
        log.debug("Admin requesting dashboard stats");
        return ResponseEntity.ok(adminService.getDashboardStats());
    }

    // ==================== USER MANAGEMENT ====================

    @GetMapping("/users")
    public ResponseEntity<PageResponse<UserResponseDto>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        Sort.Direction direction = sortDir.equalsIgnoreCase("desc")
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;
        PageRequest pageRequest = PageRequest.of(page, Math.min(size, 100), Sort.by(direction, sortBy));

        return ResponseEntity.ok(adminService.getAllUsers(pageRequest));
    }

    @GetMapping("/users/all")
    public ResponseEntity<List<UserResponseDto>> getAllUsersList() {
        return ResponseEntity.ok(adminService.getAllUsersList());
    }

    @PutMapping("/users/{id}/toggle-status")
    public ResponseEntity<UserResponseDto> toggleUserStatus(@PathVariable Long id) {
        log.info("Admin toggling status for user {}", id);
        return ResponseEntity.ok(adminService.toggleUserStatus(id));
    }

    @PostMapping("/users")
    public ResponseEntity<UserResponseDto> createUser(@RequestBody AdminCreateUserDto dto) {
        log.info("Admin creating user: {}", dto.getEmail());
        return ResponseEntity.ok(adminService.createUser(dto));
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable Long id, @RequestBody AdminUpdateUserDto dto) {
        log.info("Admin updating user: {}", id);
        return ResponseEntity.ok(adminService.updateUser(id, dto));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        log.info("Admin deleting user: {}", id);
        adminService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    // ==================== CANDIDAT MANAGEMENT ====================

    @GetMapping("/candidats")
    public ResponseEntity<PageResponse<CandidatResponseDto>> getAllCandidats(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        Sort.Direction direction = sortDir.equalsIgnoreCase("desc")
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;
        PageRequest pageRequest = PageRequest.of(page, Math.min(size, 100), Sort.by(direction, sortBy));

        return ResponseEntity.ok(adminService.getAllCandidats(pageRequest));
    }

    @GetMapping("/candidats/all")
    public ResponseEntity<List<CandidatResponseDto>> getAllCandidatsList() {
        return ResponseEntity.ok(adminService.getAllCandidatsList());
    }

    @PutMapping("/candidats/{id}/status")
    public ResponseEntity<CandidatResponseDto> updateCandidatStatus(
            @PathVariable Long id,
            @RequestParam EtatDossier status) {
        log.info("Admin updating candidat {} status to {}", id, status);
        return ResponseEntity.ok(adminService.updateCandidatStatus(id, status));
    }

    @DeleteMapping("/candidats/{id}")
    public ResponseEntity<Void> deleteCandidat(@PathVariable Long id) {
        log.info("Admin deleting candidat: {}", id);
        adminService.deleteCandidat(id);
        return ResponseEntity.noContent().build();
    }

    // ==================== SUJET MANAGEMENT ====================

    @GetMapping("/sujets")
    public ResponseEntity<List<SujetResponseDto>> getAllSujets() {
        return ResponseEntity.ok(adminService.getAllSujets());
    }

    @GetMapping("/sujets/paginated")
    public ResponseEntity<PageResponse<SujetResponseDto>> getAllSujetsPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        Sort.Direction direction = sortDir.equalsIgnoreCase("desc")
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;
        PageRequest pageRequest = PageRequest.of(page, Math.min(size, 100), Sort.by(direction, sortBy));

        return ResponseEntity.ok(adminService.getAllSujetsPaginated(pageRequest));
    }

    @PutMapping("/sujets/{id}/approve")
    public ResponseEntity<SujetResponseDto> approveSujet(@PathVariable Long id) {
        log.info("Admin approving sujet {}", id);
        return ResponseEntity.ok(adminService.approveSujet(id));
    }

    @PutMapping("/sujets/{id}/reject")
    public ResponseEntity<SujetResponseDto> rejectSujet(@PathVariable Long id) {
        log.info("Admin rejecting sujet {}", id);
        return ResponseEntity.ok(adminService.rejectSujet(id));
    }

    @DeleteMapping("/sujets/{id}")
    public ResponseEntity<Void> deleteSujet(@PathVariable Long id) {
        log.info("Admin deleting sujet: {}", id);
        adminService.deleteSujet(id);
        return ResponseEntity.noContent().build();
    }
}
