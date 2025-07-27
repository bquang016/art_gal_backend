package com.example.art_gal.service.impl;

import com.example.art_gal.entity.Artist;
import com.example.art_gal.exception.ResourceNotFoundException;
import com.example.art_gal.payload.ArtistDto;
import com.example.art_gal.repository.ArtistRepository;
import com.example.art_gal.service.ArtistService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArtistServiceImpl implements ArtistService {

    private final ArtistRepository artistRepository;

    public ArtistServiceImpl(ArtistRepository artistRepository) {
        this.artistRepository = artistRepository;
    }

    @Override
    public ArtistDto createArtist(ArtistDto artistDto) {
        Artist artist = mapToEntity(artistDto);
        Artist newArtist = artistRepository.save(artist);
        return mapToDTO(newArtist);
    }

    @Override
    public List<ArtistDto> getAllArtists() {
        List<Artist> artists = artistRepository.findAll();
        return artists.stream().map(this::mapToDTO).collect(Collectors.toList());
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
        return mapToDTO(updatedArtist);
    }

    @Override
    public void deleteArtist(long id) {
        Artist artist = artistRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Artist", "id", id));
        artistRepository.delete(artist);
    }

    // Helper method to map Entity to DTO
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

    // Helper method to map DTO to Entity
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