package com.example.art_gal.service.impl;

import com.example.art_gal.entity.ActivityLog;
import com.example.art_gal.entity.User;
import com.example.art_gal.payload.ActivityLogDto;
import com.example.art_gal.repository.ActivityLogRepository;
import com.example.art_gal.service.ActivityLogService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ActivityLogServiceImpl implements ActivityLogService {

    private final ActivityLogRepository activityLogRepository;

    public ActivityLogServiceImpl(ActivityLogRepository activityLogRepository) {
        this.activityLogRepository = activityLogRepository;
    }

    @Override
    public void logActivity(User actor, String action, String details) {
        ActivityLog log = new ActivityLog();
        String actorName = String.format("%s (%s)", actor.getName(), actor.getUsername());
        
        log.setActor(actorName);
        log.setAction(action);
        log.setDetails(details);
        
        activityLogRepository.save(log);
    }

    @Override
    public List<ActivityLogDto> getAllLogs() {
        return activityLogRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private ActivityLogDto mapToDto(ActivityLog log) {
        ActivityLogDto dto = new ActivityLogDto();
        dto.setId(log.getId());
        dto.setActor(log.getActor());
        dto.setAction(log.getAction());
        dto.setDetails(log.getDetails());
        dto.setCreatedAt(log.getCreatedAt());
        return dto;
    }
}