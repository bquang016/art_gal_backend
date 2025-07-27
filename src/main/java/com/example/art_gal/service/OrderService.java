package com.example.art_gal.service;

import com.example.art_gal.payload.OrderDto;
import java.util.List;

public interface OrderService {
    OrderDto createOrder(OrderDto orderDto);
    List<OrderDto> getAllOrders();
    OrderDto getOrderById(long id);
    OrderDto updateOrderStatus(long id, String status);
}