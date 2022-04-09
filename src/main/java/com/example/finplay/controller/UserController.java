package com.example.finplay.controller;

import com.example.finplay.dto.UserCred;
import com.example.finplay.dto.UserDto;
import com.example.finplay.dto.UserRegistrationForm;
import com.example.finplay.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UserController {
	private final UserService userService;


	@GetMapping("/index")
	public String index() {
		return "index";
	}

	@RequestMapping("/login")
	public String login() {
		return "login";
	}

	@GetMapping("/signup")
	public String signup(Model model) {
		log.info("/signup");
		model.addAttribute(new UserRegistrationForm());
		return "signup";
	}

	@PostMapping("/signup")
	public ModelAndView signup(@ModelAttribute @Valid UserRegistrationForm user,
	                           BindingResult bindingResult) {
		log.info("/signup post: " + user.toString());
		if (bindingResult.hasErrors()) return new ModelAndView("signup");
		UserDto userDto = userService.create(user);
		var modelView = new ModelAndView("redirect:profile");
		modelView.addObject("user", userDto);
		return modelView;
	}

//	@GetMapping("/profile/update/{iserId}")
//	public String updateProfile(Model model){
//		model.addAttribute("user", new UserDto());
//		return "updateProfile";
//	}

	@PostMapping("/update/{userId}")
	public ModelAndView updateProfile(@Valid @ModelAttribute UserDto userDto,
	                                  BindingResult bindingResult,
	                                  @PathVariable final Long userId) {
		log.info("/update: " + userDto.toString());
		ModelAndView profile = new ModelAndView("profile");
		if (bindingResult.hasErrors()) {
			profile.addObject("userDto", userService.getById(userId));
			profile.addObject("org.springframework.validation.BindingResult.userDto", bindingResult);
			log.info("binding resul " + bindingResult);
			return profile;
		}
		var updated = userService.update(userId, userDto);
		var modelView = profile;
		modelView.addObject("userDto", updated);
		return modelView;
	}

	@GetMapping("/profile")
	public String getProfile(Principal principal, Model model) {
		log.info("/profile: " + principal.getName());
		UserDto user = userService.get(principal.getName());
		model.addAttribute("userDto", user);
		return "profile";
	}

//	@GetMapping("/profile")
//	public String getProfile(@PathVariable Long userId) {
//		log.info("/profile: " + userId);
//		UserDto user = userService.getById(userId);
//		var modelView = new ModelAndView("profile");
//		modelView.addObject("user", user);
//		return "profile";
//	}
}
