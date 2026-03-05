package com.matrimony.photo.controller;

import com.matrimony.auth.security.CustomUserDetails;
import com.matrimony.photo.dto.PhotoResponse;
import com.matrimony.photo.dto.PhotoVisibility;
import com.matrimony.photo.entity.UserPhoto;
import com.matrimony.photo.service.UserPhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user/photos")
@RequiredArgsConstructor
public class UserPhotoController {

    private final UserPhotoService photoService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PhotoResponse> uploadToS3(@AuthenticationPrincipal CustomUserDetails user, @RequestParam("file") MultipartFile file, @RequestParam PhotoVisibility visibility) {
        return ResponseEntity.ok(photoService.uploadPhotoToS3(user.getUserId(), file, visibility));
    }

/*    @PostMapping
    public ResponseEntity<?> upload(@AuthenticationPrincipal CustomUserDetails user, @RequestParam String photoUrl, @RequestParam PhotoVisibility visibility) {
        photoService.uploadPhoto(user.getUserId(), photoUrl, visibility);
        return ResponseEntity.ok("Photo uploaded, pending approval");
    }*/

    @PutMapping("/{photoId}/primary")
    public ResponseEntity<PhotoResponse> setPrimary(@AuthenticationPrincipal CustomUserDetails user, @PathVariable Long photoId) {
        return ResponseEntity.ok(photoService.setPrimaryPhoto(user.getUserId(), photoId));
    }

    @DeleteMapping("/{photoId}")
    public ResponseEntity<PhotoResponse> delete(@AuthenticationPrincipal CustomUserDetails user, @PathVariable Long photoId) {
        return ResponseEntity.ok(photoService.deletePhoto(user.getUserId(), photoId));
    }

    @GetMapping
    public ResponseEntity<List<UserPhoto>> list(@AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.ok(photoService.getUserPhotos(user.getUserId()));
    }
}
