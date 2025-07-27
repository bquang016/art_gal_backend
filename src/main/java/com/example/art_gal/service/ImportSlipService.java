package com.example.art_gal.service;

import com.example.art_gal.payload.ImportSlipCreateDto; // Thêm import này
import com.example.art_gal.payload.ImportSlipDto;
import java.util.List;

public interface ImportSlipService {
    
    // Thêm phương thức này vào
    void createImportSlip(ImportSlipCreateDto createDto); 
    
    List<ImportSlipDto> getAllImportSlips();
}