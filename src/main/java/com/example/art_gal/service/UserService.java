// src/main/java/com/example/art_gal/service/UserService.java
package com.example.art_gal.service;

import com.example.art_gal.payload.UpdateUserDto;
import com.example.art_gal.payload.UserCreateDto;
import com.example.art_gal.payload.UserDto;
import com.example.art_gal.payload.ResetPasswordDto;
import java.util.List;

public interface UserService {
    String createUser(UserCreateDto userCreateDto);
    List<UserDto> getAllUsers();
    UserDto updateUser(long id, UpdateUserDto updateUserDto);
    void resetPassword(long id, ResetPasswordDto resetPasswordDto);
}