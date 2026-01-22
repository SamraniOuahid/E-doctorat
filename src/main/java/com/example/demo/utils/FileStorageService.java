package com.example.demo.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {

    @Value("${file.upload-dir:uploads}")
    private String uploadDir;

    @Value("${file.photo-dir:photos}")
    private String photoDir;

    @Value("${file.cv-dir:cvs}")
    private String cvDir;

    @Value("${file.max-photo-size:2097152}") // 2MB default
    private long maxPhotoSize;

    @Value("${file.max-cv-size:5242880}") // 5MB default
    private long maxCvSize;

    /**
     * Store a profile photo
     */
    public String storePhoto(MultipartFile file) throws IOException {
        validatePhoto(file);
        return storeFile(file, photoDir);
    }

    /**
     * Store a CV file
     */
    public String storeCv(MultipartFile file) throws IOException {
        validateCv(file);
        return storeFile(file, cvDir);
    }

    /**
     * Generic file storage method
     */
    private String storeFile(MultipartFile file, String subDir) throws IOException {
        // Create directories if they don't exist
        Path uploadPath = Paths.get(uploadDir, subDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Generate unique filename
        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        String uniqueFilename = UUID.randomUUID().toString() + extension;

        // Store file
        Path targetLocation = uploadPath.resolve(uniqueFilename);
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

        // Return relative path
        return subDir + "/" + uniqueFilename;
    }

    /**
     * Delete a file
     */
    public void deleteFile(String filePath) {
        try {
            Path path = Paths.get(uploadDir, filePath);
            Files.deleteIfExists(path);
        } catch (IOException e) {
            // Log error but don't throw - deletion is not critical
            System.err.println("Failed to delete file: " + filePath);
        }
    }

    /**
     * Validate photo file
     */
    private void validatePhoto(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IOException("Le fichier est vide");
        }

        if (file.getSize() > maxPhotoSize) {
            throw new IOException("La taille de la photo ne doit pas dépasser 2MB");
        }

        String contentType = file.getContentType();
        if (contentType == null ||
                !(contentType.equals("image/jpeg") ||
                        contentType.equals("image/jpg") ||
                        contentType.equals("image/png"))) {
            throw new IOException("Seuls les fichiers JPG et PNG sont acceptés pour les photos");
        }
    }

    /**
     * Validate CV file
     */
    private void validateCv(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IOException("Le fichier est vide");
        }

        if (file.getSize() > maxCvSize) {
            throw new IOException("La taille du CV ne doit pas dépasser 5MB");
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.equals("application/pdf")) {
            throw new IOException("Seuls les fichiers PDF sont acceptés pour les CV");
        }
    }

    /**
     * Store a Diploma file
     */
    public String storeDiplome(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IOException("Le fichier est vide");
        }
        return storeFile(file, "diplomes");
    }

    /**
     * Store a Research Proposal file
     */
    public String storeProjetRecherche(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IOException("Le fichier est vide");
        }
        // Validate PDF type
        String contentType = file.getContentType();
        if (contentType == null || !contentType.equals("application/pdf")) {
            throw new IOException("Seuls les fichiers PDF sont acceptés pour le projet de recherche");
        }
        return storeFile(file, "projets");
    }
}
