package com.example.finplay.validation;

import com.example.finplay.dao.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


@Component
@RequiredArgsConstructor
public class EntityNotExists implements
	ConstraintValidator<NotExists, String> {
	private final UserRepository userRepository;

	@Override
	public void initialize(NotExists contactNumber) {
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		return !userRepository.existsByEmail(value);
	}

}
