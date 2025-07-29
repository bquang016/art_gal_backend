package com.example.art_gal.service.impl;

import com.example.art_gal.entity.Artist;
import com.example.art_gal.entity.User;
import com.example.art_gal.exception.ResourceNotFoundException;
import com.example.art_gal.payload.ArtistDto;
import com.example.art_gal.repository.ArtistRepository;
import com.example.art_gal.repository.UserRepository;
import com.example.art_gal.service.ActivityLogService;
import com.example.art_gal.service.ArtistService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArtistServiceImpl implements ArtistService {

    private final ArtistRepository artistRepository;
    private final UserRepository userRepository;
    private final ActivityLogService activityLogService;

    public ArtistServiceImpl(ArtistRepository artistRepository, UserRepository userRepository, ActivityLogService activityLogService) {
        this.artistRepository = artistRepository;
        this.userRepository = userRepository;
        this.activityLogService = activityLogService;
    }

    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", 0));
    }

    @Override
    public ArtistDto createArtist(ArtistDto artistDto) {
        Artist artist = mapToEntity(artistDto);
        Artist newArtist = artistRepository.save(artist);
        
        // GHI NHẬT KÝ
        activityLogService.logActivity(getCurrentUser(), "TẠO HỌA SĨ", "Đã tạo mới họa sĩ: " + newArtist.getName());

        return mapToDTO(newArtist);
    }

    @Override
    public List<ArtistDto> getAllArtists() {
        return artistRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public ArtistDto getArtistById(long id) {
        Artist artist = artistRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Artist", "id", id));
        return mapToDTO(artist);
    }

    @Override
    public ArtistDto updateArtist(ArtistDto artistDto, long id) {
        Artist artist = artistRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Artist", "id", id));

        artist.setName(artistDto.getName());
        artist.setPhone(artistDto.getPhone());
        artist.setEmail(artistDto.getEmail());
        artist.setAddress(artistDto.getAddress());
        artist.setStatus(artistDto.getStatus());

        Artist updatedArtist = artistRepository.save(artist);
        
        // GHI NHẬT KÝ
        activityLogService.logActivity(getCurrentUser(), "CẬP NHẬT HỌA SĨ", "Đã cập nhật thông tin họa sĩ: " + updatedArtist.getName());

        return mapToDTO(updatedArtist);
    }

    @Override
    public void deleteArtist(long id) {
        Artist artist = artistRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Artist", "id", id));
        
        // GHI NHẬT KÝ TRƯỚC KHI XÓA
        activityLogService.logActivity(getCurrentUser(), "XÓA HỌA SĨ", "Đã xóa họa sĩ: " + artist.getName());

        artistRepository.delete(artist);
    }

    private ArtistDto mapToDTO(Artist artist){
        ArtistDto artistDto = new ArtistDto();
        artistDto.setId(artist.getId());
        artistDto.setName(artist.getName());
        artistDto.setPhone(artist.getPhone());
        artistDto.setEmail(artist.getEmail());
        artistDto.setAddress(artist.getAddress());
        artistDto.setStatus(artist.getStatus());
        return artistDto;
    }

    private Artist mapToEntity(ArtistDto artistDto){
        Artist artist = new Artist();
        artist.setName(artistDto.getName());
        artist.setPhone(artistDto.getPhone());
        artist.setEmail(artistDto.getEmail());
        artist.setAddress(artistDto.getAddress());
        artist.setStatus(artistDto.getStatus());
        return artist;
    }
}