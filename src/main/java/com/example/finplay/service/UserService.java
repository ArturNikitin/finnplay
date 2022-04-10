package com.example.finplay.service;

import com.example.finplay.dto.NewPasswordForm;
import com.example.finplay.dto.UserCred;
import com.example.finplay.dto.UserDto;
import com.example.finplay.dto.UserRegistrationForm;

public interface UserService {
	UserDto create(UserRegistrationForm user);
	UserDto update(Long userId, UserDto user);
	UserDto get(String email);
	UserDto getById(Long id);
	UserDto updateEmail(Long userId, UserCred userCred);
	UserDto updatePassword(Long userId, NewPasswordForm passwordForm);
}
