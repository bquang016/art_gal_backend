package com.example.art_gal.service.impl;

import com.example.art_gal.entity.*;
import com.example.art_gal.exception.ResourceNotFoundException;
import com.example.art_gal.payload.ImportSlipCreateDto;
import com.example.art_gal.payload.ImportSlipDetailDto;
import com.example.art_gal.payload.ImportSlipDto;
import com.example.art_gal.payload.ImportSlipItemDto;
import com.example.art_gal.repository.*;
import com.example.art_gal.service.ImportSlipService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ImportSlipServiceImpl implements ImportSlipService {

    private final ImportSlipRepository importSlipRepository;
    private final PaintingRepository paintingRepository;
    private final ArtistRepository artistRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public ImportSlipServiceImpl(ImportSlipRepository i, PaintingRepository p, ArtistRepository a, CategoryRepository c, UserRepository u) {
        this.importSlipRepository = i;
        this.paintingRepository = p;
        this.artistRepository = a;
        this.categoryRepository = c;
        this.userRepository = u;
    }

    @Override
    @Transactional
    public void createImportSlip(ImportSlipCreateDto createDto) {
        Artist artist = artistRepository.findById(createDto.getArtistId())
                .orElseThrow(() -> new ResourceNotFoundException("Artist", "id", createDto.getArtistId()));
        User user = userRepository.findById(createDto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", createDto.getUserId()));

        ImportSlip slip = new ImportSlip();
        slip.setArtist(artist);
        slip.setUser(user);
        slip.setImportDate(new Date());

        Set<ImportSlipDetail> details = new HashSet<>();
        BigDecimal totalValue = BigDecimal.ZERO;

        for (ImportSlipItemDto itemDto : createDto.getItems()) {
            Category category = categoryRepository.findById(itemDto.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category", "id", itemDto.getCategoryId()));
            
            Painting newPainting = new Painting();
            newPainting.setName(itemDto.getName());
            newPainting.setArtist(artist);
            newPainting.setCategory(category);
            newPainting.setMaterial(itemDto.getMaterial());
            newPainting.setImportPrice(itemDto.getImportPrice());
            newPainting.setSellingPrice(itemDto.getSellingPrice());
            newPainting.setStatus("Đang bán");
            Painting savedPainting = paintingRepository.save(newPainting);

            ImportSlipDetail detail = new ImportSlipDetail();
            detail.setPainting(savedPainting);
            detail.setImportSlip(slip);
            details.add(detail);

            totalValue = totalValue.add(itemDto.getImportPrice());
        }
        
        slip.setTotalValue(totalValue);
        slip.setImportSlipDetails(details);

        importSlipRepository.save(slip);
    }

    @Override
    public List<ImportSlipDto> getAllImportSlips() {
        return importSlipRepository.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
    }

    private ImportSlipDto mapToDto(ImportSlip slip) {
        ImportSlipDto dto = new ImportSlipDto();
        dto.setId(slip.getId());
        dto.setImportDate(slip.getImportDate());
        dto.setTotalValue(slip.getTotalValue());
        dto.setArtistName(slip.getArtist().getName());
        dto.setUserName(slip.getUser().getName());
        
        dto.setImportSlipDetails(slip.getImportSlipDetails().stream().map(detail -> {
            ImportSlipDetailDto detailDto = new ImportSlipDetailDto();
            detailDto.setPaintingId(detail.getPainting().getId());
            detailDto.setPaintingName(detail.getPainting().getName());
            detailDto.setPrice(detail.getPainting().getImportPrice()); 
            return detailDto;
        }).collect(Collectors.toSet()));

        return dto;
    }
}