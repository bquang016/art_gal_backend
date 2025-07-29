package com.example.art_gal.service.impl;

import com.example.art_gal.payload.DashboardDataDto;
import com.example.art_gal.repository.OrderRepository;
import com.example.art_gal.repository.PaintingRepository;
import com.example.art_gal.service.DashboardService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DashboardServiceImpl implements DashboardService {

    private final OrderRepository orderRepository;
    private final PaintingRepository paintingRepository;

    // ✅ SỬA LẠI CONSTRUCTOR: Bỏ CategoryRepository vì không dùng đến
    public DashboardServiceImpl(OrderRepository orderRepository, PaintingRepository paintingRepository) {
        this.orderRepository = orderRepository;
        this.paintingRepository = paintingRepository;
    }

    @Override
    public DashboardDataDto getDashboardData() {
        DashboardDataDto dashboardData = new DashboardDataDto();

        // --- KPI Data ---
        DashboardDataDto.KpiData kpi = new DashboardDataDto.KpiData();
        kpi.setTotalOrders(orderRepository.count());
        
        BigDecimal totalRevenue = orderRepository.sumTotalAmount();
        kpi.setTotalRevenue(totalRevenue != null ? totalRevenue : BigDecimal.ZERO);

        // ✅ LỖI ĐÃ ĐƯỢC SỬA Ở ĐÂY (SAU KHI CẬP NHẬT REPOSITORY)
        kpi.setInventory(paintingRepository.countByStatus("Đang bán"));
        
        BigDecimal profit = (totalRevenue != null ? totalRevenue : BigDecimal.ZERO)
                .multiply(new BigDecimal("0.4"))
                .setScale(2, RoundingMode.HALF_UP);
        kpi.setProfit(profit);
        
        dashboardData.setKpiData(kpi);

        // --- Sales Data (7 đơn hàng gần nhất) ---
        DashboardDataDto.ChartData sales = new DashboardDataDto.ChartData();
        List<String> salesLabels = new ArrayList<>();
        List<Double> salesData = new ArrayList<>();
        orderRepository.findTop7ByOrderByOrderDateDesc().forEach(order -> {
            salesLabels.add("ĐH #" + order.getId());
            salesData.add(order.getTotalAmount().doubleValue() / 1000000); // Chia cho 1 triệu cho dễ nhìn
        });
        Collections.reverse(salesLabels);
        Collections.reverse(salesData);
        sales.setLabels(salesLabels);
        sales.setData(salesData);
        dashboardData.setSalesData(sales);

        // --- Proportion Data (Tỷ lệ tranh theo danh mục) ---
        DashboardDataDto.ChartData proportion = new DashboardDataDto.ChartData();
        Map<String, Long> paintingsByCategory = paintingRepository.findAll().stream()
                .filter(p -> p.getCategory() != null)
                .collect(Collectors.groupingBy(p -> p.getCategory().getName(), Collectors.counting()));
        
        proportion.setLabels(new ArrayList<>(paintingsByCategory.keySet()));
        proportion.setData(paintingsByCategory.values().stream().map(Long::doubleValue).collect(Collectors.toList()));
        dashboardData.setProportionData(proportion);

        return dashboardData;
    }
}