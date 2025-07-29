package com.example.art_gal.service.impl;

import com.example.art_gal.entity.*;
import com.example.art_gal.exception.ResourceNotFoundException;
import com.example.art_gal.payload.OrderDto;
import com.example.art_gal.payload.OrderDetailDto;
import com.example.art_gal.repository.*;
import com.example.art_gal.service.ActivityLogService;
import com.example.art_gal.service.OrderService;
import org.springframework.security.core.context.SecurityContextHolder; // ✅ THÊM DÒNG NÀY
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
    private final ActivityLogService activityLogService;

    public OrderServiceImpl(OrderRepository orderRepository, CustomerRepository customerRepository, UserRepository userRepository, PaintingRepository paintingRepository, ActivityLogService activityLogService) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.userRepository = userRepository;
        this.paintingRepository = paintingRepository;
        this.activityLogService = activityLogService;
    }

    @Override
    @Transactional
    public OrderDto createOrder(OrderDto orderDto) {
        Customer customer = customerRepository.findById(orderDto.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", orderDto.getCustomerId()));
        
        // Lấy đúng người dùng đang đăng nhập để thực hiện hành động
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User actor = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", 0)); // ID 0 là giả

        Order order = new Order();
        order.setCustomer(customer);
        order.setUser(actor); // Gán người tạo đơn hàng là người đang đăng nhập
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
            
            painting.setStatus("Dừng bán");
            paintingRepository.save(painting);
        }

        order.setOrderDetails(orderDetails);
        order.setTotalAmount(totalAmount);

        Order savedOrder = orderRepository.save(order);
        
        // Ghi nhật ký với đúng người thực hiện
        String details = String.format("Đã tạo đơn hàng #%d cho khách hàng '%s' với tổng giá trị %s.",
                savedOrder.getId(), customer.getName(), totalAmount.toString());
        activityLogService.logActivity(actor, "TẠO ĐƠN HÀNG", details);
        
        return mapToDTO(savedOrder);
    }

    @Override
    public List<OrderDto> getAllOrders() {
        return orderRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }
    
    @Override
    public List<OrderDto> getOrdersByCustomerId(long customerId) {
        List<Order> orders = orderRepository.findByCustomerId(customerId);
        return orders.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public OrderDto getOrderById(long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", id));
        return mapToDTO(order);
    }

    @Override
    @Transactional
    public OrderDto updateOrderStatus(long id, String status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", id));
        
        String oldStatus = order.getStatus();
        order.setStatus(status);

        if ("Hoàn thành".equalsIgnoreCase(status) && !"Hoàn thành".equalsIgnoreCase(oldStatus)) {
            for (OrderDetail detail : order.getOrderDetails()) {
                Painting painting = detail.getPainting();
                if (painting != null) {
                    painting.setStatus("Đã bán");
                    paintingRepository.save(painting);
                }
            }
        }
        
        if ("Đã hủy".equalsIgnoreCase(status) && !"Đã hủy".equalsIgnoreCase(oldStatus)) {
             for (OrderDetail detail : order.getOrderDetails()) {
                Painting painting = detail.getPainting();
                if (painting != null) {
                    painting.setStatus("Đang bán");
                    paintingRepository.save(painting);
                }
            }
        }
        
        Order updatedOrder = orderRepository.save(order);

        // ✅ SỬA LẠI: Lấy đúng người dùng đang đăng nhập từ SecurityContext
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User actor = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", 0)); // ID 0 là giả

        String details = String.format("Đã cập nhật trạng thái đơn hàng #%d từ '%s' sang '%s'.",
                updatedOrder.getId(), oldStatus, status);
        activityLogService.logActivity(actor, "CẬP NHẬT ĐƠN HÀNG", details);

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
            detailDto.setPaintingName(detail.getPainting().getName());
            detailDto.setQuantity(detail.getQuantity());
            detailDto.setPrice(detail.getPrice());
            return detailDto;
        }).collect(Collectors.toSet()));

        return orderDto;
    }
}