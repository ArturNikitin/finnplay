package com.example.finplay.service.impl;

import com.example.finplay.dao.UserRepository;
import com.example.finplay.dto.UserDto;
import com.example.finplay.dto.UserRegistrationForm;
import com.example.finplay.model.User;
import com.example.finplay.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	private final UserRepository userRepository;
	private final PasswordEncoder encoder;

	@Override
	public UserDto getById(Long userId) {
		var user = userRepository.findById(userId)
			.orElseThrow(() -> new EntityNotFoundException("User not found " + userId));
		return UserDto.builder()
			.id(user.getId())
			.email(user.getEmail())
			.firstName(user.getFirstName())
			.lastName(user.getLastName())
			.build();
	}

	@Override
	public UserDto create(UserRegistrationForm user) {
		var userToSave = User.builder()
			.email(user.getEmail())
			.firstName(user.getFirstName())
			.lastName(user.getLastName())
			.password(encoder.encode(user.getPassword()))
			.birthdate(user.getBirthDate())
			.build();

		return save(userToSave);
	}

	@Override
	public UserDto update(Long userId, UserDto user) {
		var oldUser = userRepository.findById(userId)
			.orElseThrow(() -> new EntityNotFoundException("User not found " + user.getEmail()));
		return save(update(oldUser, user));
	}

	@Override
	public UserDto get(String email) {
		var user = userRepository.findByEmail(email)
			.orElseThrow(() -> new EntityNotFoundException("User not found " + email));
		return UserDto.builder()
			.id(user.getId())
			.email(user.getEmail())
			.firstName(user.getFirstName())
			.lastName(user.getLastName())
			.birthDate(user.getBirthdate())
			.build();
	}

	private User update(User oldUser, UserDto user) {
		oldUser.setEmail(user.getEmail());
		oldUser.setFirstName(user.getFirstName());
		oldUser.setLastName(user.getLastName());
		oldUser.setBirthdate(user.getBirthDate());
		return oldUser;
	}

	private UserDto save(User user) {
		User save = userRepository.save(user);
		return UserDto.builder()
			.id(save.getId())
			.email(save.getEmail())
			.firstName(save.getFirstName())
			.lastName(save.getLastName())
			.birthDate(save.getBirthdate())
			.build();
	}
}
