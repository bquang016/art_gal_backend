package com.example.art_gal.config;

import com.example.art_gal.entity.Role;
import com.example.art_gal.entity.User;
import com.example.art_gal.repository.RoleRepository;
import com.example.art_gal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
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

    @Override
    public void run(String... args) throws Exception {
        // ✅ SỬA LẠI: Sử dụng String cho tên vai trò, không dùng ERole
        String adminRoleName = "ROLE_ADMIN";
        String nhanvienRoleName = "ROLE_NHANVIEN";

        // Kiểm tra và tạo vai trò ADMIN nếu chưa tồn tại
        if (roleRepository.findByName(adminRoleName).isEmpty()) {
            Role role = new Role();
            role.setName(adminRoleName);
            roleRepository.save(role);
        }

        // Kiểm tra và tạo vai trò NHANVIEN nếu chưa tồn tại
        if (roleRepository.findByName(nhanvienRoleName).isEmpty()) {
            Role role = new Role();
            role.setName(nhanvienRoleName);
            roleRepository.save(role);
        }

        // Kiểm tra và tạo người dùng 'admin' nếu chưa tồn tại
        if (userRepository.findByUsername("admin").isEmpty()) {
            Role adminRole = roleRepository.findByName(adminRoleName)
                    .orElseThrow(() -> new RuntimeException("Error: Role ADMIN is not found."));
            Role userRoleForAdmin = roleRepository.findByName(nhanvienRoleName)
                    .orElseThrow(() -> new RuntimeException("Error: Role NHANVIEN is not found."));

            User admin = new User();
            admin.setName("Quang Đẹp Trai");
            admin.setUsername("admin");
            admin.setEmail("admin@artgallery.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRoles(new HashSet<>(Arrays.asList(adminRole, userRoleForAdmin)));
            userRepository.save(admin);
        }

        // Kiểm tra và tạo người dùng 'nhanvien' nếu chưa tồn tại
        if (userRepository.findByUsername("nhanvien").isEmpty()) {
            Role userRole = roleRepository.findByName(nhanvienRoleName)
                    .orElseThrow(() -> new RuntimeException("Error: Role NHANVIEN is not found."));

            User nhanvien = new User();
            nhanvien.setName("Nhân Viên Bán Hàng");
            nhanvien.setUsername("nhanvien");
            nhanvien.setEmail("nhanvien@artgallery.com");
            nhanvien.setPassword(passwordEncoder.encode("nhanvien123"));
            nhanvien.setRoles(new HashSet<>(Collections.singletonList(userRole)));
            userRepository.save(nhanvien);
        }
    }
}