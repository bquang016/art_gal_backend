package com.example.art_gal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories; // <- THÊM DÒNG IMPORT

@SpringBootApplication
@EnableJpaRepositories("com.example.art_gal.repository") // <- THÊM DÒNG NÀY
public class ArtGalleryManagementSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(ArtGalleryManagementSystemApplication.class, args);
    }
}