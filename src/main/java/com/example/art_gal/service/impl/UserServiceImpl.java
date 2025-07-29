package com.example.art_gal.service.impl;

import com.example.art_gal.entity.Role;
import com.example.art_gal.entity.User;
import com.example.art_gal.exception.APIException;
import com.example.art_gal.exception.ResourceNotFoundException;
import com.example.art_gal.payload.ResetPasswordDto;
import com.example.art_gal.payload.UpdateUserDto;
import com.example.art_gal.payload.UserCreateDto;
import com.example.art_gal.payload.UserDto;
import com.example.art_gal.repository.RoleRepository;
import com.example.art_gal.repository.UserRepository;
import com.example.art_gal.service.ActivityLogService; // ✅ THÊM DÒNG NÀY
import com.example.art_gal.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder; // ✅ THÊM DÒNG NÀY
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ActivityLogService activityLogService; // ✅ THÊM DÒNG NÀY

    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder,
                           ActivityLogService activityLogService) { // ✅ THÊM VÀO CONSTRUCTOR
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.activityLogService = activityLogService; // ✅ THÊM DÒNG NÀY
    }

    @Override
    public String createUser(UserCreateDto userCreateDto) {
        if(userRepository.existsByUsername(userCreateDto.getUsername())){
            throw new APIException(HttpStatus.BAD_REQUEST, "Username is already exists!.");
        }

        if(userRepository.existsByEmail(userCreateDto.getEmail())){
            throw new APIException(HttpStatus.BAD_REQUEST, "Email is already exists!.");
        }

        User user = new User();
        user.setName(userCreateDto.getName());
        user.setUsername(userCreateDto.getUsername());
        user.setEmail(userCreateDto.getEmail());
        user.setPassword(passwordEncoder.encode(userCreateDto.getPassword()));
        user.setStatus(userCreateDto.getStatus() != null ? userCreateDto.getStatus() : "Hoạt động");


        Set<Role> roles = new HashSet<>();
        userCreateDto.getRoles().forEach(roleName -> {
            Role userRole = roleRepository.findByName("ROLE_" + roleName.toUpperCase())
                    .orElseThrow(() -> new APIException(HttpStatus.BAD_REQUEST, "Role not found: " + roleName));
            roles.add(userRole);
        });
        user.setRoles(roles);

        userRepository.save(user);

        // ✅ GHI NHẬT KÝ
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User actor = userRepository.findByUsername(username).orElse(null);
        if (actor != null) {
            String details = String.format("Đã tạo tài khoản mới '%s' với vai trò %s.", user.getUsername(), userCreateDto.getRoles());
            activityLogService.logActivity(actor, "TẠO TÀI KHOẢN", details);
        }

        return "User created successfully!.";
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public UserDto updateUser(long id, UpdateUserDto updateUserDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        
        user.setName(updateUserDto.getName());
        user.setEmail(updateUserDto.getEmail());
        user.setStatus(updateUserDto.getStatus());
        
        Set<Role> roles = new HashSet<>();
        updateUserDto.getRoles().forEach(roleName -> {
            Role userRole = roleRepository.findByName("ROLE_" + roleName.toUpperCase())
                    .orElseThrow(() -> new APIException(HttpStatus.BAD_REQUEST, "Role not found: " + roleName));
            roles.add(userRole);
        });
        user.setRoles(roles);
        
        User updatedUser = userRepository.save(user);

        // ✅ GHI NHẬT KÝ
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User actor = userRepository.findByUsername(username).orElse(null);
        if (actor != null) {
            String details = String.format("Đã cập nhật thông tin cho tài khoản '%s'.", updatedUser.getUsername());
            activityLogService.logActivity(actor, "CẬP NHẬT TÀI KHOẢN", details);
        }

        return mapToDto(updatedUser);
    }

    @Override
    public void resetPassword(long id, ResetPasswordDto resetPasswordDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        
        user.setPassword(passwordEncoder.encode(resetPasswordDto.getNewPassword()));
        userRepository.save(user);

        // ✅ GHI NHẬT KÝ
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User actor = userRepository.findByUsername(username).orElse(null);
        if (actor != null) {
            String details = String.format("Đã đặt lại mật khẩu cho tài khoản '%s'.", user.getUsername());
            activityLogService.logActivity(actor, "ĐẶT LẠI MẬT KHẨU", details);
        }
    }
    
    private UserDto mapToDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        userDto.setStatus(user.getStatus());
        userDto.setRoles(user.getRoles().stream().map(role -> 
            role.getName().replace("ROLE_", "")
        ).collect(Collectors.toSet()));
        return userDto;
    }
}