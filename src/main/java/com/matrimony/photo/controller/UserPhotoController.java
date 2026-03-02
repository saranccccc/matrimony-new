package com.matrimony.photo.controller;

import com.matrimony.auth.security.CustomUserDetails;
import com.matrimony.photo.dto.PhotoVisibility;
import com.matrimony.photo.entity.UserPhoto;
import com.matrimony.photo.service.UserPhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user/photos")
@RequiredArgsConstructor
public class UserPhotoController {

    private final UserPhotoService photoService;

    @PostMapping
    public ResponseEntity<?> upload(@AuthenticationPrincipal CustomUserDetails user, @RequestParam String photoUrl, @RequestParam PhotoVisibility visibility) {
        photoService.uploadPhoto(user.getUserId(), photoUrl, visibility);
        return ResponseEntity.ok("Photo uploaded, pending approval");
    }

    @PutMapping("/{photoId}/primary")
    public ResponseEntity<?> setPrimary(@AuthenticationPrincipal CustomUserDetails user, @PathVariable Long photoId) {
        photoService.setPrimaryPhoto(user.getUserId(), photoId);
        return ResponseEntity.ok("Primary photo updated");
    }

    @DeleteMapping("/{photoId}")
    public ResponseEntity<?> delete(@AuthenticationPrincipal CustomUserDetails user, @PathVariable Long photoId) {
        photoService.deletePhoto(user.getUserId(), photoId);
        return ResponseEntity.ok("Photo deleted");
    }

    @GetMapping
    public List<UserPhoto> list(@AuthenticationPrincipal CustomUserDetails user) {
        return photoService.getUserPhotos(user.getUserId());
    }
}
