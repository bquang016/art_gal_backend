package com.example.art_gal.payload;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
public class DashboardDataDto {
    private KpiData kpiData;
    private ChartData salesData;
    private ChartData proportionData;

    @Data
    public static class KpiData {
        private long totalOrders;
        private BigDecimal totalRevenue;
        private long inventory;
        private BigDecimal profit; // Giả sử lợi nhuận được tính toán
    }

    @Data
    public static class ChartData {
        private List<String> labels;
        private List<Double> data;
    }
}