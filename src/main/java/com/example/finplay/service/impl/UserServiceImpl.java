package com.example.finplay.service.impl;

import com.example.finplay.dao.UserRepository;
import com.example.finplay.dto.NewPasswordForm;
import com.example.finplay.dto.UserCred;
import com.example.finplay.dto.UserDto;
import com.example.finplay.dto.UserRegistrationForm;
import com.example.finplay.model.User;
import com.example.finplay.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	private final UserRepository userRepository;
	private final PasswordEncoder encoder;

	@Override
	@Transactional(readOnly = true)
	public UserDto getById(Long userId) {
		var user = getUserByIdOrThrow(userId);
		return UserDto.builder()
			.id(user.getId())
			.email(user.getEmail())
			.birthDate(user.getBirthdate())
			.firstName(user.getFirstName())
			.lastName(user.getLastName())
			.build();
	}

	@Override
	@Transactional
	public UserDto create(UserRegistrationForm user) {
		var userToSave = User.builder()
			.email(user.getEmail())
			.firstName(user.getFirstName())
			.lastName(user.getLastName())
			.password(encoder.encode(user.getPassword()))
			.birthdate(user.getBirthDate())
			.build();

		return saveOrUpdate(userToSave);
	}

	@Override
	@Transactional
	public UserDto update(Long userId, UserDto user) {
		var oldUser = getUserByIdOrThrow(userId);
		return saveOrUpdate(update(oldUser, user));
	}

	@Override
	@Transactional(readOnly = true)
	public UserDto get(String email) {
		var user = getUserByEmailOrThrow(email);
		return UserDto.builder()
			.id(user.getId())
			.email(user.getEmail())
			.firstName(user.getFirstName())
			.lastName(user.getLastName())
			.birthDate(user.getBirthdate())
			.build();
	}

	@Override
	@Transactional
	public UserDto updateEmail(Long userId, UserCred userCred) {
		var user = getUserByIdOrThrow(userId);
		checkPassword(userCred.getPassword(), user.getPassword());
		user.setEmail(userCred.getEmail());
		var updatedUser = saveOrUpdate(user);
		return updatedUser;

	}

	@Override
	@Transactional
	public UserDto updatePassword(Long userId, NewPasswordForm passwordForm) {
		User user = getUserByIdOrThrow(userId);
		checkPassword(passwordForm.getOldPassword(), user.getPassword());
		user.setPassword(encoder.encode(passwordForm.getNewPassword()));
		var updatedUser = saveOrUpdate(user);
		return updatedUser;
	}

	private void checkPassword(String oldPassword, String newPassword) {
		if (!encoder.matches(oldPassword, newPassword))
			throw new SecurityException("Password is not correct");
	}

	private User getUserByEmailOrThrow(String email) {
		return userRepository.findByEmail(email)
			.orElseThrow(() -> new EntityNotFoundException("User not found " + email));
	}

	private User getUserByIdOrThrow(Long userId) {
		var user = userRepository.findById(userId)
			.orElseThrow(() -> new EntityNotFoundException("User not found " + userId));
		return user;
	}

	private User update(User oldUser, UserDto user) {
		oldUser.setEmail(user.getEmail());
		oldUser.setFirstName(user.getFirstName());
		oldUser.setLastName(user.getLastName());
		oldUser.setBirthdate(user.getBirthDate());
		return oldUser;
	}

	private UserDto saveOrUpdate(User user) {
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
