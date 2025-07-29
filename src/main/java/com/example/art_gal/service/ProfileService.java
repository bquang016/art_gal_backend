package com.example.art_gal.service;

import com.example.art_gal.payload.ChangePasswordDto;
import com.example.art_gal.payload.ProfileDto;
import com.example.art_gal.payload.UserDto;

public interface ProfileService {
    UserDto getCurrentUser();
    UserDto updateProfile(ProfileDto profileDto);
    void changePassword(ChangePasswordDto changePasswordDto);
}