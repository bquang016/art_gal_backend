package com.example.art_gal.controller;

import com.example.art_gal.payload.CustomerDto;
import com.example.art_gal.service.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    // API tạo Khách hàng mới (Admin hoặc Nhân viên)
    @PreAuthorize("hasAnyRole('ADMIN', 'NHANVIEN')")
    @PostMapping
    public ResponseEntity<CustomerDto> createCustomer(@RequestBody CustomerDto customerDto){
        return new ResponseEntity<>(customerService.createCustomer(customerDto), HttpStatus.CREATED);
    }

    // API lấy tất cả Khách hàng
    @GetMapping
    public List<CustomerDto> getAllCustomers(){
        return customerService.getAllCustomers();
    }

    // API lấy Khách hàng theo ID
    @GetMapping("/{id}")
    public ResponseEntity<CustomerDto> getCustomerById(@PathVariable(name = "id") long id){
        return ResponseEntity.ok(customerService.getCustomerById(id));
    }

    // API cập nhật Khách hàng (Admin hoặc Nhân viên)
    @PreAuthorize("hasAnyRole('ADMIN', 'NHANVIEN')")
    @PutMapping("/{id}")
    public ResponseEntity<CustomerDto> updateCustomer(@RequestBody CustomerDto customerDto, @PathVariable(name = "id") long id){
        CustomerDto customerResponse = customerService.updateCustomer(customerDto, id);
        return new ResponseEntity<>(customerResponse, HttpStatus.OK);
    }

    // API xóa Khách hàng (Admin hoặc Nhân viên)
    @PreAuthorize("hasAnyRole('ADMIN', 'NHANVIEN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCustomer(@PathVariable(name = "id") long id){
        customerService.deleteCustomer(id);
        return new ResponseEntity<>("Customer entity deleted successfully.", HttpStatus.OK);
    }
}