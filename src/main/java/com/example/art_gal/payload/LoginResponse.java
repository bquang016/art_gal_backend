package com.example.art_gal.payload;

import java.util.List;

public class LoginResponse {
    private String accessToken;
    private UserDetailsResponse userDetails;

    public LoginResponse(String accessToken, UserDetailsResponse userDetails) {
        this.accessToken = accessToken;
        this.userDetails = userDetails;
    }

    // Getters and Setters
    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public UserDetailsResponse getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(UserDetailsResponse userDetails) {
        this.userDetails = userDetails;
    }
}