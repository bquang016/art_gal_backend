package com.example.art_gal.controller;

import com.example.art_gal.payload.ResetPasswordDto;
import com.example.art_gal.payload.UpdateUserDto;
import com.example.art_gal.payload.UserCreateDto;
import com.example.art_gal.payload.UserDto;
import com.example.art_gal.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // API để Admin tạo tài khoản mới
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<String> createUser(@RequestBody UserCreateDto userCreateDto){
        String response = userService.createUser(userCreateDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // API lấy danh sách tất cả tài khoản
    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
    
    // API cập nhật thông tin tài khoản
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<UserDto> updateUser(@PathVariable long id, @RequestBody UpdateUserDto updateUserDto) {
        return ResponseEntity.ok(userService.updateUser(id, updateUserDto));
    }

    // API đặt lại mật khẩu
    @PatchMapping("/{id}/reset-password")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<String> resetPassword(@PathVariable long id, @RequestBody ResetPasswordDto resetPasswordDto) {
        userService.resetPassword(id, resetPasswordDto);
        return ResponseEntity.ok("Password reset successfully.");
    }
}