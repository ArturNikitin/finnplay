package com.example.finplay.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data(staticConstructor = "of")
public class UserCred {
	private String email;
	private String password;
}
