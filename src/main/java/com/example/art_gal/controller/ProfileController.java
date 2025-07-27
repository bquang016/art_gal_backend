package com.example.art_gal.controller;

import com.example.art_gal.payload.UserDto;
import com.example.art_gal.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import com.example.art_gal.repository.UserRepository;
import com.example.art_gal.entity.User;


@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    private final UserRepository userRepository;
    // Inject các service cần thiết khác nếu cần

    public ProfileController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // API lấy thông tin của user đang đăng nhập
    @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUser(Authentication authentication) {
        User user = userRepository.findByUsernameOrEmail(authentication.getName(), authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Map User entity to UserDto
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        // Lấy thêm SĐT và các thông tin khác nếu bạn đã thêm vào User entity
        // userDto.setPhone(user.getPhone()); 

        return ResponseEntity.ok(userDto);
    }
    
    // Bạn có thể thêm các API để cập nhật thông tin hoặc đổi mật khẩu ở đây
}