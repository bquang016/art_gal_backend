package com.example.art_gal.payload;

import lombok.Data;
import java.util.Set;

@Data
public class UpdateUserDto {
    private String name;
    private String email;
    private Set<String> roles;
}