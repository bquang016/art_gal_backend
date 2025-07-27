package com.example.art_gal.payload;

import lombok.Data;
import java.util.Set;

@Data
public class UserDto {
    private Long id;
    private String name;
    private String username;
    private String email;
    private Set<String> roles;
    // Thêm trường status nếu bạn có trong Entity User
    // private String status; 
}