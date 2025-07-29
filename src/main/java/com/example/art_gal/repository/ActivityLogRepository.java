package com.example.art_gal.repository;

import com.example.art_gal.entity.ActivityLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {
    // Sắp xếp nhật ký theo thời gian mới nhất
    List<ActivityLog> findAllByOrderByCreatedAtDesc();
}