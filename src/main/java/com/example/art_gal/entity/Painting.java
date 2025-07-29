package com.example.art_gal.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@Entity
@Table(name = "paintings")
public class Painting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String material;
    private String image;
    private String size; // Kích thước tranh, vd: "80x120 cm"
    
    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(precision = 19, scale = 4)
    private BigDecimal importPrice;

    @Column(precision = 19, scale = 4)
    private BigDecimal sellingPrice;

    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artist_id", nullable = false)
    private Artist artist;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
}