package com.example.art_gal.payload;

import lombok.Data;
import java.util.List;

@Data
public class ImportSlipCreateDto {
    private Long artistId;
    private List<ImportSlipItemDto> items;
}