package com.example.art_gal.payload;

import java.util.List;

public class UserDetailsResponse {
    private String name;
    private List<String> roles;

    public UserDetailsResponse(String name, List<String> roles) {
        this.name = name;
        this.roles = roles;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}