package com.example.art_gal.service;

import com.example.art_gal.payload.LoginDto;
import com.example.art_gal.payload.JWTAuthResponse;

public interface AuthService {
    JWTAuthResponse login(LoginDto loginDto);
}