package com.example.art_gal.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

@Setter
@Getter
@Entity
@Table(name = "import_slips")
public class ImportSlip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date importDate;

    @Column(precision = 19, scale = 4)
    private BigDecimal totalValue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artist_id") // Nhà cung cấp là Họa sĩ
    private Artist artist;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id") // Nhân viên tạo phiếu
    private User user;

    @OneToMany(mappedBy = "importSlip", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ImportSlipDetail> importSlipDetails;
}