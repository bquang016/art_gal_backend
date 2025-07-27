package com.example.art_gal.payload;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

@Data
public class ImportSlipDto {
    private Long id;
    private Date importDate;
    private BigDecimal totalValue;
    private String artistName;
    private String userName;
    private Set<ImportSlipDetailDto> importSlipDetails;
}