package com.example.finplay.dto;

import com.example.finplay.validation.NotExists;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;

@NoArgsConstructor
@Data(staticConstructor = "of")
public class UserCred {
	@Email(message = "Email is not valid")
	@NotExists(message = "User with this email already exists")
	private String email;
	private String password;
}
