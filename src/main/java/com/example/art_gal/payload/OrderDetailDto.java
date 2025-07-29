package com.example.art_gal.payload;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class OrderDetailDto {
    private Long paintingId; // ID của bức tranh được mua
    private int quantity;
    private String paintingName;
    private BigDecimal price; // Giá bán tại thời điểm tạo đơn
}