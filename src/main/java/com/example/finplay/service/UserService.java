package com.example.finplay.service;

import com.example.finplay.dto.UserDto;
import com.example.finplay.dto.UserRegistrationForm;

public interface UserService {
	UserDto create(UserRegistrationForm user);
	UserDto update(Long userId, UserDto user);
	UserDto get(String email);
	UserDto getById(Long id);
}
