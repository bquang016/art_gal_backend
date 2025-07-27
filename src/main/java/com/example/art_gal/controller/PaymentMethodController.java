package com.example.art_gal.controller;

import com.example.art_gal.entity.PaymentMethod;
import com.example.art_gal.exception.ResourceNotFoundException;
import com.example.art_gal.payload.PaymentMethodDto;
import com.example.art_gal.repository.PaymentMethodRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/payment-methods")
public class PaymentMethodController {

    private final PaymentMethodRepository paymentMethodRepository;

    public PaymentMethodController(PaymentMethodRepository paymentMethodRepository) {
        this.paymentMethodRepository = paymentMethodRepository;
    }

    /**
     * API để lấy danh sách tất cả các phương thức thanh toán.
     * @return Danh sách các phương thức thanh toán.
     */
    @GetMapping
    public List<PaymentMethodDto> getAllPaymentMethods() {
        return paymentMethodRepository.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
    }

    /**
     * API để lấy danh sách các phương thức thanh toán bằng QR đang hoạt động.
     * @return Danh sách các phương thức QR đang bật.
     */
    @GetMapping("/active-qr")
    public List<PaymentMethodDto> getActiveQrMethods() {
        return paymentMethodRepository.findAll().stream()
                .filter(method -> method.getMethodKey().contains("qr") && method.isEnabled())
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
    
    /**
     * API để cập nhật thông tin một phương thức thanh toán (bật/tắt, cấu hình).
     * Yêu cầu quyền ADMIN.
     * @param id ID của phương thức cần cập nhật.
     * @param dto Dữ liệu mới.
     * @return Phương thức thanh toán sau khi đã cập nhật.
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<PaymentMethodDto> updatePaymentMethod(@PathVariable Long id, @RequestBody PaymentMethodDto dto) {
        PaymentMethod method = paymentMethodRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PaymentMethod", "id", id));
        
        method.setEnabled(dto.isEnabled());
        
        // Cập nhật URL ảnh nếu nó được gửi lên
        if (dto.getQrCodeImageUrl() != null) {
            method.setQrCodeImageUrl(dto.getQrCodeImageUrl());
        }
        
        PaymentMethod updatedMethod = paymentMethodRepository.save(method);
        return ResponseEntity.ok(mapToDto(updatedMethod));
    }

    /**
     * Helper method để chuyển đổi Entity sang DTO.
     * @param method Entity cần chuyển đổi.
     * @return DTO tương ứng.
     */
    private PaymentMethodDto mapToDto(PaymentMethod method) {
        PaymentMethodDto dto = new PaymentMethodDto();
        dto.setId(method.getId());
        dto.setMethodKey(method.getMethodKey());
        dto.setName(method.getName());
        dto.setDescription(method.getDescription());
        dto.setEnabled(method.isEnabled());
        dto.setConfigurable(method.isConfigurable());
        dto.setQrCodeImageUrl(method.getQrCodeImageUrl());
        return dto;
    }
}