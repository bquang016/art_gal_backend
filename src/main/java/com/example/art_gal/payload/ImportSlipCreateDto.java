package com.example.art_gal.payload;

import lombok.Data;
import java.util.List;

@Data
public class ImportSlipCreateDto {
    private Long artistId;
    private Long userId;
    private String notes;
    private List<ImportSlipItemDto> items;
}