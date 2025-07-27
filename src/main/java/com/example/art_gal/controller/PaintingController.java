package com.example.art_gal.controller;

import com.example.art_gal.payload.PaintingDto;
import com.example.art_gal.service.PaintingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/paintings")
public class PaintingController {

    private final PaintingService paintingService;

    public PaintingController(PaintingService paintingService) {
        this.paintingService = paintingService;
    }

    // API tạo Tranh mới (Admin hoặc Nhân viên)
    @PreAuthorize("hasAnyRole('ADMIN', 'NHANVIEN')")
    @PostMapping
    public ResponseEntity<PaintingDto> createPainting(@RequestBody PaintingDto paintingDto){
        return new ResponseEntity<>(paintingService.createPainting(paintingDto), HttpStatus.CREATED);
    }

    // API lấy tất cả Tranh
    @GetMapping
    public List<PaintingDto> getAllPaintings(){
        return paintingService.getAllPaintings();
    }

    // API lấy Tranh theo ID
    @GetMapping("/{id}")
    public ResponseEntity<PaintingDto> getPaintingById(@PathVariable(name = "id") long id){
        return ResponseEntity.ok(paintingService.getPaintingById(id));
    }

    // API cập nhật Tranh (Admin hoặc Nhân viên)
    @PreAuthorize("hasAnyRole('ADMIN', 'NHANVIEN')")
    @PutMapping("/{id}")
    public ResponseEntity<PaintingDto> updatePainting(@RequestBody PaintingDto paintingDto, @PathVariable(name = "id") long id){
        PaintingDto paintingResponse = paintingService.updatePainting(paintingDto, id);
        return new ResponseEntity<>(paintingResponse, HttpStatus.OK);
    }

    // API xóa Tranh (Admin hoặc Nhân viên)
    @PreAuthorize("hasAnyRole('ADMIN', 'NHANVIEN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePainting(@PathVariable(name = "id") long id){
        paintingService.deletePainting(id);
        return new ResponseEntity<>("Painting entity deleted successfully.", HttpStatus.OK);
    }
}