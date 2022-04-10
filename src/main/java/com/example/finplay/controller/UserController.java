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

	@GetMapping("/edit/profile/{userId}")
	public String getEditProfile(Model model, @PathVariable final Long userId) {
		log.info("/edit/profile: " + userId);
		UserDto user = userService.getById(userId);
		model.addAttribute("userDto", user);
		return "editProfile";
	}

	@PostMapping("/edit/profile/{userId}")
	public ModelAndView updateProfile(@Valid @ModelAttribute UserDto userDto,
	                                  BindingResult bindingResult,
	                                  @PathVariable final Long userId) {
		log.info("/edit/profile: " + userDto.toString());
		if (bindingResult.hasErrors()) {
			ModelAndView profile = new ModelAndView("editProfile");
			profile.addObject("userDto", userService.getById(userId));
			profile.addObject("org.springframework.validation.BindingResult.userDto", bindingResult);
			return profile;
		}
		ModelAndView profile = new ModelAndView("profile");
		var updated = userService.update(userId, userDto);
		var modelView = profile;
		modelView.addObject("userDto", updated);
		return modelView;
	}

	@GetMapping("/update/email/{userId}")
	public String updateEmail(Model model, @PathVariable final Long userId) {
		log.info("/edit/profile: " + userId);
		UserDto userDto = userService.getById(userId);
		model.addAttribute("userDto", userDto);
		model.addAttribute("userCred", new UserCred());
		return "updateEmail";
	}

	@PostMapping("/update/email/{userId}")
	public String updateEmail(@Valid @ModelAttribute UserCred userCred,
				  BindingResult bindingResult,
	                          @PathVariable final Long userId,
	                          Model model) {
		log.info("/update/email " + userCred.getEmail());
		if(bindingResult.hasErrors()){
			model.addAttribute("userId", userId);
			return "updateEmail";
		}
		UserDto userDto = userService.updateEmail(userId, userCred);
		model.addAttribute("userDto", userDto);
		return "profile";
	}

	@GetMapping("/profile")
	public String getProfile(Principal principal, Model model) {
		log.info("/profile: " + principal.getName());
		UserDto user = userService.get(principal.getName());
		model.addAttribute("userDto", user);
		return "profile";
	}

	@GetMapping("/update/password/{userId}")
	public String updatePassword(Model model,
	                             @PathVariable final Long userId) {
		log.info("/update/password " + userId);
		UserDto user = userService.getById(userId);
		model.addAttribute("userDto", user);
		model.addAttribute("newPasswordForm", new NewPasswordForm());
		return "updatePassword";
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
		if(bindingResult.hasErrors()){
			model.addAttribute("userId", userId);
			return "updatePassword";
		}
		UserDto userDto = userService.updatePassword(userId, newPasswordForm);
		model.addAttribute("userDto", userDto);
		return "profile";
	}
}
