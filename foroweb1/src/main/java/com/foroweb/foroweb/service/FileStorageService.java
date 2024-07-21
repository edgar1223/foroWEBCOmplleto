package com.foroweb.foroweb.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;

    public FileStorageService() {
        this.fileStorageLocation = Paths.get("upload-dir").toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    public String storeFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Failed to store empty file.");
        }

        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path targetLocation = this.fileStorageLocation.resolve(fileName);

        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

        // Devolver una ruta relativa en lugar de una ruta absoluta
        return "/upload-dir/" + fileName;
    }
    public String storeFile2(Path filePath) throws IOException {
        String fileName = System.currentTimeMillis() + "_" + filePath.getFileName().toString();
        Path targetLocation = this.fileStorageLocation.resolve(fileName);

        Files.copy(filePath, targetLocation, StandardCopyOption.REPLACE_EXISTING);

        // Devolver una ruta relativa en lugar de una ruta absoluta
        return "/upload-dir/" + fileName;
    }
}
