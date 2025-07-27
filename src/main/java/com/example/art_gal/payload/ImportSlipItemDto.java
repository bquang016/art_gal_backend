package com.example.art_gal.payload;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ImportSlipItemDto {
    private String name;
    private BigDecimal importPrice;
    private BigDecimal sellingPrice;
    private Long categoryId;
    private String material;
}