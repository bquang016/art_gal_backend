package com.example.art_gal.controller;

import com.example.art_gal.payload.ImportSlipCreateDto;
import com.example.art_gal.payload.ImportSlipDto;
import com.example.art_gal.service.ImportSlipService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/import-slips")
public class ImportSlipController {

    private final ImportSlipService importSlipService;

    public ImportSlipController(ImportSlipService importSlipService) {
        this.importSlipService = importSlipService;
    }

    /**
     * API để tạo một phiếu nhập hàng mới.
     * Yêu cầu quyền ADMIN hoặc NHANVIEN.
     * @param createDto Dữ liệu phiếu nhập từ client.
     * @return Thông báo tạo thành công.
     */
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_NHANVIEN')")
    public ResponseEntity<String> createImportSlip(@RequestBody ImportSlipCreateDto createDto) {
        importSlipService.createImportSlip(createDto);
        return new ResponseEntity<>("Import Slip created successfully.", HttpStatus.CREATED);
    }

    /**
     * API để lấy danh sách tất cả các phiếu nhập đã tạo.
     * Yêu cầu người dùng phải đăng nhập.
     * @return Danh sách các phiếu nhập.
     */
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ImportSlipDto>> getAllImportSlips() {
        return ResponseEntity.ok(importSlipService.getAllImportSlips());
    }
}