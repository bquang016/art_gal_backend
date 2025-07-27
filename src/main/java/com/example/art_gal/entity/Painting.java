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

    private String material; // Chất liệu
    private String image; // URL to the image

    @Column(precision = 19, scale = 4) // Phù hợp cho số tiền lớn
    private BigDecimal importPrice;

    @Column(precision = 19, scale = 4)
    private BigDecimal sellingPrice;

    private String status; // e.g., "Đang bán", "Đã bán", "Dừng bán"

    // Mối quan hệ: Nhiều Painting thuộc về một Artist
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artist_id", nullable = false)
    private Artist artist;

    // Mối quan hệ: Nhiều Painting thuộc về một Category
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
}