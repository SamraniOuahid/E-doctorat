package com.example.demo.candidat.controller;

// Correct Import for DiplomeDto if it is in a different package
// Checked previously: com.example.demo.candidat.dto.DiplomeDto
import com.example.demo.candidat.dto.DiplomeDto;
import com.example.demo.candidat.model.Diplome;
import com.example.demo.candidat.service.CandidatService;
import com.example.demo.candidat.service.DiplomeService;
import com.example.demo.candidat.model.Candidat;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/diplomes")
@RequiredArgsConstructor
public class DiplomeController {

    private final DiplomeService diplomeService;
    private final CandidatService candidatService;

    @GetMapping
    public ResponseEntity<List<Diplome>> getMyDiplomes() {
        Candidat currentCandidat = candidatService.getCurrentCandidat();
        return ResponseEntity.ok(diplomeService.getDiplomesByCandidat(currentCandidat.getId()));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Diplome> addDiplome(
            @RequestPart("diplome") DiplomeDto diplomeDto,
            @RequestPart(value = "file", required = false) MultipartFile file) throws IOException {

        Candidat currentCandidat = candidatService.getCurrentCandidat();
        Diplome saved = diplomeService.addDiplome(currentCandidat.getId(), diplomeDto, file);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDiplome(@PathVariable Long id) {
        Candidat currentCandidat = candidatService.getCurrentCandidat();
        diplomeService.deleteDiplome(id, currentCandidat.getId());
        return ResponseEntity.noContent().build();
    }
}
