package com.example.art_gal.service.impl;

import com.example.art_gal.payload.DashboardDataDto;
import com.example.art_gal.repository.OrderRepository;
import com.example.art_gal.repository.PaintingRepository;
import com.example.art_gal.service.DashboardService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;

@Service
public class DashboardServiceImpl implements DashboardService {

    private final OrderRepository orderRepository;
    private final PaintingRepository paintingRepository;

    public DashboardServiceImpl(OrderRepository orderRepository, PaintingRepository paintingRepository) {
        this.orderRepository = orderRepository;
        this.paintingRepository = paintingRepository;
    }

    @Override
    public DashboardDataDto getDashboardData() {
        DashboardDataDto dashboardData = new DashboardDataDto();

        // KPI Data (Logic giả lập)
        DashboardDataDto.KpiData kpi = new DashboardDataDto.KpiData();
        kpi.setTotalOrders(orderRepository.count());
        kpi.setTotalRevenue(BigDecimal.valueOf(1380000000)); // Logic thật sẽ tính tổng từ các order
        kpi.setInventory(paintingRepository.count());
        kpi.setProfit(BigDecimal.valueOf(517500000)); // Logic thật
        dashboardData.setKpiData(kpi);

        // Sales Data (Logic giả lập)
        DashboardDataDto.ChartData sales = new DashboardDataDto.ChartData();
        sales.setLabels(Arrays.asList("Hai", "Ba", "Tư", "Năm", "Sáu", "Bảy", "CN"));
        sales.setData(Arrays.asList(8.5, 11.2, 9.8, 14.5, 12.0, 15.3, 12.55));
        dashboardData.setSalesData(sales);

        // Proportion Data (Logic giả lập)
        DashboardDataDto.ChartData proportion = new DashboardDataDto.ChartData();
        proportion.setLabels(Arrays.asList("Sơn dầu", "Trừu tượng", "Sơn mài", "Phong cảnh"));
        proportion.setData(Arrays.asList(45.0, 25.0, 15.0, 15.0));
        dashboardData.setProportionData(proportion);

        return dashboardData;
    }
}