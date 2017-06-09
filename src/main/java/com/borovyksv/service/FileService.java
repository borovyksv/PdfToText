package com.borovyksv.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileService {

    @Value("${upload.file.storage}")
    private String uploadFileStorage;

    public boolean store(MultipartFile file, String id) {
        Path target = Paths.get(uploadFileStorage).resolve(id+".pdf");
        try {
            if (Files.exists(target)) {
                System.out.println("File: " + file.getOriginalFilename() + " exists");
                return false;
            } else {
                Files.copy(file.getInputStream(), target);
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }
}
