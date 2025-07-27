package com.example.art_gal.payload;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ImportSlipDetailDto {
    private Long paintingId;
    private String paintingName;
    private BigDecimal price;
}