package com.example.art_gal.controller;

import com.example.art_gal.payload.ChangePasswordDto;
import com.example.art_gal.payload.ProfileDto;
import com.example.art_gal.payload.UserDto;
import com.example.art_gal.service.ProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserDto> getCurrentUserProfile() {
        return ResponseEntity.ok(profileService.getCurrentUser());
    }

    @PutMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserDto> updateCurrentUserProfile(@RequestBody ProfileDto profileDto) {
        return ResponseEntity.ok(profileService.updateProfile(profileDto));
    }

    @PostMapping("/change-password")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> changeCurrentUserPassword(@RequestBody ChangePasswordDto changePasswordDto) {
        profileService.changePassword(changePasswordDto);
        return ResponseEntity.ok("Đổi mật khẩu thành công.");
    }
}