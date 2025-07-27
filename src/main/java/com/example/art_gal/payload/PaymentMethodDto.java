package com.example.art_gal.payload;

import lombok.Data;

@Data
public class PaymentMethodDto {
    private Long id;
    private String methodKey;
    private String name;
    private String description;
    private boolean enabled;
    private boolean configurable;
    private String qrCodeImageUrl;
}