package com.example.art_gal.payload;

import lombok.Data;

@Data
public class ArtistDto {
    private Long id;
    private String name;
    private String phone;
    private String email;
    private String address;
    private String status;
}