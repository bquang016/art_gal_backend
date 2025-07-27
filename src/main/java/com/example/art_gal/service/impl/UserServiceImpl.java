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
import com.example.art_gal.service.UserService;
import org.springframework.http.HttpStatus;
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

    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
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

        Set<Role> roles = new HashSet<>();
        userCreateDto.getRoles().forEach(roleName -> {
            // Tự động thêm tiền tố ROLE_ để tìm trong DB
            Role userRole = roleRepository.findByName("ROLE_" + roleName.toUpperCase())
                    .orElseThrow(() -> new APIException(HttpStatus.BAD_REQUEST, "Role not found: " + roleName));
            roles.add(userRole);
        });
        user.setRoles(roles);

        userRepository.save(user);

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
        
        Set<Role> roles = new HashSet<>();
        updateUserDto.getRoles().forEach(roleName -> {
            Role userRole = roleRepository.findByName("ROLE_" + roleName.toUpperCase())
                    .orElseThrow(() -> new APIException(HttpStatus.BAD_REQUEST, "Role not found: " + roleName));
            roles.add(userRole);
        });
        user.setRoles(roles);
        
        User updatedUser = userRepository.save(user);
        return mapToDto(updatedUser);
    }

    @Override
    public void resetPassword(long id, ResetPasswordDto resetPasswordDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        
        user.setPassword(passwordEncoder.encode(resetPasswordDto.getNewPassword()));
        userRepository.save(user);
    }
    
    // Helper method để chuyển User Entity sang User DTO an toàn
    private UserDto mapToDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        userDto.setRoles(user.getRoles().stream().map(role -> 
            role.getName().replace("ROLE_", "")
        ).collect(Collectors.toSet()));
        return userDto;
    }
}