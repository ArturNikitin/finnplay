package com.example.finplay.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
	private Long id;
	private String email;
	@NotBlank(message = "Cannot be empty")
	private String firstName;
	@NotBlank(message = "Cannot be empty")
	private String lastName;
	@Past
	@NotNull
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date birthDate;
}
