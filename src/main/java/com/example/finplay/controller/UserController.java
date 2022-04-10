package com.example.finplay.controller;

import com.example.finplay.dto.NewPasswordForm;
import com.example.finplay.dto.UserCred;
import com.example.finplay.dto.UserDto;
import com.example.finplay.dto.UserRegistrationForm;
import com.example.finplay.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.security.Principal;

import static com.example.finplay.controller.View.PROFILE;
import static com.example.finplay.controller.View.SIGNUP;
import static com.example.finplay.controller.View.UPDATE_EMAIL;
import static com.example.finplay.controller.View.UPDATE_PASSWORD;
import static com.example.finplay.controller.View.UPDATE_PROFILE;

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
		log.info("GET: signup");
		model.addAttribute(new UserRegistrationForm());
		return SIGNUP.value;
	}

	@PostMapping("/signup")
	public ModelAndView signup(@ModelAttribute @Valid UserRegistrationForm user,
	                           BindingResult bindingResult) {
		log.info("POST: /signup: " + user.toString());
		if (bindingResult.hasErrors()) return new ModelAndView("signup");
		UserDto userDto = userService.create(user);
		var modelView = new ModelAndView("redirect:" + PROFILE.value);
		modelView.addObject("user", userDto);
		return modelView;
	}

	@GetMapping("/update/profile/{userId}")
	public String getUpdateProfile(Model model, @PathVariable final Long userId) {
		log.info("GET: update/profile: " + userId);
		UserDto user = userService.getById(userId);
		model.addAttribute("userDto", user);
		return UPDATE_PROFILE.value;
	}

	@PostMapping("/update/profile/{userId}")
	public ModelAndView updateProfile(@Valid @ModelAttribute UserDto userDto,
	                                  BindingResult bindingResult,
	                                  @PathVariable final Long userId) {
		log.info("POST: update/profile: " + userDto.toString());
		if (bindingResult.hasErrors()) {
			ModelAndView profile = new ModelAndView(UPDATE_PROFILE.value);
			profile.addObject("userDto", userService.getById(userId));
			profile.addObject("org.springframework.validation.BindingResult.userDto", bindingResult);
			return profile;
		}
		ModelAndView profile = new ModelAndView(PROFILE.value);
		var updated = userService.update(userId, userDto);
		var modelView = profile;
		modelView.addObject("userDto", updated);
		return modelView;
	}

	@GetMapping("/update/email/{userId}")
	public String updateEmail(Model model, @PathVariable final Long userId) {
		log.info("GET: update/email: " + userId);
		UserDto userDto = userService.getById(userId);
		model.addAttribute("userDto", userDto);
		model.addAttribute("userCred", new UserCred());
		return UPDATE_EMAIL.value;
	}

	@PostMapping("/update/email/{userId}")
	public String updateEmail(@Valid @ModelAttribute UserCred userCred,
	                          BindingResult bindingResult,
	                          @PathVariable final Long userId,
	                          Model model) {
		log.info("POST: update/email " + userCred.getEmail());
		if (bindingResult.hasErrors()) {
			model.addAttribute("userId", userId);
			return UPDATE_EMAIL.value;
		}
		UserDto userDto = userService.updateEmail(userId, userCred);
		model.addAttribute("userDto", userDto);
		return PROFILE.value;
	}

	@GetMapping("/profile")
	public String getProfile(Principal principal, Model model) {
		log.info("GET: /profile: " + principal.getName());
		UserDto user = userService.get(principal.getName());
		model.addAttribute("userDto", user);
		return PROFILE.value;
	}

	@GetMapping("/update/password/{userId}")
	public String updatePassword(Model model,
	                             @PathVariable final Long userId) {
		log.info("GET: update/password " + userId);
		UserDto user = userService.getById(userId);
		model.addAttribute("userDto", user);
		model.addAttribute("newPasswordForm", NewPasswordForm.builder().build());
		return UPDATE_PASSWORD.value;
	}

	@PostMapping("/update/password/{userId}")
	public String updatePassword(@Valid NewPasswordForm newPasswordForm,
	                             final BindingResult bindingResult,
	                             Model model,
	                             @PathVariable final Long userId) {
		log.info("POST: update/password " + userId);
		if (!newPasswordForm.getNewPassword().equals(newPasswordForm.getNewPasswordCopy())) {
			bindingResult.addError(new FieldError("passwordForm", "newPasswordCopy", "Must be same password"));
		}
		if (bindingResult.hasErrors()) {
			model.addAttribute("userId", userId);
			return UPDATE_PASSWORD.value;
		}
		UserDto userDto = userService.updatePassword(userId, newPasswordForm);
		model.addAttribute("userDto", userDto);
		return PROFILE.value;
	}
}
