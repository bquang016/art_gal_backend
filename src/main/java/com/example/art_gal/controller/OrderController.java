package com.example.art_gal.controller;

import com.example.art_gal.payload.OrderDto;
import com.example.art_gal.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
// import org.springframework.security.access.prepost.PreAuthorize; // Có thể xóa dòng này
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // API tạo Đơn hàng mới (chỉ cần đăng nhập)
    // @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_NHANVIEN')") // <-- XÓA DÒNG NÀY
    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@RequestBody OrderDto orderDto){
        return new ResponseEntity<>(orderService.createOrder(orderDto), HttpStatus.CREATED);
    }

    @GetMapping
    public List<OrderDto> getAllOrders(){
        return orderService.getAllOrders();
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable long id){
        return ResponseEntity.ok(orderService.getOrderById(id));
    }
    
    // API cập nhật trạng thái Đơn hàng (chỉ cần đăng nhập)
    // @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_NHANVIEN')") // <-- XÓA DÒNG NÀY
    @PatchMapping("/{id}/status")
    public ResponseEntity<OrderDto> updateOrderStatus(@PathVariable long id, @RequestBody Map<String, String> statusUpdate){
        String status = statusUpdate.get("status");
        OrderDto updatedOrder = orderService.updateOrderStatus(id, status);
        return ResponseEntity.ok(updatedOrder);
    }
}