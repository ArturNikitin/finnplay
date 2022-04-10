package com.example.finplay.service.impl;

import com.example.finplay.dao.UserRepository;
import com.example.finplay.dto.NewPasswordForm;
import com.example.finplay.dto.UserDto;
import com.example.finplay.dto.UserRegistrationForm;
import com.example.finplay.model.User;
import com.example.finplay.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.EntityNotFoundException;
import java.security.SecureRandom;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

	@Mock
	private UserRepository userRepository;

	@Spy
	private PasswordEncoder encoder = new BCryptPasswordEncoder(8, new SecureRandom());

	@InjectMocks
	private UserServiceImpl userService;

	@BeforeEach
	void setUp() {
	}

	@Test
	void getByIdSuccessTest() {
		when(userRepository.findById(1L)).thenReturn(Optional.of(user));

		var user = userService.getById(1L);

		assertEquals(1l, user.getId());
		assertEquals(email, user.getEmail());
		verify(userRepository, times(1)).findById(1L);
	}

	@Test
	void createSuccessTest() {
		var user = User.builder()
			.email(email)
			.firstName("Bob")
			.lastName("Malkovic")
			.build();
		when(userRepository.save(user)).thenReturn(user);

		UserDto userDto = userService.create(getRegistrationForm());

		verify(userRepository, times(1)).save(user);
		verify(encoder, times(1)).encode(getRegistrationForm().getPassword());
		assertEquals(email, userDto.getEmail());
	}

	@Test
	void updatePasswordSuccess() {
		when(userRepository.findById(1L)).thenReturn(Optional.of(user));

		var newPasswordForm = NewPasswordForm.builder()
			.newPassword("1234")
			.newPasswordCopy("1234")
			.oldPassword("12345").build();

		Mockito.when(encoder.matches("12345", "12345")).thenReturn(true);
		Mockito.when(userRepository.save(user)).thenReturn(User.builder().email(email)
			.id(1L)
			.firstName("Bob")
			.lastName("Malkovic")
			.password("1234")
			.build());

		userService.updatePassword(1L, newPasswordForm);

		verify(userRepository, times(1)).findById(1L);
		verify(userRepository, times(1)).save(user);
		verify(encoder, times(1)).matches("12345", "12345");
	}

	@Test()
	void updatePasswordFailUserNotFoundTest() {
		when(userRepository.findById(1L)).thenReturn(Optional.empty());

		var newPasswordForm = NewPasswordForm.builder()
			.newPassword("1234")
			.newPasswordCopy("1234")
			.oldPassword("12345").build();

		assertThrows(EntityNotFoundException.class, () -> userService.updatePassword(1L, newPasswordForm));

	}

	private static final String email = "user@gmail.com";


	private User user = User.builder()
		.email(email)
		.id(1L)
		.firstName("Bob")
		.lastName("Malkovic")
		.password("12345")
		.build();


	private UserRegistrationForm getRegistrationForm() {
		return UserRegistrationForm.builder()
			.email(email)
			.password("12345")
			.firstName("Bob")
			.lastName("Malkovic")
			.build();
	}
}