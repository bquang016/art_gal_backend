package com.example.art_gal.config;

import com.example.art_gal.entity.PaymentMethod;
import com.example.art_gal.entity.Role;
import com.example.art_gal.entity.User;
import com.example.art_gal.repository.PaymentMethodRepository;
import com.example.art_gal.repository.RoleRepository;
import com.example.art_gal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashSet;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    @Override
    public void run(String... args) throws Exception {
        seedRolesAndUsers();
        seedPaymentMethods();
    }

    private void seedRolesAndUsers() {
        String adminRoleName = "ROLE_ADMIN";
        String nhanvienRoleName = "ROLE_NHANVIEN";

        if (roleRepository.findByName(adminRoleName).isEmpty()) {
            Role role = new Role();
            role.setName(adminRoleName);
            roleRepository.save(role);
        }

        if (roleRepository.findByName(nhanvienRoleName).isEmpty()) {
            Role role = new Role();
            role.setName(nhanvienRoleName);
            roleRepository.save(role);
        }

        if (userRepository.findByUsername("admin").isEmpty()) {
            Role adminRole = roleRepository.findByName(adminRoleName).orElseThrow();
            User admin = new User();
            admin.setName("Quang Đẹp Trai");
            admin.setUsername("admin");
            admin.setEmail("admin@artgallery.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setStatus("Hoạt động");
            admin.setRoles(new HashSet<>(Collections.singletonList(adminRole)));
            userRepository.save(admin);
        }

        if (userRepository.findByUsername("nhanvien").isEmpty()) {
            Role userRole = roleRepository.findByName(nhanvienRoleName).orElseThrow();
            User nhanvien = new User();
            nhanvien.setName("Nhân Viên Bán Hàng");
            nhanvien.setUsername("nhanvien");
            nhanvien.setEmail("nhanvien@artgallery.com");
            nhanvien.setPassword(passwordEncoder.encode("nhanvien123"));
            nhanvien.setStatus("Hoạt động");
            nhanvien.setRoles(new HashSet<>(Collections.singletonList(userRole)));
            userRepository.save(nhanvien);
        }
    }
    
    private void seedPaymentMethods() {
        if (paymentMethodRepository.count() == 0) {
            PaymentMethod cash = new PaymentMethod();
            cash.setMethodKey("cash");
            cash.setName("Tiền mặt");
            cash.setDescription("Thanh toán trực tiếp bằng tiền mặt tại quầy.");
            cash.setEnabled(true);
            cash.setConfigurable(false);
            paymentMethodRepository.save(cash);

            PaymentMethod qrBank = new PaymentMethod();
            qrBank.setMethodKey("qr_bank");
            qrBank.setName("Chuyển khoản ngân hàng");
            qrBank.setDescription("Quét mã QR để chuyển khoản qua ứng dụng ngân hàng.");
            qrBank.setEnabled(true);
            qrBank.setConfigurable(true);
            paymentMethodRepository.save(qrBank);

            PaymentMethod momo = new PaymentMethod();
            momo.setMethodKey("qr_momo");
            momo.setName("Ví điện tử MoMo");
            momo.setDescription("Quét mã QR để thanh toán qua ví MoMo.");
            momo.setEnabled(true); 
            momo.setConfigurable(true);
            paymentMethodRepository.save(momo);

            PaymentMethod zaloPay = new PaymentMethod();
            zaloPay.setMethodKey("qr_zalopay");
            zaloPay.setName("Ví điện tử ZaloPay");
            zaloPay.setDescription("Quét mã QR để thanh toán qua ví ZaloPay.");
            zaloPay.setEnabled(true); 
            zaloPay.setConfigurable(true);
            paymentMethodRepository.save(zaloPay);
        }
    }
}