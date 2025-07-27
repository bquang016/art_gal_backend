package com.example.art_gal.service;

import com.example.art_gal.payload.CustomerDto;
import java.util.List;

public interface CustomerService {
    CustomerDto createCustomer(CustomerDto customerDto);
    List<CustomerDto> getAllCustomers();
    CustomerDto getCustomerById(long id);
    CustomerDto updateCustomer(CustomerDto customerDto, long id);
    void deleteCustomer(long id);
}