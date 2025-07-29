package com.example.art_gal.payload;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class PaintingDto {
    private Long id;
    private String name;
    private String material;
    private String image;
    private String size;
    private String description;
    private BigDecimal importPrice;
    private BigDecimal sellingPrice;
    private String status;
    private Long artistId;
    private Long categoryId;
}