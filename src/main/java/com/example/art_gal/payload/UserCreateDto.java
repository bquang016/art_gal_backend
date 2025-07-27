package com.example.art_gal.payload;

import lombok.Data;
import java.util.Set;

@Data
public class UserCreateDto {
    private String name;
    private String username;
    private String email;
    private String password;
    private Set<String> roles; // VD: ["ADMIN", "NHANVIEN"]
}