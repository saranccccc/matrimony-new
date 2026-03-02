package com.matrimony.photo.controller;

import com.matrimony.photo.dto.PhotoStatus;
import com.matrimony.photo.service.AdminPhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/photos")
@RequiredArgsConstructor
public class AdminPhotoController {

    private final AdminPhotoService adminPhotoService;

    @PutMapping("/{photoId}/status")
    public ResponseEntity<?> updateStatus(@PathVariable Long photoId, @RequestParam PhotoStatus status) {
        adminPhotoService.updatePhotoStatus(photoId, status);
        return ResponseEntity.ok("Photo status updated");
    }
}
