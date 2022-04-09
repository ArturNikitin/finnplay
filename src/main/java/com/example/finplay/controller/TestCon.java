package com.example.finplay.controller;

import com.example.finplay.dao.UserRepository;
import com.example.finplay.dto.UserCred;
import com.example.finplay.dto.UserDto;
import com.example.finplay.model.User;
import com.example.finplay.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class TestCon {

	private final UserService userService;

//	@GetMapping("/test2")
//	public List<User> test2(){
//		return repository.findAll();
//	}

	@GetMapping
	public String test() {
		return "hello v2";
	}

//	@PostMapping("/test3")
//	public UserDto user(@RequestBody UserCred userCred) {
//		return userService.create(userCred);
//	}
}
