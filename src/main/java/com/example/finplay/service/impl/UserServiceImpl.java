package com.example.finplay.service.impl;

import com.example.finplay.dao.UserRepository;
import com.example.finplay.dto.NewPasswordForm;
import com.example.finplay.dto.UserCred;
import com.example.finplay.dto.UserDto;
import com.example.finplay.dto.UserRegistrationForm;
import com.example.finplay.model.User;
import com.example.finplay.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	private final UserRepository userRepository;
	private final PasswordEncoder encoder;
	@Lazy
	private final AuthenticationManager authenticationManager;

	@Override
	@Transactional(readOnly = true)
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
		var oldUser = userRepository.findById(userId)
			.orElseThrow(() -> new EntityNotFoundException("User not found " + user.getEmail()));
		return saveOrUpdate(update(oldUser, user));
	}

	@Override
	@Transactional(readOnly = true)
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

	@Override
	@Transactional
	public UserDto updateEmail(Long userId, UserCred userCred) {
		var user = userRepository.findById(userId)
			.orElseThrow(() -> new EntityNotFoundException("User not found " + userId));
		if (!encoder.matches(userCred.getPassword(), user.getPassword()))
			throw new SecurityException("Password is not correct");
		user.setEmail(userCred.getEmail());
		var updatedUser = saveOrUpdate(user);
		updateSecurity(user);
		return updatedUser;

	}

	@Override
	@Transactional
	public UserDto updatePassword(Long userId, NewPasswordForm passwordForm) {
		var user = userRepository.findById(userId)
			.orElseThrow(() -> new EntityNotFoundException("User not found " + userId));
		if (!encoder.matches(passwordForm.getOldPassword(), user.getPassword()))
			throw new SecurityException("WTF");
		user.setPassword(passwordForm.getNewPassword());
		var updatedUser = saveOrUpdate(user);
		updateSecurity(user);
		return updatedUser;
	}

	private void updateSecurity(User user) {
		var encode = encoder.encode(user.getPassword());
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), encode));
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
