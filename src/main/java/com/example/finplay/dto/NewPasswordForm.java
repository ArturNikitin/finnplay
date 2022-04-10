package com.example.finplay.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class NewPasswordForm {
	@Size(min = 6, max = 50, message = "Password cannot be shorter than 6 and longer than 50")
	@Pattern(regexp = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z]*",
		message = "Password must contain at least one small character, one big character and a number")
	String newPassword;
	@NotBlank
	String newPasswordCopy;
	@NotBlank
	String  oldPassword;
}
