package com.example.art_gal.repository;

import com.example.art_gal.entity.Painting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaintingRepository extends JpaRepository<Painting, Long> {
    
    boolean existsByNameAndArtistId(String name, Long artistId);

    // ✅ THÊM PHƯƠC THỨC NÀY ĐỂ SỬA LỖI
    long countByStatus(String status);
}