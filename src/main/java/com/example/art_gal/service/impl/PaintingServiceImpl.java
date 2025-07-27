package com.example.art_gal.service.impl;

import com.example.art_gal.entity.Artist;
import com.example.art_gal.entity.Category;
import com.example.art_gal.entity.Painting;
import com.example.art_gal.exception.ResourceNotFoundException;
import com.example.art_gal.payload.PaintingDto;
import com.example.art_gal.repository.ArtistRepository;
import com.example.art_gal.repository.CategoryRepository;
import com.example.art_gal.repository.PaintingRepository;
import com.example.art_gal.service.PaintingService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaintingServiceImpl implements PaintingService {

    private final PaintingRepository paintingRepository;
    private final ArtistRepository artistRepository;
    private final CategoryRepository categoryRepository;

    public PaintingServiceImpl(PaintingRepository paintingRepository,
                               ArtistRepository artistRepository,
                               CategoryRepository categoryRepository) {
        this.paintingRepository = paintingRepository;
        this.artistRepository = artistRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public PaintingDto createPainting(PaintingDto paintingDto) {
        Painting painting = mapToEntity(paintingDto);
        Painting newPainting = paintingRepository.save(painting);
        return mapToDTO(newPainting);
    }

    @Override
    public List<PaintingDto> getAllPaintings() {
        List<Painting> paintings = paintingRepository.findAll();
        return paintings.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public PaintingDto getPaintingById(long id) {
        Painting painting = paintingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Painting", "id", id));
        return mapToDTO(painting);
    }

    @Override
    public PaintingDto updatePainting(PaintingDto paintingDto, long id) {
        Painting painting = paintingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Painting", "id", id));

        Artist artist = artistRepository.findById(paintingDto.getArtistId())
                .orElseThrow(() -> new ResourceNotFoundException("Artist", "id", paintingDto.getArtistId()));
        Category category = categoryRepository.findById(paintingDto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", paintingDto.getCategoryId()));

        painting.setName(paintingDto.getName());
        painting.setMaterial(paintingDto.getMaterial());
        painting.setImage(paintingDto.getImage());
        painting.setImportPrice(paintingDto.getImportPrice());
        painting.setSellingPrice(paintingDto.getSellingPrice());
        painting.setStatus(paintingDto.getStatus());
        painting.setArtist(artist);
        painting.setCategory(category);

        Painting updatedPainting = paintingRepository.save(painting);
        return mapToDTO(updatedPainting);
    }

    @Override
    public void deletePainting(long id) {
        Painting painting = paintingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Painting", "id", id));
        paintingRepository.delete(painting);
    }

    // Helper methods
    private PaintingDto mapToDTO(Painting painting){
        PaintingDto paintingDto = new PaintingDto();
        paintingDto.setId(painting.getId());
        paintingDto.setName(painting.getName());
        paintingDto.setMaterial(painting.getMaterial());
        paintingDto.setImage(painting.getImage());
        paintingDto.setImportPrice(painting.getImportPrice());
        paintingDto.setSellingPrice(painting.getSellingPrice());
        paintingDto.setStatus(painting.getStatus());
        paintingDto.setArtistId(painting.getArtist().getId());
        paintingDto.setCategoryId(painting.getCategory().getId());
        return paintingDto;
    }

    private Painting mapToEntity(PaintingDto paintingDto){
        Painting painting = new Painting();
        
        Artist artist = artistRepository.findById(paintingDto.getArtistId())
                .orElseThrow(() -> new ResourceNotFoundException("Artist", "id", paintingDto.getArtistId()));
        Category category = categoryRepository.findById(paintingDto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", paintingDto.getCategoryId()));

        painting.setName(paintingDto.getName());
        painting.setMaterial(paintingDto.getMaterial());
        painting.setImage(paintingDto.getImage());
        painting.setImportPrice(paintingDto.getImportPrice());
        painting.setSellingPrice(paintingDto.getSellingPrice());
        painting.setStatus(paintingDto.getStatus());
        painting.setArtist(artist);
        painting.setCategory(category);
        return painting;
    }
}