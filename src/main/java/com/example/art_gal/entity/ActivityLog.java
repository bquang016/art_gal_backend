package com.example.art_gal.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import java.util.Date;

@Setter
@Getter
@Entity
@Table(name = "activity_logs")
public class ActivityLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String actor; // Tên người thực hiện, vd: "Quang Đẹp Trai (admin)"

    @Column(nullable = false)
    private String action; // Hành động, vd: "TẠO ĐƠN HÀNG", "CẬP NHẬT TRẠNH"

    @Column(columnDefinition = "TEXT")
    private String details; // Chi tiết hành động, vd: "Đã tạo đơn hàng #1 cho khách hàng Anh Nam"

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", updatable = false)
    private Date createdAt;
}