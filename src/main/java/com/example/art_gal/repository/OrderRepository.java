package com.example.art_gal.repository;

import com.example.art_gal.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // ✅ THÊM CÁC PHƯƠNG THỨC NÀY ĐỂ SỬA LỖI
    List<Order> findByCustomerId(Long customerId);

    @Query("SELECT SUM(o.totalAmount) FROM Order o WHERE o.status = 'Hoàn thành'")
    BigDecimal sumTotalAmount();

    List<Order> findTop7ByOrderByOrderDateDesc();
}