package com.example.art_gal.repository;

import com.example.art_gal.entity.Painting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaintingRepository extends JpaRepository<Painting, Long> {
}