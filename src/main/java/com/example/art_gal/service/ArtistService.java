package com.example.art_gal.service;

import com.example.art_gal.payload.ArtistDto;
import java.util.List;

public interface ArtistService {
    ArtistDto createArtist(ArtistDto artistDto);
    List<ArtistDto> getAllArtists();
    ArtistDto getArtistById(long id);
    ArtistDto updateArtist(ArtistDto artistDto, long id);
    void deleteArtist(long id);
}