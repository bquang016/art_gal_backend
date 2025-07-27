package com.example.art_gal.controller;

import com.example.art_gal.payload.ArtistDto;
import com.example.art_gal.service.ArtistService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/artists")
public class ArtistController {

    private final ArtistService artistService;

    public ArtistController(ArtistService artistService) {
        this.artistService = artistService;
    }

    // API tạo Họa sĩ mới (Admin hoặc Nhân viên)
    @PreAuthorize("hasAnyRole('ADMIN', 'NHANVIEN')")
    @PostMapping
    public ResponseEntity<ArtistDto> createArtist(@RequestBody ArtistDto artistDto){
        return new ResponseEntity<>(artistService.createArtist(artistDto), HttpStatus.CREATED);
    }

    // API lấy tất cả Họa sĩ (Bất kỳ ai đã đăng nhập)
    @GetMapping
    public List<ArtistDto> getAllArtists(){
        return artistService.getAllArtists();
    }

    // API lấy Họa sĩ theo ID (Bất kỳ ai đã đăng nhập)
    @GetMapping("/{id}")
    public ResponseEntity<ArtistDto> getArtistById(@PathVariable(name = "id") long id){
        return ResponseEntity.ok(artistService.getArtistById(id));
    }

    // API cập nhật Họa sĩ (Admin hoặc Nhân viên)
    @PreAuthorize("hasAnyRole('ADMIN', 'NHANVIEN')")
    @PutMapping("/{id}")
    public ResponseEntity<ArtistDto> updateArtist(@RequestBody ArtistDto artistDto, @PathVariable(name = "id") long id){
        ArtistDto artistResponse = artistService.updateArtist(artistDto, id);
        return new ResponseEntity<>(artistResponse, HttpStatus.OK);
    }

    // API xóa Họa sĩ (Admin hoặc Nhân viên)
    @PreAuthorize("hasAnyRole('ADMIN', 'NHANVIEN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteArtist(@PathVariable(name = "id") long id){
        artistService.deleteArtist(id);
        return new ResponseEntity<>("Artist entity deleted successfully.", HttpStatus.OK);
    }
}