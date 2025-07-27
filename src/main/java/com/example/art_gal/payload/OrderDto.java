package com.example.art_gal.payload;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

@Data
public class OrderDto {
    private Long id;
    private Date orderDate;
    private BigDecimal totalAmount;
    private String status;

    // Thông tin liên quan
    private Long customerId;
    private String customerName; // Thêm để hiển thị
    private Long userId;
    private String userName; // Thêm để hiển thị

    // Danh sách các sản phẩm trong đơn hàng
    private Set<OrderDetailDto> orderDetails;
}