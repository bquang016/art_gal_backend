package com.example.art_gal.service.impl;

import com.example.art_gal.entity.*;
import com.example.art_gal.exception.ResourceNotFoundException;
import com.example.art_gal.payload.OrderDto;
import com.example.art_gal.payload.OrderDetailDto;
import com.example.art_gal.repository.*;
import com.example.art_gal.service.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final PaintingRepository paintingRepository;

    public OrderServiceImpl(OrderRepository orderRepository, CustomerRepository customerRepository, UserRepository userRepository, PaintingRepository paintingRepository) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.userRepository = userRepository;
        this.paintingRepository = paintingRepository;
    }

    @Override
    @Transactional
    public OrderDto createOrder(OrderDto orderDto) {
        Customer customer = customerRepository.findById(orderDto.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", orderDto.getCustomerId()));
        
        User user = userRepository.findById(orderDto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", orderDto.getUserId()));

        Order order = new Order();
        order.setCustomer(customer);
        order.setUser(user);
        order.setOrderDate(new Date());
        order.setStatus("Chờ xác nhận");

        Set<OrderDetail> orderDetails = new HashSet<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (OrderDetailDto detailDto : orderDto.getOrderDetails()) {
            Painting painting = paintingRepository.findById(detailDto.getPaintingId())
                    .orElseThrow(() -> new ResourceNotFoundException("Painting", "id", detailDto.getPaintingId()));
            
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setPainting(painting);
            orderDetail.setQuantity(detailDto.getQuantity());
            orderDetail.setPrice(painting.getSellingPrice());
            orderDetail.setOrder(order);

            orderDetails.add(orderDetail);
            totalAmount = totalAmount.add(painting.getSellingPrice().multiply(BigDecimal.valueOf(detailDto.getQuantity())));
        }

        order.setOrderDetails(orderDetails);
        order.setTotalAmount(totalAmount);

        Order savedOrder = orderRepository.save(order);
        return mapToDTO(savedOrder);
    }

    @Override
    public List<OrderDto> getAllOrders() {
        return orderRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public OrderDto getOrderById(long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", id));
        return mapToDTO(order);
    }

    @Override
    @Transactional // Đảm bảo tất cả các thao tác đều thành công hoặc sẽ rollback
    public OrderDto updateOrderStatus(long id, String status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", id));
        
        order.setStatus(status);

        // THÊM LOGIC MỚI Ở ĐÂY
        if ("Hoàn thành".equalsIgnoreCase(status)) {
            for (OrderDetail detail : order.getOrderDetails()) {
                Painting painting = detail.getPainting();
                if (painting != null) {
                    painting.setStatus("Dừng bán"); // Cập nhật trạng thái tranh
                    paintingRepository.save(painting); // Lưu lại thay đổi
                }
            }
        }
        
        Order updatedOrder = orderRepository.save(order);
        return mapToDTO(updatedOrder);
    }
    
    private OrderDto mapToDTO(Order order){
        OrderDto orderDto = new OrderDto();
        orderDto.setId(order.getId());
        orderDto.setOrderDate(order.getOrderDate());
        orderDto.setTotalAmount(order.getTotalAmount());
        orderDto.setStatus(order.getStatus());
        orderDto.setCustomerId(order.getCustomer().getId());
        orderDto.setCustomerName(order.getCustomer().getName());
        orderDto.setUserId(order.getUser().getId());
        orderDto.setUserName(order.getUser().getName());
        
        orderDto.setOrderDetails(order.getOrderDetails().stream().map(detail -> {
            OrderDetailDto detailDto = new OrderDetailDto();
            detailDto.setPaintingId(detail.getPainting().getId());
            detailDto.setQuantity(detail.getQuantity());
            detailDto.setPrice(detail.getPrice());
            return detailDto;
        }).collect(Collectors.toSet()));

        return orderDto;
    }
}