package com.example.art_gal.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "payment_methods")
public class PaymentMethod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String methodKey; // e.g., "cash", "qr_bank", "momo"

    @Column(nullable = false)
    private String name;

    private String description;
    
    private boolean enabled;
    
    private boolean configurable;

    // Sử dụng một trường duy nhất để lưu URL ảnh QR cho TẤT CẢ các phương thức QR
    @Column(length = 512)
    private String qrCodeImageUrl;
}