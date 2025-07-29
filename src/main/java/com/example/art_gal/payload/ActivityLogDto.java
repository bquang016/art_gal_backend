package com.example.art_gal.payload;

import lombok.Data;
import java.util.Date;

@Data
public class ActivityLogDto {
    private Long id;
    private String actor;
    private String action;
    private String details;
    private Date createdAt;
}