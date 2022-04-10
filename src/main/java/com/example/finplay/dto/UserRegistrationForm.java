package com.example.finplay.dto;

import com.example.finplay.validation.NotExists;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationForm {
	@Email(message = "Email is not valid")
	@NotExists(message = "User with this email already exists")
	private String email;

	@Size(min = 6, max = 50, message = "Password cannot be shorter than 6 and longer than 50")
	@Pattern(regexp = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z]*",
		message = "Password must contain at least one small character, one big character and a number")
	private String password;

	@NotBlank(message = "Cannot be empty")
	private String firstName;

	@NotBlank(message = "Cannot be empty")
	private String lastName;

	@Past
	@NotNull
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date birthDate;
}
