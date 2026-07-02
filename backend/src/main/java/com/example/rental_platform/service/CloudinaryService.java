

package com.example.rental_platform.service;

import java.io.IOException;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CloudinaryService {

    private final Cloudinary cloudinary;

    // =========================
    // UPLOAD IMAGE
    // =========================
    public String uploadImage(MultipartFile file) {

        try {

            Map<?, ?> uploadResult = cloudinary.uploader()
                    .upload(file.getBytes(), Map.of());

            return uploadResult.get("secure_url").toString();

        } catch (IOException e) {

            throw new RuntimeException("Image upload failed", e);
        }
    }

    // =========================
    // DELETE IMAGE
    // =========================
    public void deleteImage(String imageUrl) {

        try {

            String publicId = extractPublicId(imageUrl);

            cloudinary.uploader().destroy(publicId, Map.of());

        } catch (Exception e) {

            throw new RuntimeException("Image deletion failed", e);
        }
    }

    // =========================
    // EXTRACT PUBLIC ID
    // =========================
    private String extractPublicId(String imageUrl) {

        String[] parts = imageUrl.split("/");

        String fileName = parts[parts.length - 1];

        return fileName.substring(0, fileName.lastIndexOf("."));
    }
}