package com.example.art_gal.service.impl;

import com.example.art_gal.entity.User;
import com.example.art_gal.exception.APIException;
import com.example.art_gal.exception.ResourceNotFoundException;
import com.example.art_gal.payload.ChangePasswordDto;
import com.example.art_gal.payload.ProfileDto;
import com.example.art_gal.payload.UserDto;
import com.example.art_gal.repository.UserRepository;
import com.example.art_gal.service.ProfileService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class ProfileServiceImpl implements ProfileService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public ProfileServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    private User getActor() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", 0));
    }

    @Override
    public UserDto getCurrentUser() {
        return mapToDto(getActor());
    }

    @Override
    public UserDto updateProfile(ProfileDto profileDto) {
        User user = getActor();
        user.setName(profileDto.getName());
        user.setEmail(profileDto.getEmail());
        User updatedUser = userRepository.save(user);
        return mapToDto(updatedUser);
    }

    @Override
    public void changePassword(ChangePasswordDto changePasswordDto) {
        User user = getActor();

        // Kiểm tra mật khẩu hiện tại có đúng không
        if (!passwordEncoder.matches(changePasswordDto.getCurrentPassword(), user.getPassword())) {
            throw new APIException(HttpStatus.BAD_REQUEST, "Mật khẩu hiện tại không chính xác.");
        }
        
        // Cập nhật mật khẩu mới
        user.setPassword(passwordEncoder.encode(changePasswordDto.getNewPassword()));
        userRepository.save(user);
    }

    private UserDto mapToDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        userDto.setStatus(user.getStatus());
        userDto.setRoles(user.getRoles().stream()
                .map(role -> role.getName().replace("ROLE_", ""))
                .collect(Collectors.toSet()));
        return userDto;
    }
}