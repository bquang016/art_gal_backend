package com.example.art_gal.service.impl;

import com.example.art_gal.entity.Customer;
import com.example.art_gal.entity.User;
import com.example.art_gal.exception.ResourceNotFoundException;
import com.example.art_gal.payload.CustomerDto;
import com.example.art_gal.repository.CustomerRepository;
import com.example.art_gal.repository.UserRepository;
import com.example.art_gal.service.ActivityLogService;
import com.example.art_gal.service.CustomerService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final ActivityLogService activityLogService;

    public CustomerServiceImpl(CustomerRepository customerRepository, UserRepository userRepository, ActivityLogService activityLogService) {
        this.customerRepository = customerRepository;
        this.userRepository = userRepository;
        this.activityLogService = activityLogService;
    }

    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", 0));
    }

    @Override
    public CustomerDto createCustomer(CustomerDto customerDto) {
        Customer customer = mapToEntity(customerDto);
        Customer newCustomer = customerRepository.save(customer);
        
        activityLogService.logActivity(getCurrentUser(), "TẠO KHÁCH HÀNG", "Đã tạo khách hàng mới: " + newCustomer.getName());

        return mapToDTO(newCustomer);
    }

    @Override
    public List<CustomerDto> getAllCustomers() {
        return customerRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public CustomerDto getCustomerById(long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", id));
        return mapToDTO(customer);
    }

    @Override
    public CustomerDto updateCustomer(CustomerDto customerDto, long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", id));

        customer.setName(customerDto.getName());
        customer.setPhone(customerDto.getPhone());
        customer.setEmail(customerDto.getEmail());
        customer.setAddress(customerDto.getAddress());
        customer.setStatus(customerDto.getStatus());

        Customer updatedCustomer = customerRepository.save(customer);
        
        activityLogService.logActivity(getCurrentUser(), "CẬP NHẬT KHÁCH HÀNG", "Đã cập nhật thông tin khách hàng: " + updatedCustomer.getName());
        
        return mapToDTO(updatedCustomer);
    }

    @Override
    public void deleteCustomer(long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", id));
        
        activityLogService.logActivity(getCurrentUser(), "XÓA KHÁCH HÀNG", "Đã xóa khách hàng: " + customer.getName());

        customerRepository.delete(customer);
    }

    private CustomerDto mapToDTO(Customer customer){
        CustomerDto customerDto = new CustomerDto();
        customerDto.setId(customer.getId());
        customerDto.setName(customer.getName());
        customerDto.setPhone(customer.getPhone());
        customerDto.setEmail(customer.getEmail());
        customerDto.setAddress(customer.getAddress());
        customerDto.setStatus(customer.getStatus());
        return customerDto;
    }

    private Customer mapToEntity(CustomerDto customerDto){
        Customer customer = new Customer();
        customer.setName(customerDto.getName());
        customer.setPhone(customerDto.getPhone());
        customer.setEmail(customerDto.getEmail());
        customer.setAddress(customerDto.getAddress());
        customer.setStatus(customerDto.getStatus());
        return customer;
    }
}