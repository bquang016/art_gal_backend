package com.example.art_gal.service;

import com.example.art_gal.entity.User;
import com.example.art_gal.payload.ActivityLogDto;
import java.util.List;

public interface ActivityLogService {
    void logActivity(User actor, String action, String details);
    List<ActivityLogDto> getAllLogs();
}