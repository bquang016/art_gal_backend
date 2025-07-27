package com.example.art_gal.service;

import com.example.art_gal.payload.PaintingDto;
import java.util.List;

public interface PaintingService {
    PaintingDto createPainting(PaintingDto paintingDto);
    List<PaintingDto> getAllPaintings();
    PaintingDto getPaintingById(long id);
    PaintingDto updatePainting(PaintingDto paintingDto, long id);
    void deletePainting(long id);
}