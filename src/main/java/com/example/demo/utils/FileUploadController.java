package com.example.demo.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/upload")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class FileUploadController {

    private final FileStorageService fileStorageService;

    /**
     * Upload profile photo
     * POST /api/upload/photo
     */
    @PostMapping("/photo")
    public ResponseEntity<?> uploadPhoto(@RequestParam("file") MultipartFile file) {
        try {
            String filePath = fileStorageService.storePhoto(file);

            Map<String, String> response = new HashMap<>();
            response.put("filePath", filePath);
            response.put("message", "Photo uploadée avec succès");

            return ResponseEntity.ok(response);
        } catch (IOException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    /**
     * Upload CV
     * POST /api/upload/cv
     */
    @PostMapping("/cv")
    public ResponseEntity<?> uploadCv(@RequestParam("file") MultipartFile file) {
        try {
            String filePath = fileStorageService.storeCv(file);

            Map<String, String> response = new HashMap<>();
            response.put("filePath", filePath);
            response.put("message", "CV uploadé avec succès");

            return ResponseEntity.ok(response);
        } catch (IOException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
}
